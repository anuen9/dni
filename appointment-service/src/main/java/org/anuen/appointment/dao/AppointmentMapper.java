package org.anuen.appointment.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.anuen.appointment.entity.po.Appointment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppointmentMapper extends BaseMapper<Appointment> {
}
