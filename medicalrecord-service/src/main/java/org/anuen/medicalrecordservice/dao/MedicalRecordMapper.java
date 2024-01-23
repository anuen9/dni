package org.anuen.medicalrecordservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.anuen.medicalrecordservice.entity.po.MedicalRecord;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface MedicalRecordMapper extends BaseMapper<MedicalRecord> {
}
