package com.g25.mailer.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession
public class RedisConfig {

//    @Value("${REDIS_HOST:127.0.0.1}")
//    private String host;
//
//    @Value("${REDIS_PORT:6379}")
//    private int port;
//
//    @Value("${REDIS_PASSWORD:}") // 비밀번호가 없으면 빈 문자열
//    private String password;
//
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        RedisClusterConfiguration redisConfig = new RedisClusterConfiguration();
//        redisConfig.clusterNode(host, port);
//        if (!password.isEmpty()) {
//            redisConfig.setPassword(password);
//        }
//        return new LettuceConnectionFactory(redisConfig);
//    }
//
//    @Bean
//    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
//        return new GenericJackson2JsonRedisSerializer();
//    }

}
