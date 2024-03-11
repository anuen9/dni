package org.anuen.advice.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.anuen.advice.dao.AdviceMapper;
import org.anuen.advice.entity.dto.AddAdviceDto;
import org.anuen.advice.entity.po.Advice;
import org.anuen.advice.entity.vo.DetailsAdviceVo;
import org.anuen.advice.enums.NeedNursingEnum;
import org.anuen.advice.enums.NursingFrequencyEnum;
import org.anuen.advice.service.IAdviceService;
import org.anuen.api.client.AppointmentClient;
import org.anuen.api.client.UserClient;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.enums.ResponseStatus;
import org.anuen.common.utils.UserContextHolder;
import org.anuen.utils.RPCRespResolver;
import org.anuen.utils.SysUserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AdviceServiceImpl
        extends ServiceImpl<AdviceMapper, Advice> implements IAdviceService {

    private final SysUserUtil sysUserUtil;

    private final AppointmentClient appointmentClient;

    private final RPCRespResolver respResolver;

    private final UserClient userClient;

    @Override
    public ResponseEntity<?> add(AddAdviceDto addAdviceDto) {
        if (!isLogical(addAdviceDto) || StrUtil.isBlank(addAdviceDto.getContent())) {
            return ResponseEntity.fail(ResponseStatus.PARAM_LOSS_LOGIC);
        }

        String currUserUid = UserContextHolder.getUser(); // check the operator whether doctor or not
        Integer userType = sysUserUtil.getUserType(currUserUid);
        if (userType.equals(-1)) {
            return ResponseEntity.fail(ResponseStatus.REMOTE_PROCEDURE_CALL_ERROR);
        }
        if (!userType.equals(2)) { // not a doctor -> deny
            return ResponseEntity.fail(ResponseStatus.UNAUTHORIZED);
        }

        Advice advice = Advice.newInstance(); // copy dto -> po
        BeanUtils.copyProperties(addAdviceDto, advice);
        final Date now = new Date(System.currentTimeMillis());
        advice.setDoctorUid(currUserUid);
        advice.setCreatedTime(now);
        advice.setUpdatedTime(now);

        final Integer reqApptId = advice.getAppointmentId(); // if appointment id in request was not exist, just insert into 1 database..
        if (Objects.isNull(reqApptId) || reqApptId.equals(0)) {
            this.baseMapper.insert(advice);
            return ResponseEntity.success();
        }

        if (!appointmentClient.isAppointmentExist(reqApptId)) { // if appointment id in request was an invalid value -> return
            return ResponseEntity.fail(ResponseStatus.APPT_NOT_EXIST);
        }

        this.baseMapper.insert(advice); // insert into 2 databases
        ResponseEntity<?> resp = appointmentClient.bindWithAdvice(reqApptId, advice.getAdviceId());
        if (!respResolver.isInvokeSuccess(resp)) { // bind appointment with advice fail
            return ResponseEntity.fail();
        }

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
                .list();
        if (CollectionUtil.isEmpty(dbAdvices)) {
            return ResponseEntity.success();
        }

        List<DetailsAdviceVo> voList = new ArrayList<>();
        for (Advice advice : dbAdvices) {
            String pUid = advice.getPatientUid();
            String dUid = advice.getDoctorUid();
            Integer needNursing = advice.getNeedNursing();
            Integer nursingFrequency = advice.getNursingFrequency();

            ResponseEntity<?> resp = userClient.getNamesByUidList(Arrays.asList(pUid, dUid));
            List<String> names = respResolver.getRespDataOfList(resp, String.class);
            if (CollectionUtil.isEmpty(names)) {
                return ResponseEntity.fail(ResponseStatus.DATABASE_INCONSISTENCY);
            }

            DetailsAdviceVo detailVo = DetailsAdviceVo.newInstance();
            detailVo.setAdviceId(advice.getAdviceId());
            detailVo.setPatientName(names.get(0));
            detailVo.setDoctorName(names.get(1));
            detailVo.setContent(advice.getContent());
            detailVo.setNeedNursing(NeedNursingEnum.getMeaningByValue(needNursing));
            detailVo.setRequiredNursingNumber(advice.getRequiredNursingNumber());
            detailVo.setNursingFrequency(NursingFrequencyEnum.getMeaningByValue(nursingFrequency));
            detailVo.setStatus(advice.getStatus());
            detailVo.setAppointmentId(advice.getAppointmentId());
            detailVo.setCreatedTime(advice.getCreatedTime());
            detailVo.setUpdatedTime(advice.getUpdatedTime());
            voList.add(detailVo);
        }

        return ResponseEntity.success(voList);
    }

    @Override
    public ResponseEntity<?> getOne(@NonNull Integer adviceId) {
        Advice dbAdvice = lambdaQuery().eq(Advice::getAdviceId, adviceId).one();
        if (Objects.isNull(dbAdvice)) {
            return ResponseEntity.success();
        }

        List<String> uidList = new ArrayList<>(Arrays.asList(dbAdvice.getPatientUid(), dbAdvice.getDoctorUid()));
        ResponseEntity<?> resp = userClient.getNamesByUidList(uidList);
        List<String> names = respResolver.getRespDataOfList(resp, String.class);
        if (CollectionUtil.isEmpty(names)) {
            return ResponseEntity.fail(ResponseStatus.REMOTE_PROCEDURE_CALL_ERROR);
        }

        DetailsAdviceVo adviceVo = DetailsAdviceVo.newInstance();
        BeanUtils.copyProperties(dbAdvice, adviceVo);
        adviceVo.setPatientName(names.get(0));
        adviceVo.setDoctorName(names.get(1));
        adviceVo.setNeedNursing(NeedNursingEnum.getMeaningByValue(dbAdvice.getNeedNursing()));
        adviceVo.setNursingFrequency(NursingFrequencyEnum.getMeaningByValue(dbAdvice.getNursingFrequency()));

        return ResponseEntity.success(adviceVo);
    }

    private Boolean isLogical(AddAdviceDto adviceDto) {
        Integer beNeeded = adviceDto.getNeedNursing();
        Integer requiredNursingNumber = adviceDto.getRequiredNursingNumber();
        Integer nursingFrequency = adviceDto.getNursingFrequency();

        if (!NeedNursingEnum.isValueValid(beNeeded)) { // the value of beNeeded are invalid -> return false
            return Boolean.FALSE;
        }

        if (NeedNursingEnum.NEED.getValue().equals(beNeeded)) { // situation of need nursing
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
