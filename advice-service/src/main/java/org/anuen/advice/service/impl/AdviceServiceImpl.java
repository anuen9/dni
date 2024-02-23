package org.anuen.advice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.anuen.advice.dao.AdviceMapper;
import org.anuen.advice.entity.po.Advice;
import org.anuen.advice.service.IAdviceService;
import org.springframework.stereotype.Service;

@Service
public class AdviceServiceImpl
        extends ServiceImpl<AdviceMapper, Advice> implements IAdviceService {
}
