package org.example.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "oss.qiniu")
public class QiniuOssProperties {

    private String accessKey;
    private String secretKey;
    private String bucket;
    private String cdn;

}
