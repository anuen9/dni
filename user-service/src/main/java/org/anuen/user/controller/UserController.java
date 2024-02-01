package org.anuen.user.controller;

import lombok.RequiredArgsConstructor;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.user.entity.dto.LoginForm;
import org.anuen.user.entity.dto.UserDto;
import org.anuen.user.service.IUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final IUserService userService;

    @PostMapping("add")
    public ResponseEntity<?> add(@RequestBody UserDto userDto) {
        return userService.save(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginForm loginForm) {
        return userService.login(loginForm);
    }
}
