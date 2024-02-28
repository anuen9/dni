package org.anuen.appointment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.anuen.appointment.entity.dto.AddApptDto;
import org.anuen.appointment.service.IAppointmentService;
import org.anuen.common.entity.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/appointment")
public class AppointmentController {

    private final IAppointmentService appointmentService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody AddApptDto addApptDto) {
        return appointmentService.add(addApptDto);
    }
}
