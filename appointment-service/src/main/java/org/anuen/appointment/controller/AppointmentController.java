package org.anuen.appointment.controller;

import cn.hutool.core.util.StrUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anuen.appointment.entity.dto.AddApptDto;
import org.anuen.appointment.entity.dto.ModifyApptDto;
import org.anuen.appointment.service.IAllergiesService;
import org.anuen.appointment.service.IAppointmentService;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.enums.ResponseStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/appointment")
public class AppointmentController {

    private final IAppointmentService appointmentService;

    private final IAllergiesService allergiesService;

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

    @GetMapping("/getDetailsByApptId")
    public ResponseEntity<?> getDetailsByApptId(@RequestParam("appointmentId") Integer apptId) {
        if (Objects.isNull(apptId)) {
            return ResponseEntity.fail(ResponseStatus.PARAM_LOSE);
        }
        return appointmentService.getDetailsByApptId(apptId);
    }

    @GetMapping("/getAllergenList")
    public ResponseEntity<?> getAllergenList() {
        try {
            return allergiesService.getFormatList();
        } catch (Exception e) {
            log.error("---> get allergies list error, please check database");
            return ResponseEntity.fail(ResponseStatus.DATABASE_ERROR);
        }
    }

    @PostMapping("/modify")
    public ResponseEntity<?> modify(@RequestBody ModifyApptDto modifyApptDto) {
        return appointmentService.modify(modifyApptDto);
    }
}
