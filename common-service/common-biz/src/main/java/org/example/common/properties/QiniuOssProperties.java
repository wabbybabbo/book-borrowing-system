package org.example.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 七牛云对象存储的配置属性类
 */
@Data
@Component
@ConfigurationProperties(prefix = "oss.qiniu")
public class QiniuOssProperties {

    private String accessKey;
    private String secretKey;
    private String bucket;
    private String cdn;

}
