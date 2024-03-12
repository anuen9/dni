package org.anuen.advice.service.impl;

import org.anuen.advice.service.IAdviceTransService;
import org.springframework.stereotype.Service;

import java.util.function.BiFunction;

@Service
public class AdviceTransServiceImpl implements IAdviceTransService {

    @Override
    public <T, U, R> void callBack(BiFunction<T, U, R> function, T var1, U var2) {
        function.apply(var1, var2);
    }
}
