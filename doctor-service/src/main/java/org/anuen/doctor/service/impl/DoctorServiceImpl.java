package org.anuen.doctor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.anuen.doctor.dao.DoctorMapper;
import org.anuen.doctor.entity.po.Doctor;
import org.anuen.doctor.service.IDoctorService;
import org.springframework.stereotype.Service;

@Service
public class DoctorServiceImpl
        extends ServiceImpl<DoctorMapper, Doctor> implements IDoctorService {
}
