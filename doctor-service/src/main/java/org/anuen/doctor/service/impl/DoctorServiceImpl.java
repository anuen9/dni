package org.anuen.doctor.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.anuen.api.client.UserClient;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.entity.dto.UserDto;
import org.anuen.common.enums.ResponseStatus;
import org.anuen.doctor.dao.DoctorMapper;
import org.anuen.doctor.entity.dto.AddDoctorDto;
import org.anuen.doctor.entity.po.Doctor;
import org.anuen.doctor.service.IDoctorService;
import org.anuen.utils.SysUserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl
        extends ServiceImpl<DoctorMapper, Doctor> implements IDoctorService {

    private final SysUserUtil sysUserUtil;

    private final UserClient userClient;

    @Override
    public ResponseEntity<?> add(AddDoctorDto addDoctorDto) {
        Doctor dbDoctor = lambdaQuery() // check doctor are exists?
                .eq(Doctor::getFirstName, addDoctorDto.getFirstName())
                .eq(Doctor::getLastName, addDoctorDto.getLastName())
                .one();
        if (Objects.nonNull(dbDoctor)) {
            return ResponseEntity.fail(ResponseStatus.USER_EXISTS);
        }

        Doctor doctor = Doctor.newInstance(); // convert dto -> po, insert into DB
        BeanUtils.copyProperties(addDoctorDto, doctor);
        doctor.setUid(IdUtil.fastUUID());

        this.baseMapper.insert(doctor);

        UserDto dto4Save = sysUserUtil.getUserDto4Save(doctor); // remote invoke user-service to insert user
        userClient.add(dto4Save);
        return ResponseEntity.success();
    }
}
