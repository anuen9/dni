package org.anuen.advice.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.anuen.advice.entity.dto.AddAdviceDto;
import org.anuen.advice.entity.po.Advice;
import org.anuen.common.entity.ResponseEntity;

public interface IAdviceService extends IService<Advice> {
    ResponseEntity<?> add(AddAdviceDto addAdviceDto);

    Boolean isAdviceExist(Integer adviceId);

    ResponseEntity<?> getListByPatient(String patientUid);

    ResponseEntity<?> getOne(Integer adviceId);

    ResponseEntity<?> bidirectionalBind(Integer adviceId, Integer apptId);

    ResponseEntity<?> getNotBoundListByPatient(String pUid);
}
