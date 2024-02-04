package org.anuen.patient.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.utils.UserContextHolder;
import org.anuen.patient.entity.dto.PatientDto;
import org.anuen.patient.service.IPatientService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/patient")
public class PatientController {

    private final IPatientService patientService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody PatientDto patientDto) {
        return patientService.save(patientDto);
    }

    @GetMapping("/getOne")
    public ResponseEntity<?> getOne(@RequestParam("userId") Integer userId) {
        log.warn("------> {}", UserContextHolder.getUser());
        return patientService.findOne(userId);
    }
}
