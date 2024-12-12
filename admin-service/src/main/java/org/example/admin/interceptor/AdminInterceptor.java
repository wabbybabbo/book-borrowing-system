package org.example.admin.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.common.constant.UserInfoConstant;
import org.example.common.constant.UserRoleConstant;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getRequestURI().equals("/user/login")) {
            return true; //登录时直接放行
        }
        // 1.获取登录用户角色
        String userRole = request.getHeader(UserInfoConstant.USER_ROLE);
        log.info("[log] 用户角色 {}", userRole);
        // 2.验证登录用户角色是否为 'admin'，是则放行
        return userRole.equals(UserRoleConstant.ADMIN);
    }
}
