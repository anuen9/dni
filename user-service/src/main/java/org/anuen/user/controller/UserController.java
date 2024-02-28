package org.anuen.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.entity.dto.UserDto;
import org.anuen.common.enums.ResponseStatus;
import org.anuen.common.exception.UnauthorizedException;
import org.anuen.user.entity.dto.LoginForm;
import org.anuen.common.entity.ModifyPassForm;
import org.anuen.user.service.IUserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final IUserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody UserDto userDto) {
        return userService.save(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginForm loginForm) {
        return userService.login(loginForm);
    }

    @PostMapping("/modifyPass")
    public ResponseEntity<?> modifyPassword(@Valid @RequestBody ModifyPassForm modifyPassForm) {
        try {
            return userService.modifyPassword(modifyPassForm);
        } catch (UnauthorizedException ue) {
            return ResponseEntity.fail(ResponseStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/getUserTypeByUid")
    public ResponseEntity<?> getUserTypeByUid(@RequestParam("uid") String uid) {
        return userService.getUserTypeByUid(uid);
    }
}
