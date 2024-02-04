package org.anuen.common.interceptors;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.anuen.common.utils.UserContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

public class UserInfoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        String userInfo = request.getHeader("user-info"); // get user from header and put it into threadLocal
        if (StrUtil.isNotBlank(userInfo)) {
            UserContextHolder.setUser(userInfo);
        } // access
        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler, Exception ex) throws Exception {
        // clear user
        UserContextHolder.removeUser();
    }
}
