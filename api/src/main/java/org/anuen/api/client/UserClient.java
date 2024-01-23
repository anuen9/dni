package org.anuen.api.client;

import org.anuen.api.dto.UserDto;
import org.anuen.common.entity.ResponseEntity;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("user-service")
public interface UserClient {
    @PostMapping("/api/user/add")
    ResponseEntity<?> add(@RequestBody UserDto userDto);
}
