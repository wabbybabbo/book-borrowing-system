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
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Redis 缓存配置
 */
@Slf4j
@EnableCaching //开启缓存注解功能
//启用CacheProperties的配置属性绑定，这允许从配置文件（如application.properties或application.yml）中读取Redis缓存相关的配置。
@EnableConfigurationProperties(CacheProperties.class)
@Configuration
public class RedisCacheConfig {

    /**
     * 自定义 RedisCacheManager
     * <p>
     * 修改 Redis 序列化方式，默认 JdkSerializationRedisSerializer
     *
     * @param redisConnectionFactory {@link RedisConnectionFactory}
     * @param cacheProperties        {@link CacheProperties}
     * @return {@link RedisCacheManager}
     */
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory, CacheProperties cacheProperties) {
        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration(cacheProperties))
                .build();
    }

    /**
     * 自定义 RedisCacheConfiguration
     *
     * @param cacheProperties {@link CacheProperties}
     * @return {@link RedisCacheConfiguration}
     */
    @Bean
    RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();

        config = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()));
//        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));

        CacheProperties.Redis redisProperties = cacheProperties.getRedis();

        if (redisProperties.getTimeToLive() != null) {
            log.info("[log] 设置缓存的过期时间：{}s", redisProperties.getTimeToLive().getSeconds());
            config = config.entryTtl(redisProperties.getTimeToLive());//设置缓存的过期时间
        }
        if (!redisProperties.isCacheNullValues()) {
            log.info("[log] 取消设置缓存空值");
            config = config.disableCachingNullValues();//取消设置缓存空值
        }
        if (!redisProperties.isUseKeyPrefix()) {
            log.info("[log] 取消设置缓存键的前缀");
            config = config.disableKeyPrefix();//取消设置缓存键的前缀
        }
        config = config.computePrefixWith(name -> name + ":");//覆盖默认key双冒号  CacheKeyPrefix#prefixed
        return config;
    }

}

