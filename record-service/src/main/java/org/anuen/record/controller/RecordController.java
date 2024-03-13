package org.anuen.record.controller;

import lombok.RequiredArgsConstructor;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.record.entity.dto.NewRecordForm;
import org.anuen.record.service.INursingRecordService;
import org.anuen.utils.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
