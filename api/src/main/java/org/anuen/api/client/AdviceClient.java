package org.anuen.api.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("advice-service")
public interface AdviceClient {
    @GetMapping("/api/advice/isAdviceExist")
    Boolean isAdviceExist(@RequestParam("adviceId") Integer adviceId);

    @GetMapping("/api/advice/fetchOneOfJson")
    String fetchOneOfJson(@RequestParam("adviceId") Integer adviceId);

    @PostMapping("/api/advice/finishAdvice")
    Boolean finishAdvice(@RequestParam("adviceId") Integer aId);
}
