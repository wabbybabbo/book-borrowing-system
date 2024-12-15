package org.example.client.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.client.context.UserContext;
import org.example.common.constant.ClaimConstant;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //1.获取登录用户id
        String userId = request.getHeader(ClaimConstant.CLIENT_ID);
        //2.将用户id保存到ThreadLocal中
        try {
            UserContext.setUserId(Long.parseLong(userId));
            log.info("[log] 用户id已保存到ThreadLocal中 userId: {}", userId);
        } catch (NumberFormatException e) {
            log.error("[log] 用户id应为数字类型 userId: {}, NumberFormatException: {}", userId, e.getMessage());
        }
        //3.放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //清理线程中的用户id数据
        UserContext.removeUserId();
    }

}
