package org.example.gateway.filter;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.constant.UserInfoConstant;
import org.example.common.exception.NullHttpHeaderException;
import org.example.common.util.JwtUtil;
import org.example.gateway.properties.AuthProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.example.common.constant.MessageConstant.NULL_HTTP_HEADER;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final AuthProperties authProperties;
    private final JwtUtil jwtUtil;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 获取请求头Authorization中的token
     *
     * @param request 请求
     * @return token
     */
    private String getToken(ServerHttpRequest request) {
        List<String> header = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            throw new NullHttpHeaderException(HttpHeaders.AUTHORIZATION + NULL_HTTP_HEADER);
        }
        return header.get(0);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求
        ServerHttpRequest request = exchange.getRequest();
        //判断是否需要做拦截
        String requestPath = request.getPath().toString();
        log.info("[log] requestPath: {}", requestPath);
        for (String pathPattern : authProperties.getExcludePaths()) {
            if (antPathMatcher.match(pathPattern, requestPath)) {
                return chain.filter(exchange); //放行
            }
        }
        //获取token
        String token;
        try {
            token = getToken(request);
        } catch (NullHttpHeaderException e) {
            log.error("[log] NullHttpHeaderException: {}", e.getMessage());
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED); //设置响应状态码401 表示未授权
            return response.setComplete(); //终止
        }
        //校验token
        if (!jwtUtil.validate(token)) {
            log.error("[log] token校验不通过");
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED); //设置响应状态码401 表示未授权
            return response.setComplete(); //终止
        }
        //解析token，获取用户信息
        Map<String, String> userInfo = jwtUtil.parseToken(token);
//        log.info("[log] 用户角色 {}", userInfo.get(UserInfoConstant.USER_ROLE));
        //传递用户信息
        ServerWebExchange swe = exchange.mutate() //mutate 对下游请求做更改
                .request(builder -> builder.header(UserInfoConstant.USER_ID, userInfo.get(UserInfoConstant.USER_ID))) //设置一个请求头
                .request(builder -> builder.header(UserInfoConstant.USER_ROLE, userInfo.get(UserInfoConstant.USER_ROLE)))
                .build();
        return chain.filter(swe); //放行
    }

    @Override
    public int getOrder() {
        //过滤器执行顺序，值越小，优先级越高
        return 0;
    }
}
