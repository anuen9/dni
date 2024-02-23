package org.anuen.record.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.anuen.record.dao.NursingRecordMapper;
import org.anuen.record.entity.po.NursingRecord;
import org.anuen.record.service.INursingRecordService;
import org.springframework.stereotype.Service;

@Service
public class NursingRecordServiceImpl
        extends ServiceImpl<NursingRecordMapper, NursingRecord> implements INursingRecordService {
}
