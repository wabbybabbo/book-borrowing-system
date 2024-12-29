package org.example.admin.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * SpringCache配置
 */
@Slf4j
@EnableCaching //开启缓存注解功能
//启用CacheProperties的配置属性绑定，这允许从配置文件（如application.properties或application.yml）中读取Redis缓存相关的配置。
@EnableConfigurationProperties(CacheProperties.class)
@Configuration
public class RedisCacheConfig {

    /**
     * 配置Redis缓存管理器
     *
     * @param redisConnectionFactory {@link RedisConnectionFactory}
     * @param cacheProperties        {@link CacheProperties}
     * @return {@link RedisCacheManager}
     */
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory, CacheProperties cacheProperties) {
        // 针对不同cacheName，使用不同的配置
        /*Map<String, RedisCacheConfiguration> initialCacheConfigurations = new HashMap<>() {{
            put("captchaCache", RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofMinutes(1))//1分钟
                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))//修改Redis值序列化方式
                    .computePrefixWith(cacheName -> cacheName + ":"));
            // ...
        }};*/

        // 自定义 RedisCacheConfiguration
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig();
        //cacheConfig = cacheConfig.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()));//修改Redis值序列化方式，默认StringSerializationRedisSerializer
        //cacheConfig = cacheConfig.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));//修改Redis值序列化方式，默认JdkSerializationRedisSerializer
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        if (redisProperties.getTimeToLive() != null) {
            log.info("[log] 设置缓存的过期时间：{}s", redisProperties.getTimeToLive().getSeconds());
            cacheConfig = cacheConfig.entryTtl(redisProperties.getTimeToLive());//设置缓存的过期时间
        }
        if (!redisProperties.isCacheNullValues()) {
            log.info("[log] 取消设置缓存空值");
            cacheConfig = cacheConfig.disableCachingNullValues();//取消设置缓存空值
        }
        if (!redisProperties.isUseKeyPrefix()) {
            log.info("[log] 取消设置缓存键的前缀");
            cacheConfig = cacheConfig.disableKeyPrefix();//取消设置缓存键的前缀
        }
        cacheConfig = cacheConfig.computePrefixWith(cacheName -> cacheName + ":");//覆盖默认key双冒号

        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(cacheConfig)
                /*.withInitialCacheConfigurations(initialCacheConfigurations)*/
                .build();
    }

}