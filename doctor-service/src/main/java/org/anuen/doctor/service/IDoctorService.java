package org.anuen.doctor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.doctor.entity.dto.AddDoctorDto;
import org.anuen.doctor.entity.po.Doctor;


public interface IDoctorService extends IService<Doctor> {
    ResponseEntity<?> add(AddDoctorDto addDoctorDto);
}
