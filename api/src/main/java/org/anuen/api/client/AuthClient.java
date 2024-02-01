package org.anuen.api.client;

import org.anuen.common.entity.ResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient("gateway")
public interface AuthClient {
    @GetMapping("/auth/token")
    ResponseEntity<?> token(@RequestParam("userUid") String userUid);
}
