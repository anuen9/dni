package org.anuen.patient.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.anuen.patient.entity.dto.NameSuggestion;
import org.anuen.patient.entity.po.Patient;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PatientMapper extends BaseMapper<Patient> {
    List<NameSuggestion> selectNamesByInput(@Param("input") String input);
}
