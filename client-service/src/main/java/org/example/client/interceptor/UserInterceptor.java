package org.example.client.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.client.context.UserContext;
import org.example.common.constant.UserInfoConstant;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取登录用户id
        String userId = request.getHeader(UserInfoConstant.USER_ID);
        //2.将用户id存入ThreadLocal
        log.info("[log] 正在将用户id(id={})存入ThreadLocal", userId);
        if (userId != null && !userId.isEmpty())
            UserContext.setUserId(Integer.parseInt(userId));
        //3.放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清理线程中的用户id数据
        UserContext.removeUserId();
    }
}
