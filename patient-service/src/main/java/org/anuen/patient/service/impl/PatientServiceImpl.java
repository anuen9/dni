package org.anuen.patient.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.anuen.api.client.UserClient;
import org.anuen.api.dto.UserDto;
import org.anuen.common.entity.ModifyPassForm;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.enums.ExceptionMessage;
import org.anuen.common.enums.ResponseStatus;
import org.anuen.common.exception.UnauthorizedException;
import org.anuen.common.utils.UserContextHolder;
import org.anuen.patient.config.DefaultInfoProperties;
import org.anuen.patient.config.EmailProperties;
import org.anuen.patient.dao.PatientMapper;
import org.anuen.patient.entity.dto.PatientDto;
import org.anuen.patient.entity.po.Patient;
import org.anuen.patient.service.IPatientService;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(value = {DefaultInfoProperties.class, EmailProperties.class})
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements IPatientService {

    /**
     * default properties by 'yaml'
     */
    private final DefaultInfoProperties defaultInfoProperties;

    /**
     * email properties by 'yaml'
     */
    private final EmailProperties emailProperties;

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
            return ResponseEntity.fail(ResponseStatus.USER_EXISTS);
        }

        Patient patient = Patient.newInstance(); // convert dto 2 po -> insert into database
        BeanUtils.copyProperties(patientDto, patient);
        patient.setUid(IdUtil.fastUUID());
        save(patient);

        UserDto user4Save = getUser4Save(patient); // get userDto by patient
        userClient.add(user4Save); // call service [user-service] to save user
        return ResponseEntity.success();
    }

    @Override
    public ResponseEntity<?> findOne(Integer userId) {
        Patient one = getOne(
                new LambdaQueryWrapper<Patient>()
                        .eq(Patient::getId, userId));
        if (Objects.isNull(one)) {
            return ResponseEntity.fail(ResponseStatus.USER_NOT_FOUND);
        }
        return ResponseEntity.success(one);
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
        if (emailProperties.getEmailNotVerified().equals(dbPatient.getEmailStatus())) {
            return ResponseEntity.fail(ResponseStatus.EMAIL_NOT_VERIFIED);
        }
        assert /* email is verified */emailProperties.getEmailVerified().equals(dbPatient.getEmailStatus());

        dbPatient.setUpdateTime(new Date(System.currentTimeMillis())); // update patient's [update_time] to now
        updateById(dbPatient);

        /*
         * The validation in [patient-service] is completed,
         * we should actually modify pass now.
         * */
        modifyPassForm.setUserUid(currUserUid); // translate userUid by publish to mq
        try {
            rabbitTemplate.convertAndSend(
                    "patient.topic",
                    "modify.pass",
                    modifyPassForm);
        } catch (AmqpException e) {
            log.error("""
                    ---> method: modifyPass().
                    --->---> [patient-service] valid access, but notify [user-service] fail!
                    """, e);
        }
        return ResponseEntity.success();
    }

    /**
     * read patient's information
     * write detail into userDto prepare for 'add'
     *
     * @param patient patient
     * @return userDto for saving
     */
    private UserDto getUser4Save(Patient patient) {
        final String nickName = patient.getLastName() + patient.getFirstName();
        return UserDto.builder()
                .uid(patient.getUid())
                .userType(defaultInfoProperties.getUserType())
                .nickName(nickName)
                .password(defaultInfoProperties.getPassword())
                .build();
    }

}
