package org.anuen.record.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.anuen.api.client.AdviceClient;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.entity.json.JsonAdvice;
import org.anuen.common.utils.UserContextHolder;
import org.anuen.record.dao.NursingRecordMapper;
import org.anuen.record.entity.dto.FinishNDto;
import org.anuen.record.entity.dto.NewRecordForm;
import org.anuen.record.entity.po.NursingRecord;
import org.anuen.record.service.INursingRecordService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

import static org.anuen.common.enums.ResponseStatus.*;
import static org.anuen.record.enums.HasBadReactionEnum.DO_NOT_HAS;

@Service
@RequiredArgsConstructor
public class NursingRecordServiceImpl
        extends ServiceImpl<NursingRecordMapper, NursingRecord> implements INursingRecordService {

    private final AdviceClient adviceClient;

    @Override
    public ResponseEntity<?> startNursing(NewRecordForm newRecordForm) {
        Integer adviceId = newRecordForm.getAdviceId(); // fetch json advice by advice id
        String json = adviceClient.fetchOneOfJson(adviceId);
        if (StrUtil.isBlank(json)) {
            return ResponseEntity.fail(DATABASE_NO_RECORD);
        }
        JsonAdvice jAdv = JSONObject.parseObject(json, JsonAdvice.class);
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
        r.setNursingDateEnd(new Date(System.currentTimeMillis()));
        r.setHasBadReaction(hasBad);
        return ResponseEntity.success();
    }

}
