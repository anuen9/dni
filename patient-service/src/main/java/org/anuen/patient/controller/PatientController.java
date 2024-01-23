package org.anuen.patient.controller;

import lombok.RequiredArgsConstructor;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.patient.entity.dto.PatientDto;
import org.anuen.patient.service.IPatientService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/patient")
public class PatientController {

    private final IPatientService patientService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody PatientDto patientDto) {
        return patientService.save(patientDto);
    }
}
