package org.anuen.patient.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.anuen.api.client.UserClient;
import org.anuen.api.dto.UserDto;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.patient.config.DefaultInfoConfig;
import org.anuen.patient.dao.PatientMapper;
import org.anuen.patient.entity.dto.PatientDto;
import org.anuen.patient.entity.po.Patient;
import org.anuen.patient.service.IPatientService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements IPatientService {

    /**
     * default properties by 'yaml'
     */
    private final DefaultInfoConfig defaultInfoConfig;

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
                .userType(defaultInfoConfig.defaultUserType())
                .nickName(nickName)
                .password(defaultInfoConfig.defaultPassword())
                .build();
    }

}
