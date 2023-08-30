package com.project.stocker.configure;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;

@Configuration
@EnableCaching
public class RedisConfig {

    private String host;
    private int port;

    public RedisConfig(@Value("${spring.data.redis.host}") String host, @Value("${spring.data.redis.port}") int port) {
        this.host = host;
        this.port = port;
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration cacheConfiguration = createCacheConfiguration(Duration.ofHours(1));

        HashMap<String, RedisCacheConfiguration> cacheConfigurationsHashMap = new HashMap<>();
        cacheConfigurationsHashMap.put("yesterdayLastPrice", createCacheConfiguration(Duration.ofMinutes(1435)));
        cacheConfigurationsHashMap.put("top10ByTradeIncrease", createCacheConfiguration(Duration.ofMinutes(1435)));
        cacheConfigurationsHashMap.put("top10ByTradeDecrease",createCacheConfiguration(Duration.ofMinutes(1435)));
        cacheConfigurationsHashMap.put("top10ByTradeVolume", createCacheConfiguration(Duration.ofMinutes(1435)));

        return RedisCacheManager.builder()
                .cacheWriter(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(cacheConfiguration)
                .withInitialCacheConfigurations(cacheConfigurationsHashMap)
                .build();
    }

    private RedisCacheConfiguration createCacheConfiguration(Duration ttl){
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .disableCachingNullValues();
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
}
