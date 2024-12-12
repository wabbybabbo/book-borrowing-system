package org.example.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GatewayApplicationTest {

    @Test
    public void test() {
        String str = null;
        System.out.println("[sout] isBlank: " + str.isBlank());
        System.out.println("[sout] isEmpty: " + str.isEmpty());
    }

}
