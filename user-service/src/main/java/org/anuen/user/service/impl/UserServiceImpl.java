package org.anuen.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.enums.ResponseStatus;
import org.anuen.user.dao.UserMapper;
import org.anuen.user.entity.dto.UserDto;
import org.anuen.user.entity.po.User;
import org.anuen.user.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> save(UserDto userDto) {
        User dbUser = getOne( // if user already exists do nothing
                new LambdaQueryWrapper<User>()
                        .select(User::getId)
                        .eq(User::getUserType, userDto.getUserType())
                        .eq(User::getNickName, userDto.getNickName())
                        .last("limit 1"));
        if (Objects.nonNull(dbUser)) {
            return ResponseEntity.fail(ResponseStatus.USER_EXISTS);
        }

        User user = User.newInstance(); // save user
        BeanUtils.copyProperties(userDto, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
        return ResponseEntity.success();
    }
}
