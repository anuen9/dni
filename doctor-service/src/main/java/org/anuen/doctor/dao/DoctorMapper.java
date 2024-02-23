package org.anuen.doctor.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.anuen.doctor.entity.po.Doctor;
import org.apache.ibatis.annotations.Mapper;



@Mapper
public interface DoctorMapper extends BaseMapper<Doctor> {
}
