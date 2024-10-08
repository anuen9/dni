package org.anuen.patient.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.anuen.api.client.UserClient;
import org.anuen.common.entity.EmailSettings;
import org.anuen.common.entity.ModifyPassForm;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.entity.dto.UserDto;
import org.anuen.common.enums.EmailStatus;
import org.anuen.common.enums.EmailSubjects;
import org.anuen.common.enums.ExceptionMessage;
import org.anuen.common.exception.FormatException;
import org.anuen.common.exception.UnauthorizedException;
import org.anuen.common.utils.UserContextHolder;
import org.anuen.patient.dao.PatientMapper;
import org.anuen.patient.entity.dto.NameSuggestion;
import org.anuen.patient.entity.dto.PatientDto;
import org.anuen.patient.entity.po.Patient;
import org.anuen.patient.entity.vo.PatientVo;
import org.anuen.patient.service.IPatientService;
import org.anuen.utils.SysUserUtil;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.anuen.common.enums.MessageQueueConst.MQ_MODIFY_PASS;
import static org.anuen.common.enums.MessageQueueConst.MQ_VERIFY_EMAIL;
import static org.anuen.common.enums.ResponseStatus.*;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements IPatientService {

    private final SysUserUtil sysUserUtil;

    /**
     * open feign client for RPC
     */
    private final UserClient userClient;

    /**
     * rabbit mq template used to async invoke
     */
    private final RabbitTemplate rabbitTemplate;

    @Override
    public ResponseEntity<?> save(PatientDto patientDto) {
        Patient dbPatient = lambdaQuery() // if user already exists -> return
                .eq(Patient::getFirstName, patientDto.getFirstName())
                .eq(Patient::getLastName, patientDto.getLastName())
                .one();
        if (Objects.nonNull(dbPatient)) {
            return ResponseEntity.fail(USER_EXISTS);
        }

        Patient patient = Patient.newInstance(); // convert dto 2 po -> insert into database
        BeanUtils.copyProperties(patientDto, patient);
        patient.setUid(IdUtil.fastUUID());

        this.baseMapper.insert(patient);

        UserDto user4Save = sysUserUtil.getUserDto4Save(patient); // get userDto by patient
        userClient.add(user4Save); // call service [user-service] to save user
        return ResponseEntity.success();
    }

    @Override
    public ResponseEntity<?> findOne(String uid) {
        Patient dbOne = getOne(
                new LambdaQueryWrapper<Patient>()
                        .eq(Patient::getUid, uid));
        if (Objects.isNull(dbOne)) {
            return ResponseEntity.fail(USER_NOT_FOUND);
        }
        PatientVo vo = PatientVo.newInstance();
        BeanUtils.copyProperties(dbOne, vo);
        vo.setName(dbOne.getLastName() + dbOne.getFirstName());
        return ResponseEntity.success(vo);
    }

    /**
     * modify password
     *
     * @param modifyPassForm modify form
     * @return response entity
     */
    @Override
    public ResponseEntity<?> modifyPass(ModifyPassForm modifyPassForm) {
        String currUserUid = UserContextHolder.getUser(); // get current user uid
        if (StrUtil.isBlank(currUserUid)) {
            log.error("""
                    ---> method: modifyPass().
                    --->---> error: can't get user info(user uid) from thread local!
                    """);
            throw new UnauthorizedException(ExceptionMessage.UNAUTHORIZED.getMessage()); // stop
        }

        Patient dbPatient = lambdaQuery() // check email status
                .eq(Patient::getUid, currUserUid)
                .one();
        if (EmailStatus.EMAIL_NOT_VERIFY.getStatusCode().equals(dbPatient.getEmailStatus())) {
            return ResponseEntity.fail(EMAIL_NOT_VERIFIED);
        }
        assert /* email is verified */
                EmailStatus.EMAIL_HAS_VERIFIED.getStatusCode().equals(dbPatient.getEmailStatus());

        dbPatient.setUpdateTime(new Date(System.currentTimeMillis())); // update patient's [update_time] to now
        updateById(dbPatient);

        /*
         * The validation in [patient-service] is completed,
         * we should actually modify pass now.
         * */
        modifyPassForm.setUserUid(currUserUid); // translate userUid by publish to mq
        try {
            rabbitTemplate.convertAndSend(
                    MQ_MODIFY_PASS.getExchange(),
                    MQ_MODIFY_PASS.getRoutingKey(),
                    modifyPassForm);
        } catch (AmqpException e) {
            log.error("""
                    ---> method: modifyPass().
                    --->---> [patient-service] valid access, but notify [user-service] fail!
                    """, e);
        }
        return ResponseEntity.success();
    }

    @Override
    public ResponseEntity<?> verifyEmail(String email) {
        if (StrUtil.isBlank(email) || !Validator.isEmail(email)) { // check string -> email address
            throw new FormatException(ExceptionMessage.IS_NOT_EMAIL_ADDRESS.getMessage());
        }

        EmailSettings settings = EmailSettings.newSettings() // build settings
                .subject(EmailSubjects.VERIFY_CODE.description())
                .destination(email);

        try { // send
            rabbitTemplate.convertAndSend(
                    MQ_VERIFY_EMAIL.getExchange(),
                    MQ_VERIFY_EMAIL.getRoutingKey(),
                    settings);
        } catch (AmqpException e) {
            log.error("""
                    ---> method: verifyEmail().
                    --->---> notify [email-service] fail!
                    """);
        }
        return ResponseEntity.success();
    }

    @Override
    public ResponseEntity<?> fetchSuggestionsByName(String name) {
        if (StrUtil.isBlank(name)) {
            return ResponseEntity.fail(NECESSARY_PARAM_MISSING);
        }
        String input = "%" + name + "%";
        List<NameSuggestion> suggestNames = this.baseMapper.selectNamesByInput(input);
        if (CollectionUtil.isNotEmpty(suggestNames)) {
            return ResponseEntity.success(suggestNames);
        }
        return ResponseEntity.success(new ArrayList<>());
    }

    @Override
    public ResponseEntity<?> getInfo() {
        String currUsr = UserContextHolder.getUser();
        Patient me = this.baseMapper.selectOne(
                new LambdaQueryWrapper<Patient>()
                        .eq(Patient::getUid, currUsr));
        if (Objects.isNull(me)) {
            return ResponseEntity.fail(PATIENT_PERMISSION_DENY);
        }
        PatientVo vo = PatientVo.newInstance();
        BeanUtils.copyProperties(me, vo);
        vo.setName(me.getNickname());
        return ResponseEntity.success(vo);
    }

}
