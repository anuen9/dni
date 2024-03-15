package org.anuen.nurse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.anuen.nurse.dao.ScheduleMapper;
import org.anuen.nurse.entity.po.Schedule;
import org.anuen.nurse.service.IScheduleService;
import org.springframework.stereotype.Service;

@Service
public class ScheduleServiceImpl
        extends ServiceImpl<ScheduleMapper, Schedule> implements IScheduleService {

}
