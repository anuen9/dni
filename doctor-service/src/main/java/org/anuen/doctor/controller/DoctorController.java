package org.anuen.doctor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.doctor.entity.dto.AddDoctorDto;
import org.anuen.doctor.service.IDoctorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final IDoctorService doctorService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody AddDoctorDto addDoctorDto) {
        return doctorService.add(addDoctorDto);
    }
}
