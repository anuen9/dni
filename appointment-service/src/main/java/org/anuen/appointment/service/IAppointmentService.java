package org.anuen.appointment.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.anuen.appointment.entity.dto.AddApptDto;
import org.anuen.appointment.entity.po.Appointment;
import org.anuen.common.entity.ResponseEntity;

public interface IAppointmentService extends IService<Appointment> {
    ResponseEntity<?> add(AddApptDto addApptDto);

    ResponseEntity<?> getListByPatient(String pUid);

    ResponseEntity<?> getDetailsByApptId(Integer apptId);
}
