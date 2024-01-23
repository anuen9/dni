package org.anuen.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.enums.ResponseStatus;
import org.anuen.user.dao.UserMapper;
import org.anuen.user.entity.dto.UserDto;
import org.anuen.user.entity.po.User;
import org.anuen.user.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public ResponseEntity<?> save(UserDto userDto) {
        // if user already exists do nothing
        User dbUser = getOne(
                new LambdaQueryWrapper<User>()
                        .select(User::getId)
                        .eq(User::getUserType, userDto.getUserType())
                        .eq(User::getNickName, userDto.getNickName())
                        .last("limit 1"));
        if (Objects.nonNull(dbUser)) {
            return ResponseEntity.fail(ResponseStatus.USER_EXISTS);
        }
        // save user
        User user = User.newInstance();
        BeanUtils.copyProperties(userDto, user);
        save(user);
        return ResponseEntity.success();
    }
}
