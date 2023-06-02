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
public class JsonRedisUtil<E> extends BaseRedisUtil<E> {
    @Resource
    private RedisTemplate<String, E> jsonRedisTemplate;

    @PostConstruct
    public void init() {
        setRedisTemplate(jsonRedisTemplate);
    }


    /**
     * 递增
     *
     * @param key
     * @param delta
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于");
        }
        return jsonRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key
     * @param delta
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于");
        }
        return jsonRedisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key
     * @param item
     * @param by
     * @return
     */
    public double hincr(String key, String item, double by) {
        return jsonRedisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key
     * @param item
     * @param by
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return jsonRedisTemplate.opsForHash().increment(key, item, -by);
    }

}
