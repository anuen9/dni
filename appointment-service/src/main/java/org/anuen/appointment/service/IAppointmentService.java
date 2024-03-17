package org.anuen.appointment.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.anuen.appointment.entity.dto.AddApptDto;
import org.anuen.appointment.entity.dto.ModifyApptDto;
import org.anuen.appointment.entity.dto.UpdateStatusDto;
import org.anuen.appointment.entity.po.Appointment;
import org.anuen.common.entity.ResponseEntity;

public interface IAppointmentService extends IService<Appointment> {
    ResponseEntity<?> add(AddApptDto addApptDto);

    ResponseEntity<?> getListByPatient(String pUid);

    ResponseEntity<?> getDetailsByApptId(Integer apptId);

    ResponseEntity<?> modify(ModifyApptDto modifyApptDto);

    Boolean isAppointmentExist(Integer apptId);

    ResponseEntity<?> bindWithAdvice(Integer apptId, Integer adviceId);

    ResponseEntity<?> getNotBoundListByPatient(String pUid);

    ResponseEntity<?> listNeedAdmit();

    ResponseEntity<?> updateStatus(UpdateStatusDto us);

    ResponseEntity<?> listCanDischarge();
}
