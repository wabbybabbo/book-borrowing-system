package org.example.common.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.log.StaticLog;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.common.constant.UserInfoConstant;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
@Builder
public class JwtUtil {

    private String key; //密钥
    private int ttl; //token存活时间（单位：小时）

    /**
     * JWT创建
     *
     * @param claim 用户信息
     * @return token
     */
    public String createToken(Map<String, Object> claim) {
        DateTime now = DateTime.now();
        DateTime expires = now.offsetNew(DateField.HOUR, ttl);
        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                //签发时间
                put(JWTPayload.ISSUED_AT, now);
                //生效时间
                put(JWTPayload.NOT_BEFORE, now);
                //失效时间
                put(JWTPayload.EXPIRES_AT, expires);
                //内容
                putAll(claim);
            }
        };
        log.info("[log] 用户角色 {}", payload.get(UserInfoConstant.USER_ROLE));
        String token = JWTUtil.createToken(payload, key.getBytes());
        StaticLog.info("[log] 生成JWT token：{}", token);

        return token;
    }

    public String createToken() {
        DateTime now = DateTime.now();
        DateTime expires = now.offsetNew(DateField.HOUR, ttl);
        ;
        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                //签发时间
                put(JWTPayload.ISSUED_AT, now);
                //生效时间
                put(JWTPayload.NOT_BEFORE, now);
                //失效时间
                put(JWTPayload.EXPIRES_AT, expires);
            }
        };
        String token = JWTUtil.createToken(payload, key.getBytes());
        StaticLog.info("[log] 生成JWT token：{}", token);

        return token;
    }

    /**
     * JWT解析
     *
     * @param token 加密字符串JWT（JSON Web Token）
     * @return 用户信息
     */
    public Map<String, String> parseToken(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        Map<String, String> userInfo = new HashMap<String, String>();
        userInfo.put(UserInfoConstant.USER_ID, jwt.getPayload(UserInfoConstant.USER_ID).toString());
        userInfo.put(UserInfoConstant.USER_ROLE, jwt.getPayload(UserInfoConstant.USER_ROLE).toString());
        return userInfo;
    }

    /**
     * 验证 JWT token的数据合法性和过期时间
     *
     * @param token 加密字符串
     * @return token是否有效
     */
    public boolean validate(String token) {
        //校验jwt token的数据合法性，也校验了token的过期时间
        return JWTUtil.parseToken(token)
                .setKey(key.getBytes())
                .validate(0);
    }
}
