package org.anuen.appointment.controller;

import cn.hutool.core.util.StrUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.anuen.appointment.entity.dto.AddApptDto;
import org.anuen.appointment.service.IAppointmentService;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.enums.ResponseStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/appointment")
public class AppointmentController {

    private final IAppointmentService appointmentService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody AddApptDto addApptDto) {
        return appointmentService.add(addApptDto);
    }

    @GetMapping("/getListByPatient")
    public ResponseEntity<?> getListByPatient(@RequestParam("patientUid") String pUid) {
        if (StrUtil.isBlank(pUid)) {
            return ResponseEntity.fail(ResponseStatus.PARAM_LOSE);
        }
        return appointmentService.getListByPatient(pUid);
    }
}
