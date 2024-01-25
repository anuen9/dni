package org.anuen.patient.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.anuen.api.client.UserClient;
import org.anuen.api.dto.UserDto;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.enums.ResponseStatus;
import org.anuen.patient.config.DefaultInfoProperties;
import org.anuen.patient.dao.PatientMapper;
import org.anuen.patient.entity.dto.PatientDto;
import org.anuen.patient.entity.po.Patient;
import org.anuen.patient.service.IPatientService;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(DefaultInfoProperties.class)
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements IPatientService {

    /**
     * default properties by 'yaml'
     */
    private final DefaultInfoProperties defaultInfoProperties;

    /**
     * open feign client for RPC
     */
    private final UserClient userClient;

    @Override
    public ResponseEntity<?> save(PatientDto patientDto) {
        // convert DTO to PO
        Patient patient = Patient.newInstance();
        BeanUtils.copyProperties(patientDto, patient);
        patient.setUid(IdUtil.fastUUID());
        // storage patient
        save(patient);
        // get userDto by patient
        UserDto user4Save = getUser4Save(patient);
        // call user service to add user by RPC
        userClient.add(user4Save);
        return ResponseEntity.success();
    }

    @Override
    public ResponseEntity<?> findOne(Integer userId) {
        Patient one = getOne(
                new LambdaQueryWrapper<Patient>()
                        .eq(Patient::getId, userId));
        if (Objects.isNull(one)) {
            return ResponseEntity.success(null, ResponseStatus.USER_NOT_FOUND.getMessage());
        }
        return ResponseEntity.success(one);
    }

    /**
     * read patient's information
     * write detail into userDto prepare for 'add'
     *
     * @param patient patient
     * @return userDto for saving
     */
    private UserDto getUser4Save(Patient patient) {
        final String nickName = patient.getFirstName() + patient.getLastName();
        return UserDto.builder()
                .uid(patient.getUid())
                .userType(defaultInfoProperties.getUserType())
                .nickName(nickName)
                .password(defaultInfoProperties.getPassword())
                .build();
    }

}
