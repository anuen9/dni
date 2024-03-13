package org.anuen.advice.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.anuen.advice.dao.AdviceMapper;
import org.anuen.advice.entity.dto.AddAdviceDto;
import org.anuen.advice.entity.po.Advice;
import org.anuen.advice.entity.vo.BindAdviceVo;
import org.anuen.advice.entity.vo.DetailsAdviceVo;
import org.anuen.advice.enums.NeedNursingEnum;
import org.anuen.advice.enums.NursingFrequencyEnum;
import org.anuen.advice.service.IAdviceService;
import org.anuen.advice.service.IAdviceTransService;
import org.anuen.api.client.AppointmentClient;
import org.anuen.api.client.UserClient;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.entity.json.JsonAdvice;
import org.anuen.common.exception.DatabaseException;
import org.anuen.common.utils.UserContextHolder;
import org.anuen.utils.RPCRespResolver;
import org.anuen.utils.SysUserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.anuen.advice.enums.NeedNursingEnum.NEED;
import static org.anuen.advice.enums.NeedNursingEnum.getMeaningByValue;
import static org.anuen.common.enums.ResponseStatus.*;

@Service
@RequiredArgsConstructor
public class AdviceServiceImpl
        extends ServiceImpl<AdviceMapper, Advice> implements IAdviceService {

    private final SysUserUtil sysUserUtil;

    private final AppointmentClient appointmentClient;

    private final RPCRespResolver respResolver;

    private final UserClient userClient;

    private final IAdviceTransService adviceTransService;

    @Override
    public ResponseEntity<?> add(AddAdviceDto addAdviceDto) {
        if (!isLogical(addAdviceDto) || StrUtil.isBlank(addAdviceDto.getContent())) {
            return ResponseEntity.fail(PARAM_LOSS_LOGIC);
        }

        String currUserUid = UserContextHolder.getUser(); // check the operator whether doctor or not
        Integer userType = sysUserUtil.getUserType(currUserUid);
        if (userType.equals(-1)) {
            return ResponseEntity.fail(REMOTE_PROCEDURE_CALL_ERROR);
        }
        if (!userType.equals(2)) { // not a doctor -> deny
            return ResponseEntity.fail(UNAUTHORIZED);
        }

        Advice advice = Advice.newInstance(); // copy dto -> po
        BeanUtils.copyProperties(addAdviceDto, advice);
        final Date now = new Date(System.currentTimeMillis());
        advice.setDoctorUid(currUserUid);
        advice.setCreatedTime(now);
        advice.setUpdatedTime(now);

        final Integer reqApptId = advice.getAppointmentId(); // if appointment id in request was not exist, just insert into 1 database..
        advice.setAppointmentId(0);
        if (Objects.isNull(reqApptId) || reqApptId.equals(0)) {
            this.baseMapper.insert(advice);
            return ResponseEntity.success();
        }

        if (!appointmentClient.isAppointmentExist(reqApptId)) { // if appointment id in request was an invalid value -> return
            return ResponseEntity.fail(APPT_NOT_EXIST);
        }

        this.baseMapper.insert(advice); // insert into 2 databases

        adviceTransService.callBack(this::bidirectionalBind, advice.getAdviceId(), reqApptId);

        return ResponseEntity.success();
    }

    @Override
    public Boolean isAdviceExist(Integer adviceId) {
        Advice dbAdvice = lambdaQuery()
                .select(Advice::getAdviceId)
                .eq(Advice::getAdviceId, adviceId)
                .one();
        if (Objects.isNull(dbAdvice)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public ResponseEntity<?> getListByPatient(String patientUid) {
        List<Advice> dbAdvices = lambdaQuery()
                .eq(Advice::getPatientUid, patientUid)
                .orderByDesc(Advice::getUpdatedTime)
                .list();
        if (CollectionUtil.isEmpty(dbAdvices)) {
            return ResponseEntity.success(new ArrayList<>());
        }

        List<DetailsAdviceVo> vos;
        try {
            vos = dbAdvices.stream().map(this::convertToVo).toList();
        } catch (Exception e) {
            return ResponseEntity.fail(TYPE_CONVERSION_ERROR);
        }
        return ResponseEntity.success(vos);
    }

    @Override
    public ResponseEntity<?> getOne(@NonNull Integer adviceId) {
        Advice dbAdvice = lambdaQuery().eq(Advice::getAdviceId, adviceId).one();
        if (Objects.isNull(dbAdvice)) {
            return ResponseEntity.success();
        }
        DetailsAdviceVo adviceVo;
        try {
            adviceVo = convertToVo(dbAdvice);
        } catch (Exception e) {
            return ResponseEntity.fail(REMOTE_PROCEDURE_CALL_ERROR);
        }
        return ResponseEntity.success(adviceVo);
    }

    @Override
    @Transactional
    public ResponseEntity<?> bidirectionalBind(Integer adviceId, Integer apptId) {
        Boolean isApptExist = appointmentClient.isAppointmentExist(apptId);
        Boolean isAdviceExist = isAdviceExist(adviceId);
        if (!isApptExist || !isAdviceExist) {
            return ResponseEntity.fail(DATABASE_INCONSISTENCY);
        }

        Advice dbAdvice = lambdaQuery().eq(Advice::getAdviceId, adviceId).one();
        dbAdvice.setAppointmentId(apptId);
        dbAdvice.setUpdatedTime(new Date(System.currentTimeMillis()));
        this.baseMapper.updateById(dbAdvice);
        ResponseEntity<?> response = appointmentClient.bindWithAdvice(apptId, adviceId);
        if (!respResolver.isInvokeSuccess(response)) {
            throw new DatabaseException("database consistency error");
        }

        return ResponseEntity.success();
    }

    @Override
    public ResponseEntity<?> getNotBoundListByPatient(String pUid) {
        List<Advice> notBoundAdvices = lambdaQuery()
                .eq(Advice::getPatientUid, pUid)
                .eq(Advice::getAppointmentId, 0)
                .orderByDesc(Advice::getUpdatedTime)
                .list();
        if (CollectionUtil.isEmpty(notBoundAdvices)) {
            return ResponseEntity.success(new ArrayList<>());
        }

        List<BindAdviceVo> voList = notBoundAdvices
                .stream()
                .map(advice -> {
                    BindAdviceVo bindVo = BindAdviceVo.newInstance();
                    bindVo.setAdviceId(advice.getAdviceId());
                    bindVo.setContent(advice.getContent());
                    bindVo.setNeedNursing(
                            getMeaningByValue(advice.getNeedNursing()));
                    bindVo.setRequiredNursingNumber(advice.getRequiredNursingNumber());
                    bindVo.setNursingFrequency(
                            NursingFrequencyEnum.getMeaningByValue(advice.getNursingFrequency()));
                    return bindVo;
                })
                .toList();

        return ResponseEntity.success(voList);
    }

    @Override
    public ResponseEntity<?> getNeedNursingAdvices(String pUid) {
        List<Advice> advices = this.baseMapper.selectList(
                new LambdaQueryWrapper<Advice>()
                        .eq(Advice::getPatientUid, pUid)
                        .eq(Advice::getNeedNursing, NEED.getValue())
                        .ne(Advice::getAppointmentId, 0));
        if (CollectionUtil.isEmpty(advices)) {
            return ResponseEntity.success(new ArrayList<>());
        }
        List<DetailsAdviceVo> vos;
        try {
            vos = advices.stream().map(this::convertToVo).toList();
        } catch (RuntimeException re) {
            return ResponseEntity.fail(TYPE_CONVERSION_ERROR);
        }
        return ResponseEntity.success(vos);
    }

    @Override
    public String fetchOneOfJson(Integer adviceId) {
        Advice one = this.baseMapper.selectOne(
                new LambdaQueryWrapper<Advice>()
                        .eq(Advice::getAdviceId, adviceId));
        if (Objects.isNull(one)) {
            return null;
        }
        JsonAdvice jAdv = new JsonAdvice();
        BeanUtils.copyProperties(one, jAdv);
        return JSONObject.toJSONString(one);
    }

    /**
     * passing a pojo and return a details' vo.
     *
     * @param advice pojo from database
     * @return vo of details vo
     */
    private DetailsAdviceVo convertToVo(Advice advice) {
        List<String> uidList = new ArrayList<>(
                Arrays.asList(advice.getPatientUid(), advice.getDoctorUid()));
        ResponseEntity<?> resp = userClient.getNamesByUidList(uidList);
        List<String> names = respResolver.getRespDataOfList(resp, String.class);
        if (CollectionUtil.isEmpty(names)
                || names.size() != uidList.size()) {
            log.error("---> the variable: [names] from remote procedure call maybe dangerous!");
            throw new RuntimeException();
        }
        DetailsAdviceVo vo = DetailsAdviceVo.newInstance();
        BeanUtils.copyProperties(advice, vo);
        int nameIndex = 0;
        vo.setPatientName(names.get(nameIndex++));
        vo.setDoctorName(names.get(nameIndex));
        vo.setNeedNursing(
                getMeaningByValue(advice.getNeedNursing()));
        vo.setNursingFrequency(
                NursingFrequencyEnum.getMeaningByValue(advice.getNursingFrequency()));
        return vo;
    }

    private Boolean isLogical(AddAdviceDto adviceDto) {
        Integer beNeeded = adviceDto.getNeedNursing();
        Integer requiredNursingNumber = adviceDto.getRequiredNursingNumber();
        Integer nursingFrequency = adviceDto.getNursingFrequency();

        if (!NeedNursingEnum.isValueValid(beNeeded)) { // the value of beNeeded are invalid -> return false
            return Boolean.FALSE;
        }

        if (NEED.getValue().equals(beNeeded)) { // situation of need nursing
            if (requiredNursingNumber.equals(0)
                    || nursingFrequency.equals(0)
                    || !Arrays.asList(1, 2, 3).contains(nursingFrequency)) { // but required nursing number or nursing frequency are 0 -> return false
                return Boolean.FALSE;
            }
        } else { // situation of don't need nursing
            if (!requiredNursingNumber.equals(0)
                    || !nursingFrequency.equals(0)) {
                return Boolean.FALSE;
            }
        }
        // the dto has logical
        return Boolean.TRUE;
    }
}
