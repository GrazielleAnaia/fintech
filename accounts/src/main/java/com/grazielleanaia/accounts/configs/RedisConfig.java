package com.grazielleanaia.accounts.configs;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.math.BigDecimal;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, BigDecimal> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, BigDecimal> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(BigDecimal.class));
        return redisTemplate;
    }
}
