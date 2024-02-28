package org.anuen.appointment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.anuen.api.client.UserClient;
import org.anuen.appointment.dao.AppointmentMapper;
import org.anuen.appointment.entity.dto.AddApptDto;
import org.anuen.appointment.entity.po.Appointment;
import org.anuen.appointment.service.IAppointmentService;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.enums.ResponseStatus;
import org.anuen.common.utils.UserContextHolder;
import org.anuen.utils.RPCRespResolver;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl
        extends ServiceImpl<AppointmentMapper, Appointment> implements IAppointmentService {

    private final UserClient userClient;

    private final RPCRespResolver respResolver;

    @Override
    public ResponseEntity<?> add(AddApptDto addApptDto) {
        String currUserUid = UserContextHolder.getUser(); // check current user are doctor?
        ResponseEntity<?> resp = userClient.getUserTypeByUid(currUserUid);
        Integer userType = respResolver.getRespData(resp, Integer.class);
        if (Objects.isNull(userType)) {
            return ResponseEntity.fail(ResponseStatus.REMOTE_PROCEDURE_CALL_ERROR);
        }
        if (!userType.equals(2)) { // if current user are not doctor -> return
            return ResponseEntity.fail(ResponseStatus.UNAUTHORIZED);
        }
        Appointment appointment = Appointment.newInstance(); // insert appt into database
        BeanUtils.copyProperties(addApptDto, appointment);
        Date now = new Date(System.currentTimeMillis());
        appointment.setDoctorUid(currUserUid).setAppointmentDate(now);
        this.baseMapper.insert(appointment);
        return ResponseEntity.success();
    }

}
