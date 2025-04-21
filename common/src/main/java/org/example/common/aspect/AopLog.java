package org.example.common.aspect;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class AopLog {

    //定义切点
    @Pointcut(value = "execution(* org.example.*.controller.*.*(..))") //匹配controller包中所有类的所有方法
    public void controllerMethods() {
    }

    // 使用Around环绕通知
    @Around("controllerMethods()")
    public Object logAroundControllerMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取类名
        String className = joinPoint.getTarget().getClass().getSimpleName();
        // 获取方法名
        String methodName = joinPoint.getSignature().getName();
        // 获取Operation注解信息
        Operation operation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Operation.class);

        // 方法执行前日志
        log.info("===> 开始执行 {}.{}() {}", className, methodName, operation != null ? operation.summary() : "");
        log.debug("参数: {}", joinPoint.getArgs());

        Object result = null;
        try {
            // 执行目标方法
            result = joinPoint.proceed();

            // 方法执行后日志
            log.info("<=== 执行完成 {}.{}()", className, methodName);
            // log.debug("返回值: {}", result);

            return result;
        } catch (Throwable exception) {
            // 异常处理日志
            log.error("<=== 执行异常 {}.{}()", className, methodName);
            log.error("异常信息: {}", exception.getMessage(), exception);
            throw exception;
        }
    }

//    // 方法执行前打印日志
//    @Before("controllerMethods()")
//    public void logBeforeControllerMethod(JoinPoint joinPoint) {
//        // 获取类名
//        String className = joinPoint.getTarget().getClass().getSimpleName();
//        // 获取方法名
//        String methodName = joinPoint.getSignature().getName();
//        // 获取Operation注解信息
//        Operation operation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Operation.class);
//        log.info("===> 开始执行 {}.{}() {}", className, methodName, operation.summary());
//        log.debug("参数: {}", joinPoint.getArgs());
//    }
//
//    // 方法执行后打印日志
//    @AfterReturning(pointcut = "controllerMethods()", returning = "result") //returning 将目标方法的返回值绑定到增强方法的同名参数上
//    public void logAfterControllerMethod(JoinPoint joinPoint, Object result) {
//        String className = joinPoint.getTarget().getClass().getSimpleName();
//        String methodName = joinPoint.getSignature().getName();
//        log.info("<=== 执行完成 {}.{}()", className, methodName);
//        log.debug("返回值: {}", result);
//    }
//
//    // 方法抛出异常时打印日志
//    @AfterThrowing(pointcut = "controllerMethods()", throwing = "exception") //throwing 将目标方法的异常值绑定到增强方法的同名参数上
//    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
//        String className = joinPoint.getTarget().getClass().getSimpleName();
//        String methodName = joinPoint.getSignature().getName();
//        log.error("<=== 执行异常 {}.{}()", className, methodName);
//        log.error("异常信息: {}", exception.getMessage(), exception);
//    }

}
