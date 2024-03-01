package org.anuen.nurse.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.anuen.nurse.entity.dto.NameSuggestion;
import org.anuen.nurse.entity.po.Nurse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NurseMapper extends BaseMapper<Nurse> {

    List<NameSuggestion> selectNamesByInput(@Param("name") String name);
}
