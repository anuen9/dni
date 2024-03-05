package org.anuen.appointment;

import org.anuen.appointment.service.IAllergiesService;
import org.anuen.appointment.service.IAppointmentService;
import org.anuen.common.entity.ResponseEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AppTest {

    @Autowired
    IAllergiesService allergiesService;

    @Autowired
    IAppointmentService appointmentService;

    @Test
    public void test() {
        allergiesService.getFormatList();
    }

    @Test
    public void test1() {
        ResponseEntity<?> detailsByApptId = appointmentService.getDetailsByApptId(4);
        System.out.println("detailsByApptId = " + detailsByApptId);
    }
}
