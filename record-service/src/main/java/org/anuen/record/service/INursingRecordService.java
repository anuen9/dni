package org.anuen.record.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.record.entity.dto.FinishNDto;
import org.anuen.record.entity.dto.NewRecordForm;
import org.anuen.record.entity.po.NursingRecord;

public interface INursingRecordService extends IService<NursingRecord> {
    ResponseEntity<?> startNursing(NewRecordForm newRecordForm);

    ResponseEntity<?> finishNursing(FinishNDto fN);

    ResponseEntity<?> listByPatient(String pUid);
}
