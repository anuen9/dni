package org.anuen.api.client;

import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.entity.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("user-service")
public interface UserClient {
    @PostMapping("/api/user/add")
    ResponseEntity<?> add(@RequestBody UserDto userDto);

    @GetMapping("/api/user/getUserTypeByUid")
    ResponseEntity<?> getUserTypeByUid(@RequestParam("uid") String uid);

    @GetMapping("/api/user/getNamesByUidList")
    ResponseEntity<?> getNamesByUidList(@RequestParam("uidList") List<String> uidList);
}
