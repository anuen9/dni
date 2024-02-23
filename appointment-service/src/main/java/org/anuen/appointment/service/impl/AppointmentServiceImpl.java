package org.anuen.appointment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.anuen.appointment.dao.AppointmentMapper;
import org.anuen.appointment.entity.po.Appointment;
import org.anuen.appointment.service.IAppointmentService;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl
        extends ServiceImpl<AppointmentMapper, Appointment> implements IAppointmentService {
}
