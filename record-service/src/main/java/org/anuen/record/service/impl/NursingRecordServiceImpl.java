package org.anuen.record.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.anuen.api.client.AdviceClient;
import org.anuen.api.client.UserClient;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.entity.json.JsonAdvice;
import org.anuen.common.utils.UserContextHolder;
import org.anuen.record.dao.NursingRecordMapper;
import org.anuen.record.entity.RecordDetailVo;
import org.anuen.record.entity.dto.FinishNDto;
import org.anuen.record.entity.dto.NewRecordForm;
import org.anuen.record.entity.po.NursingRecord;
import org.anuen.record.service.INursingRecordService;
import org.anuen.utils.RPCRespResolver;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.anuen.common.enums.ResponseStatus.*;
import static org.anuen.record.enums.HasBadReactionEnum.DO_NOT_HAS;

@Service
@RequiredArgsConstructor
public class NursingRecordServiceImpl
        extends ServiceImpl<NursingRecordMapper, NursingRecord> implements INursingRecordService {

    private final AdviceClient adviceClient;

    private final UserClient userClient;

    private final RPCRespResolver respResolver;

    @Override
    public ResponseEntity<?> startNursing(NewRecordForm newRecordForm) {
        Integer adviceId = newRecordForm.getAdviceId(); // fetch json advice by advice id
        JsonAdvice jAdv = acquireJAdv(adviceId);
        if (Objects.isNull(jAdv)) {
            return ResponseEntity.fail(REMOTE_PROCEDURE_CALL_ERROR);
        }
        final String advStatus = jAdv.getStatus(); // advice status
        final Integer requireNNum = jAdv.getRequiredNursingNumber(); // advice required nursing number
        final String pUid = jAdv.getPatientUid(); // advice patient uid
        if ("Completed!".equals(advStatus)) { // if advice was completed -> return
            return ResponseEntity.fail(ADVICE_HAS_BEEN_COMPLETED);
        }

        NursingRecord lastNRecord = this.baseMapper.selectOne( // select last nursing record
                new LambdaQueryWrapper<NursingRecord>()
                        .eq(NursingRecord::getAdviceId, adviceId)
                        .orderByDesc(NursingRecord::getCurrentNursingNumber)
                        .last("limit 1"));
        int currNNum = 1;
        if (Objects.nonNull(lastNRecord)) { // means this is not first care
            Integer lastNNum = lastNRecord.getCurrentNursingNumber();
            if (lastNNum.compareTo(requireNNum) >= 0) { // last number bigger or equals required number
                return ResponseEntity.fail(ADVICE_HAS_BEEN_COMPLETED);
            }
            currNNum = lastNNum + 1;
        }

        Date now = new Date(System.currentTimeMillis());
        NursingRecord record = NursingRecord.newInstance();
        record.setAdviceId(adviceId);
        record.setNursingDateStart(now);
        record.setNursingDateEnd(now);
        record.setCurrentNursingNumber(currNNum);
        record.setNursingBy(UserContextHolder.getUser());
        record.setPatientUid(pUid);
        record.setHasBadReaction(DO_NOT_HAS.getValue());
        record.setCreateTime(now);
        this.baseMapper.insert(record);
        return ResponseEntity.success(record.getNursingRecordId());
    }

    @Override
    public ResponseEntity<?> finishNursing(FinishNDto fN) {
        final Integer rId = fN.getNursingRecordId();
        final Integer hasBad = fN.getHasBadReaction();
        NursingRecord r = this.baseMapper.selectOne(
                new LambdaQueryWrapper<NursingRecord>()
                        .eq(NursingRecord::getNursingRecordId, rId));
        if (Objects.isNull(r)) {
            return ResponseEntity.fail(DATABASE_NO_RECORD);
        }
        if (!r.getNursingBy().equals(UserContextHolder.getUser())) {
            return ResponseEntity.fail(NURSE_OPERATE_DENY);
        }
        final Integer aId = r.getAdviceId();
        JsonAdvice jAdv = acquireJAdv(aId);
        if (Objects.isNull(jAdv)) {
            return ResponseEntity.fail(REMOTE_PROCEDURE_CALL_ERROR);
        }
        final Integer requireNNum = jAdv.getRequiredNursingNumber();
        if (r.getCurrentNursingNumber().compareTo(requireNNum) == 0) {
            Boolean finishSucceed = adviceClient.finishAdvice(aId);
            if (!finishSucceed) {
                return ResponseEntity.fail(DATABASE_INCONSISTENCY);
            }
        }
        r.setNursingDateEnd(new Date(System.currentTimeMillis()));
        r.setHasBadReaction(hasBad);
        this.baseMapper.updateById(r);
        return ResponseEntity.success();
    }

    @Override
    public ResponseEntity<?> listByPatient(String pUid) {
        List<NursingRecord> list = this.baseMapper.selectList(
                new LambdaQueryWrapper<NursingRecord>()
                        .eq(NursingRecord::getPatientUid, pUid)
                        .orderByDesc(NursingRecord::getCreateTime));
        if (CollectionUtil.isEmpty(list)) {
            return ResponseEntity.success(new ArrayList<>());
        }

        List<RecordDetailVo> voList;
        try {
            voList = list.stream().map(this::convertToVo).toList();
        } catch (Exception e) {
            return ResponseEntity.fail(REMOTE_PROCEDURE_CALL_ERROR);
        }
        return ResponseEntity.success(voList);
    }

    private RecordDetailVo convertToVo(NursingRecord r) {
        RecordDetailVo vo = new RecordDetailVo();
        vo.setRecordId(r.getNursingRecordId());
        vo.setAdviceId(r.getAdviceId());
        vo.setStartTime(r.getNursingDateStart());
        vo.setEndTime(r.getNursingDateEnd());
        vo.setThisNum(r.getCurrentNursingNumber());
        List<String> uidList = new ArrayList<>(
                Arrays.asList(r.getNursingBy(), r.getPatientUid()));
        ResponseEntity<?> resp = userClient.getNamesByUidList(uidList);
        List<String> names = respResolver.getRespDataOfList(resp, String.class);
        if (CollectionUtil.isEmpty(names) || names.size() != uidList.size()) {
            throw new RuntimeException();
        }
        vo.setNursingBy(names.get(0));
        vo.setPatientName(names.get(1));
        vo.setBadReaction(r.getHasBadReaction().equals(1));
        vo.setCreateTime(r.getCreateTime());
        return vo;
    }

    @Nullable
    private JsonAdvice acquireJAdv(@NonNull Integer aId) {
        String json = adviceClient.fetchOneOfJson(aId);
        if (StrUtil.isBlank(json)) {
            return null;
        }
        return JSONObject.parseObject(json, JsonAdvice.class);
    }

}
