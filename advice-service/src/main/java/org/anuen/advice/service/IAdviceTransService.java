package org.anuen.advice.service;


import java.util.function.BiFunction;

public interface IAdviceTransService {

    <T, U, R> void callBack(BiFunction<T, U, R> function, T t, U u);

}
