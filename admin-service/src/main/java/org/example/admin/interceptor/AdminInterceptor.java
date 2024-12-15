package org.example.admin.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.context.AdminContext;
import org.example.common.constant.ClaimConstant;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //1.获取登录管理员id
        String adminId = request.getHeader(ClaimConstant.CLIENT_ID);
        //2.将管理员id保存到ThreadLocal中
        try {
            AdminContext.setAdminId(Long.parseLong(adminId));
            log.info("[log] 管理员id已保存到ThreadLocal中 adminId: {}", adminId);
        } catch (NumberFormatException e) {
            log.error("[log] 管理员id应为数字类型 adminId: {}, NumberFormatException: {}", adminId, e.getMessage());
        }
        //3.放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //清理线程中的管理员id数据
        AdminContext.removeAdminId();
    }
    
}
