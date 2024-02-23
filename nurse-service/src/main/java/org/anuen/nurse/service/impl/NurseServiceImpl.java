package org.anuen.nurse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.anuen.nurse.dao.NurseMapper;
import org.anuen.nurse.entity.po.Nurse;
import org.anuen.nurse.service.INurseService;
import org.springframework.stereotype.Service;

@Service
public class NurseServiceImpl
        extends ServiceImpl<NurseMapper, Nurse> implements INurseService {
}
