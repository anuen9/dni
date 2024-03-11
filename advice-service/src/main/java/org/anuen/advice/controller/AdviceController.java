package org.anuen.advice.controller;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.anuen.advice.entity.dto.AddAdviceDto;
import org.anuen.advice.service.IAdviceService;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.enums.ResponseStatus;
import org.anuen.utils.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/advice")
public class AdviceController {

    private final IAdviceService adviceService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody AddAdviceDto addAdviceDto) {
        if (!ObjectUtils.isAllFieldsNonNull(addAdviceDto)) {
            return ResponseEntity.fail(ResponseStatus.PARAM_LOSE);
        }
        return adviceService.add(addAdviceDto);
    }

    @GetMapping("/isAdviceExist")
    public Boolean isAdviceExist(@RequestParam("adviceId") Integer adviceId) {
        if (Objects.isNull(adviceId) || adviceId.equals(0)) {
            return Boolean.FALSE;
        }
        return adviceService.isAdviceExist(adviceId);
    }

    @GetMapping("/getListByPatient")
    public ResponseEntity<?> getListByPatient(@RequestParam("patientUid") String patientUid) {
        if (StrUtil.isBlank(patientUid)) {
            return ResponseEntity.fail(ResponseStatus.PARAM_LOSE);
        }
        return adviceService.getListByPatient(patientUid);
    }

    @GetMapping("/getOne")
    public ResponseEntity<?> getOne(@RequestParam("adviceId") Integer adviceId) {
        if (Objects.isNull(adviceId)) {
            return ResponseEntity.fail(ResponseStatus.PARAM_LOSE);
        }
        return adviceService.getOne(adviceId);
    }
}
