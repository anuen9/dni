package org.anuen.gateway.controller;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.enums.ResponseStatus;
import org.anuen.gateway.utils.JwtTool;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final JwtTool jwtTool;

    @GetMapping("/token")
    public ResponseEntity<?> token(@RequestParam("userUid") String userUid) {
        if (StrUtil.isBlank(userUid)) {
            return ResponseEntity.fail(ResponseStatus.PARAM_LOSE);
        }
        String token = jwtTool.generateToken(userUid);
        return ResponseEntity.success(token);
    }
}
