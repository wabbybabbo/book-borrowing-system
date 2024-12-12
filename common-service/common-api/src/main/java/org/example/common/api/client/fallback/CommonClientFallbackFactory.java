package org.example.common.api.client.fallback;

import lombok.extern.slf4j.Slf4j;
import org.example.common.api.client.CommonClient;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 当调用远程服务失败时的回退策略
 */
@Slf4j
public class CommonClientFallbackFactory implements FallbackFactory<CommonClient> {

    public static final String SERVER_ACCESS_OVERLOAD = "当前服务器访问人数过多";

    @Override
    public CommonClient create(Throwable cause) {
        log.info("[log] 远程调用异常原因：{}", cause.toString());

        //当熔断时，以下方法定义了程序的回退逻辑(备用流程)。
        return new CommonClient() {
            @Override
            public String echo(String string) {
                throw new RuntimeException(SERVER_ACCESS_OVERLOAD);
            }

            @Override
            public String createToken(Map<String, Object> claim) {
                throw new RuntimeException(SERVER_ACCESS_OVERLOAD);
            }

            @Override
            public String upload(MultipartFile file) {
                throw new RuntimeException(SERVER_ACCESS_OVERLOAD);
            }
        };
    }
}
