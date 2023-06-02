package com.tpms.common.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author wld
 * @date 2023/5/16 - 16:59
 */
@Component
public class ByteRedisUtil<E> extends BaseRedisUtil<E> {
    @Resource
    private RedisTemplate<String, E> byteRedisTemplate;

    @PostConstruct
    public void init() {
        setRedisTemplate(byteRedisTemplate);
    }
}
