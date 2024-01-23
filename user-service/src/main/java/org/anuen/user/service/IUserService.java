package org.anuen.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.user.entity.dto.UserDto;
import org.anuen.user.entity.po.User;



public interface IUserService extends IService<User> {
    ResponseEntity<?> save(UserDto userDto);
}
