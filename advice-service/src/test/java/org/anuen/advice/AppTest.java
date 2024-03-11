package org.anuen.advice;

import org.anuen.advice.service.IAdviceService;
import org.anuen.common.entity.ResponseEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AppTest {

    @Autowired
    private IAdviceService adviceService;

    @Test
    public void test1() {
        ResponseEntity<?> one = adviceService.getOne(44);
        System.out.println("one = " + one);
    }
}
