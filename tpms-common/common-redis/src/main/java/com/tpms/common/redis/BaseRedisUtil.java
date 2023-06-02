package com.tpms.common.redis;

import lombok.Setter;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author wld
 * @date 2023/5/16 - 16:58
 */
@Component
public class BaseRedisUtil<E> {

    @Setter
    private RedisTemplate<String, E> redisTemplate;


    /**
     * 过期时间一周
     */
    private static final long WEEK_SECONDS = 7 * 24 * 60 * 60;

    //////////////////////////////////////////////common///////////////////////////////////////////////////

    /**
     * 列举出所有的key的集合
     *
     * @return
     */
    public Set<String> keys(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            return keys;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 指定缓存失效时间
     *
     * @param key
     * @param time
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key
     * @return
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    public boolean exists(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key
     */
    @SuppressWarnings("unchecked")
    public boolean del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
                return true;
            } else {
                redisTemplate.delete(Arrays.asList(key));
                return true;
            }
        }
        return false;
    }
    ////////////////////////////////////////////////////////String////////////////////////////////////////////////////////

    /**
     * 普通缓存放入，使用系统默认的过期时间
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, E value) {
        try {
            redisTemplate.opsForValue().set(key, value, WEEK_SECONDS, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    public boolean set(String key, E value, long expireTime) {
        try {
            if (expireTime > 0) {
                redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 批量添加String
     *
     * @param map
     * @return
     */
    public boolean multiSet(Map<String, E> map) {
        try {
            redisTemplate.opsForValue().multiSet(map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key
     * @return
     */
    public E get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /////////////////////////////////////////////////////////Map/hash///////////////////////////////////////////////////

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key
     * @param item
     * @param value
     * @return
     */
    public boolean hset(String key, String item, E value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key
     * @param item
     * @param value
     * @param time
     * @return
     */
    public boolean hset(String key, String item, E value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将Map中的元素放入set中
     *
     * @param key
     * @param map
     * @return
     */
    public boolean hmset(String key, Map<String, E> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key
     * @param map
     * @param time
     * @return
     */
    public boolean hmset(String key, Map<String, E> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashGet
     *
     * @param key
     * @param item
     * @return
     */
    public E hget(String key, String item) {
        final HashOperations<String, String, E> ops = redisTemplate.opsForHash();
        return ops.get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key
     * @return
     */
    public Map<String, E> hmget(String key) {
        final HashOperations<String, String, E> ops = redisTemplate.opsForHash();
        return ops.entries(key);
    }

    /**
     * 删除hash表中的值
     *
     * @param key
     * @param item
     */
    public void hdel(String key, String... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key
     * @param item
     * @return
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /////////////////////////////////////////////////////////////////////////////set////////////////////////////////////////////////////////////////

    // 将数据放入set缓存
    public long sSet(String key, E... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 将set数据放入缓存
    public long sSetWithExpire(String key, long time, E... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 根据key获取Set中的所有值
    public Set<E> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 根据value从一个set中查询,是否存在
    public boolean sHasKey(String key, E value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 获取set缓存的长度
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 移除值为value的
    public long setRemove(String key, E... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    // ===============================list=================================

    /**
     * 将Object放入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean lSet(String key, E value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将Object放入缓存，同时指定过期时间
     *
     * @param key
     * @param value
     * @param time
     * @return
     */
    public boolean lSet(String key, E value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean lSets(String key, List<E> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存，同时指定过期时间
     *
     * @param key
     * @param value
     * @param time
     * @return
     */
    public boolean lSets(String key, List<E> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取list缓存指定范围的内容
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<E> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取list缓存中指定索引的值
     *
     * @param key
     * @param index
     * @return
     */
    public E lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 修改list中的指定索引处的数据
     *
     * @param key
     * @param index
     * @param value
     * @return
     */
    public boolean lUpdateIndex(String key, long index, E value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从list中移除N个值为value的数据
     *
     * @param key
     * @param count
     * @param value
     * @return
     */
    public long lRemove(String key, long count, E value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
