package org.anuen.nurse.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.nurse.entity.dto.AddNurseDto;
import org.anuen.nurse.service.INurseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/nurse")
@RequiredArgsConstructor
public class NurseController {

    private final INurseService nurseService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody AddNurseDto nurseDto) {
        return nurseService.add(nurseDto);
    }
}
