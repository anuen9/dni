package org.anuen.advice.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.anuen.advice.entity.po.Advice;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdviceMapper extends BaseMapper<Advice> {

}
