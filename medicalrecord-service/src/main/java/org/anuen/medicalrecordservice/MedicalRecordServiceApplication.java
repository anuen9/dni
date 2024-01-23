package org.anuen.medicalrecordservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("org.anuen.medicalrecordservice.dao")
public class MedicalRecordServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicalRecordServiceApplication.class, args);
    }

}
