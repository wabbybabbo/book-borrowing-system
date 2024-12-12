package org.example.gateway;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import org.example.common.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class JwtTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void testValidateJwt() {
        String key = "123";
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYmYiOjE3MTI1NDMwMDMsImV4cCI6MTcxMjU0NjYwMywiaWF0IjoxNzEyNTQzMDAzfQ.eoyMVQxHgWLi2AD_YZgy2Iaj_Nl2chjvj_2Cw9wF10g";
        JWT jwt = JWTUtil.parseToken(token);
        boolean verify = JWTUtil.verify(token, key.getBytes());
        System.out.println("[sout] verify: " + verify);

        boolean verifyKey = jwt.setKey(key.getBytes()).verify();
        System.out.println("[sout] verifyKey: " + verifyKey);

        boolean verifyTime = jwt.setKey(key.getBytes()).validate(0);
        System.out.println("[sout] verifyTime: " + verifyTime);


        System.out.println("[sout] validate: " + jwtUtil.validate(token));
    }

    @Test
    public void testValidateJwt1() {
        DateTime now = DateTime.now();
        DateTime newTime = now.offsetNew(DateField.MINUTE, 10);

        Map<String, Object> payload = new HashMap<>();
        //签发时间
        payload.put(JWTPayload.ISSUED_AT, now);
        //过期时间
        payload.put(JWTPayload.EXPIRES_AT, newTime);
        //生效时间
        payload.put(JWTPayload.NOT_BEFORE, now);
        //载荷
        payload.put("userName", "zhangsan");
        payload.put("passWord", "666889");

        String key = "aabb";
        String token = JWTUtil.createToken(payload, key.getBytes());
        System.out.println("[sout] token: " + token);


        JWT jwt = JWTUtil.parseToken(token);

        boolean verifyKey = jwt.setKey(key.getBytes()).verify();
        System.out.println("[sout] verifyKey: " + verifyKey);

        boolean verifyTime = jwt.validate(0);
        System.out.println("[sout] verifyTime: " + verifyTime);
    }

}
