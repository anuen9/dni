package org.anuen.api.client;

import org.anuen.common.entity.ResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("appointment-service")
public interface AppointmentClient {
    @GetMapping("/api/appointment/isAppointmentExist")
    Boolean isAppointmentExist(@RequestParam("apptId") Integer apptId);

    @PostMapping("/api/appointment/bindWithAdvice")
    ResponseEntity<?> bindWithAdvice(
            @RequestParam("apptId") Integer apptId, @RequestParam("adviceId") Integer adviceId);
}
