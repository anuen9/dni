package org.anuen.nurse.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.anuen.nurse.entity.po.Nurse;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NurseMapper extends BaseMapper<Nurse> {
}
