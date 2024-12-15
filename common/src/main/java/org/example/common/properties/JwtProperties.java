package org.example.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String key; //密钥
    private int ttl; //token存活时间（单位：小时）

}
