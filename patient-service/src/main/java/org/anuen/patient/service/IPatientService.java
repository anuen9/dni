package org.anuen.patient.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.patient.entity.dto.PatientDto;
import org.anuen.patient.entity.po.Patient;

public interface IPatientService extends IService<Patient> {
    ResponseEntity<?> save(PatientDto patientDto);

}
