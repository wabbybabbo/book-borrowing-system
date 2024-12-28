package org.example.common.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.common.constant.ClaimConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 */
@Slf4j
@Data
@Builder
public class JwtUtil {

    private String key; //密钥
    private int ttl; //token存活时间（单位：小时）

    /**
     * 生成 JWT(JSON Web Token)
     *
     * @param claim 载荷
     * @return JWT(JSON Web Token)
     */
    public String createToken(Map<String, Object> claim) {
        DateTime now = DateTime.now();
        DateTime expires = now.offsetNew(DateField.HOUR, ttl);
        Map<String, Object> payload = new HashMap<>() {
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
        String token = JWTUtil.createToken(payload, key.getBytes());
        log.info("[log] 生成JWT token: {}", token);

        return token;
    }

    /**
     * 生成 JWT(JSON Web Token)
     *
     * @return JWT(JSON Web Token)
     */
    public String createToken() {
        DateTime now = DateTime.now();
        DateTime expires = now.offsetNew(DateField.HOUR, ttl);

        Map<String, Object> payload = new HashMap<>() {
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
        log.info("[log] 生成JWT token: {}", token);

        return token;
    }

    /**
     * 解析 JWT
     *
     * @param token 加密字符串 JWT
     * @return 载荷
     */
    public Map<String, String> parseToken(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        Map<String, String> claim = new HashMap<>();
        claim.put(ClaimConstant.CLIENT_ID, jwt.getPayload(ClaimConstant.CLIENT_ID).toString());
        return claim;
    }

    /**
     * 验证 JWT 的数据合法性和过期时间
     *
     * @param token 加密字符串 JWT
     * @return token是否有效
     */
    public boolean validate(String token) {
        //校验JWT的数据合法性，也校验了JWT的过期时间
        return JWTUtil.parseToken(token)
                .setKey(key.getBytes())
                .validate(0);
    }
}
