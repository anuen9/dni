package org.anuen.nurse.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.anuen.nurse.entity.po.Schedule;
import org.anuen.nurse.entity.vo.ScheduleVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScheduleMapper extends BaseMapper<Schedule> {
    List<ScheduleVo> selectAllVo();
}
