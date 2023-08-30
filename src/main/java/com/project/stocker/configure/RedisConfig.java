package com.project.stocker.configure;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.stocker.dto.request.TradeCreateRequestDto;
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
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
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
        RedisCacheConfiguration cacheConfiguration = createCacheConfiguration(Duration.ofHours(1)).disableCachingNullValues();

        HashMap<String, RedisCacheConfiguration> cacheConfigurationsHashMap = new HashMap<>();
        cacheConfigurationsHashMap.put("yesterdayLastPrice", createCacheConfiguration(Duration.ofMinutes(1437)));
        cacheConfigurationsHashMap.put("top10ByTradeIncrease", createCacheConfiguration(Duration.ofMinutes(1437)));
        cacheConfigurationsHashMap.put("top10ByTradeDecrease",createCacheConfiguration(Duration.ofMinutes(1437)));
        cacheConfigurationsHashMap.put("top10ByTradeVolume", createCacheConfiguration(Duration.ofMinutes(1437)));

        return RedisCacheManager.builder()
                .cacheWriter(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(cacheConfiguration)
                .withInitialCacheConfigurations(cacheConfigurationsHashMap)
                .build();
    }

    private RedisCacheConfiguration createCacheConfiguration(Duration ttl){
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // JSON Serializer
        Jackson2JsonRedisSerializer<TradeCreateRequestDto> serializer = new Jackson2JsonRedisSerializer<>(TradeCreateRequestDto.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
}
