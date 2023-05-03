package com.example.lablink.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig extends CachingConfigurerSupport {
    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.password}")
    private String password;

//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        return new LettuceConnectionFactory(host, port);
//    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(password);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // Redis에 데이터를 저장하거나 조회할 때 이를 byte 배열로 변환하여 처리함
        // 이 때 Key와 Value의 데이터 타입에 따라 Serializer를 설정해주어야 하는데
        // Serializer는 Java 객체를 Redis에서 사용할 수 있는 byte 배열로 변환하는 역할 수행
        // 이를 통해 Redis에 데이터를 저장하고 조회할 때 일관된 데이터 타입으로 처리할 수 있음
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        return redisTemplate;
    }

//    @Bean
//    public RedisTemplate<String, Object> chatRedisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, Object> chatRedisTemplate = new RedisTemplate<>();
//        chatRedisTemplate.setConnectionFactory(connectionFactory);
//        chatRedisTemplate.setKeySerializer(new StringRedisSerializer());
//        chatRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
//        return chatRedisTemplate;
//    }
//
//    // ChannelTopic 등록
//    @Bean
//    public ChannelTopic channelTopic() {
//        return new ChannelTopic("chatroom");
//    }
//
//    // MessageListenerAdapter 등록
//    @Bean
//    public MessageListenerAdapter listenerAdapter(RedisSubscriber redisSubscriber) {
//        return new MessageListenerAdapter(redisSubscriber, "sendMessage");
//    }
//    // 등록한 ChannelTopic, MessageListenerAdapter로 RedisMessageListenerContainer 등록
//    @Bean
//    public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter, ChannelTopic channelTopic) {
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.addMessageListener(listenerAdapter, channelTopic);
//        return container;
//    }

    //레디스 캐시
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext
                        .SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));


        return RedisCacheManager
                .RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }
}