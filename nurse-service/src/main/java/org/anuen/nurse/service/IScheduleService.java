package org.anuen.nurse.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.anuen.nurse.entity.po.Schedule;
import org.anuen.nurse.entity.vo.ScheduleVo;

import java.util.List;

public interface IScheduleService extends IService<Schedule> {
    List<ScheduleVo> getAllVo();
}
