package org.anuen.patient.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anuen.common.entity.ModifyPassForm;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.enums.ResponseStatus;
import org.anuen.common.exception.FormatException;
import org.anuen.common.exception.UnauthorizedException;
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

    @PostMapping("/modifyPass")
    public ResponseEntity<?> modifyPass(@Valid @RequestBody ModifyPassForm modifyPassForm) {
        try {
            return patientService.modifyPass(modifyPassForm);
        } catch (UnauthorizedException ue) {
            return ResponseEntity.fail(ResponseStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmail(@RequestParam String email) {
        try {
            return patientService.verifyEmail(email);
        } catch (FormatException fe) {
            return ResponseEntity.fail(ResponseStatus.IS_NOT_EMAIL_ADDRESS);
        }
    }

    @GetMapping("/fetchSuggestionsByName")
    public ResponseEntity<?> fetchSuggestionsByName(@RequestParam("name") String name) {
        return patientService.fetchSuggestionsByName(name);
    }

    @GetMapping("/getOne")
    public ResponseEntity<?> getOne(@RequestParam("uid") String uid) {
        return patientService.findOne(uid);
    }
}
