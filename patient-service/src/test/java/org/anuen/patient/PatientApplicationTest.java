package org.anuen.patient;

import org.anuen.patient.config.DefaultInfoProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PatientApplicationTest {

    @Autowired
    private DefaultInfoProperties defaultInfoProperties;

    @Test
    public void testYaml() {
        System.out.println(defaultInfoProperties.getPassword());
        System.out.println(defaultInfoProperties.getUserType());
    }
}
