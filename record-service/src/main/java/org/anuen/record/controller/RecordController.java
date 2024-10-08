package org.anuen.record.controller;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.record.entity.dto.FinishNDto;
import org.anuen.record.entity.dto.NewRecordForm;
import org.anuen.record.service.INursingRecordService;
import org.anuen.utils.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import static org.anuen.common.enums.ResponseStatus.PARAM_LOSE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/record")
public class RecordController {

    private final INursingRecordService recordService;

    @PostMapping("/startNursing")
    public ResponseEntity<?> startNursing(@RequestBody NewRecordForm newRecordForm) {
        if (!ObjectUtils.isAllFieldsNonNull(newRecordForm)) {
            return ResponseEntity.fail(PARAM_LOSE);
        }
        return recordService.startNursing(newRecordForm);
    }

    @PostMapping("/finishNursing")
    public ResponseEntity<?> finishNursing(@RequestBody FinishNDto fN) {
        if (!ObjectUtils.isAllFieldsNonNull(fN)) {
            return ResponseEntity.fail(PARAM_LOSE);
        }
        return recordService.finishNursing(fN);
    }

    @GetMapping("/listByPatient")
    public ResponseEntity<?> listByPatient(@RequestParam("patientUid") String pUid) {
        if (StrUtil.isBlank(pUid)) {
            return ResponseEntity.fail(PARAM_LOSE);
        }
        return recordService.listByPatient(pUid);
    }
}
