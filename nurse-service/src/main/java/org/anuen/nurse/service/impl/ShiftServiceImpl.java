package org.anuen.nurse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.anuen.nurse.dao.ShiftMapper;
import org.anuen.nurse.entity.po.Shift;
import org.anuen.nurse.service.IShiftService;
import org.springframework.stereotype.Service;

@Service
public class ShiftServiceImpl
        extends ServiceImpl<ShiftMapper, Shift> implements IShiftService {


}
