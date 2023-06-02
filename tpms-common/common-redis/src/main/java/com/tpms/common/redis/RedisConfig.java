package com.tpms.common.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis配置类
 * @author wld
 * @date 2023/5/16 - 16:59
 */
//@EnableCaching启用Redis缓存
@EnableCaching
@Configuration
public class RedisConfig<K extends Serializable, E> {

    /**
     * key序列化方式
     */
    private final StringRedisSerializer keySerializer = new StringRedisSerializer();
    @Resource
    private RedisConnectionFactory factory;
    /**
     * value序列化方式
     */
    private Jackson2JsonRedisSerializer<Object> valueSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

    {
        //解决缓存转换异常的问题
        ObjectMapper objectMapper = new ObjectMapper();
        //下面两行解决Java8新日期API序列化问题
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        //设置所有访问权限以及所有的实际类型都可序列化和反序列化
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        valueSerializer.setObjectMapper(objectMapper);
    }


    /**
     * 在Redis中以String保存键、以byte的形式保存值
     *
     * @return
     */
    @Bean
    public RedisTemplate<K, E> byteRedisTemplate() {
        final RedisTemplate<K, E> redisTemplate = new RedisTemplate<>();
        //设置key的序列化规则
        redisTemplate.setKeySerializer(keySerializer);
        // hash的key的序列化规则
        redisTemplate.setHashKeySerializer(keySerializer);

        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

    /**
     * 在Redis中以String保存键、以json的形式保存值
     *
     * @return
     */
    @Bean
    public RedisTemplate<K, E> jsonRedisTemplate() {
        final RedisTemplate<K, E> redisTemplate = new RedisTemplate<>();
        //设置key的序列化规则
        redisTemplate.setKeySerializer(keySerializer);
        // hash的key的序列化规则
        redisTemplate.setHashKeySerializer(keySerializer);

        //hash的value的序列化规则，如果不指定默认是以二进制形式保存的
        redisTemplate.setHashValueSerializer(valueSerializer);
        //设置value的序列化规则
        redisTemplate.setValueSerializer(valueSerializer);

        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

    //用来设置@Cacheable@CachePut@CacheEvict
    @Bean(name = "cacheManager")
    public RedisCacheManager cacheManager() {
        // 配置序列化（解决乱码的问题），通过config对象对缓存进行自定义配置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // 设置缓存的默认过期时间
                .entryTtl(Duration.ofDays(7))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer))
                // 不缓存空值
                .disableCachingNullValues();
        //缓存配置
        Map<String, RedisCacheConfiguration> cacheConfig = new HashMap<>();

        //根据redis缓存配置和reid连接工厂生成redis缓存管理器
        RedisCacheManager redisCacheManager = RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .transactionAware()
                .withInitialCacheConfigurations(cacheConfig)
                .build();
        return redisCacheManager;
    }

    /**
     * 配置事务管理器
     *
     * @param dataSource
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
