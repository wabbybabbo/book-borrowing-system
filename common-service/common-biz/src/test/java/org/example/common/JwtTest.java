package org.example.common;

import org.example.common.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void testCreateJwt() {
        String token = jwtUtil.createToken();
        System.out.println("[sout] " + token);
    }

}
