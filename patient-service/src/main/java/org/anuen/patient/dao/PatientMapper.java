package org.anuen.patient.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.anuen.patient.entity.po.Patient;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PatientMapper extends BaseMapper<Patient> {
}
