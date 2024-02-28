package org.anuen.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.anuen.api.client.AuthClient;
import org.anuen.common.entity.ModifyPassForm;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.entity.dto.UserDto;
import org.anuen.common.enums.RedisConst;
import org.anuen.common.enums.ResponseStatus;
import org.anuen.user.dao.UserMapper;
import org.anuen.user.entity.dto.LoginForm;
import org.anuen.user.entity.po.User;
import org.anuen.user.service.IUserService;
import org.anuen.utils.CacheClient;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final PasswordEncoder passwordEncoder;

    private final AuthClient authClient;

    private final CacheClient cacheClient;

    @Override
    public ResponseEntity<?> save(UserDto userDto) {
        User dbUser = lambdaQuery() // if user already exists -> return
                .eq(User::getUserType, userDto.getUserType())
                .eq(User::getNickName, userDto.getNickName())
                .one();
        if (Objects.nonNull(dbUser)) {
            return ResponseEntity.fail(ResponseStatus.USER_EXISTS);
        }

        User user = User.newInstance(); // save user
        BeanUtils.copyProperties(userDto, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
        return ResponseEntity.success();
    }

    @Override
    public ResponseEntity<?> login(LoginForm loginForm) {
        final String nickname = loginForm.getNickName(); // nickname or password can't be blank
        final String rawPassword = loginForm.getPassword();
        if (StrUtil.isBlank(nickname) || StrUtil.isBlank(rawPassword)) {
            return ResponseEntity.fail(ResponseStatus.LOGIN_FORM_EMPTY);
        }

        User dbUser = lambdaQuery().eq(User::getNickName, nickname).one(); // find user by nickname
        if (Objects.isNull(dbUser)) {
            return ResponseEntity.fail(ResponseStatus.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(rawPassword, dbUser.getPassword())) {
            return ResponseEntity.fail(ResponseStatus.PERMISSION_DENY);
        } // nickname and password are correct -> access

        final ResponseEntity<?> resp = authClient.token(dbUser.getUid()); // invoke service [gateway] to generate token
        if (!ResponseStatus.SUCCESS.getCode().equals(resp.getCode())) { // check response code
            final String reason = "details: " + resp.getMessage();
            return ResponseEntity.fail(ResponseStatus.REMOTE_PROCEDURE_CALL_ERROR, reason);
        }

        dbUser.setPassword("Hidden");
        cacheClient.set(RedisConst.LOGIN_USER, dbUser.getUid(), dbUser); // put user info into redis cache

        final String token = resp.getData().toString(); // get and return token
        if (StrUtil.isBlank(token)) {
            return ResponseEntity.fail(ResponseStatus.REMOTE_PROCEDURE_CALL_ERROR);
        }
        return ResponseEntity.success("Bearer " + token);
    }

    @Override
    public ResponseEntity<?> modifyPassword(ModifyPassForm modifyPassForm) {
        String currUserUid = modifyPassForm.getUserUid(); // get current user uuid
        if (StrUtil.isBlank(currUserUid)) {
            log.error("""
                    ---> method: modifyPassword().
                    --->---> error: parameter lose, user UUID dismiss!
                    """);
            return ResponseEntity.fail();
        }

        User dbUser = lambdaQuery() // check password
                .eq(User::getUid, currUserUid)
                .one();
        if (!passwordEncoder.matches(modifyPassForm.getOldPassword(), dbUser.getPassword())) {
            return ResponseEntity.fail(ResponseStatus.PERMISSION_DENY);
        } // access

        dbUser.setPassword( // change password and update database -> new pass
                passwordEncoder.encode(modifyPassForm.getNewPassword()));
        dbUser.setUpdateTime(new Date(System.currentTimeMillis())); // change time stamp of update time -> now
        updateById(dbUser);
        return ResponseEntity.success();
    }

    @Override
    public ResponseEntity<?> getUserTypeByUid(String uid) {
        User dbUser = lambdaQuery().eq(User::getUid, uid).one();
        if (Objects.isNull(dbUser)) {
            return ResponseEntity.fail(ResponseStatus.USER_NOT_FOUND);
        }
        return ResponseEntity.success(dbUser.getUserType());
    }

}
