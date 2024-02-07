package org.anuen.utils;

import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.anuen.common.enums.RedisConst;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheClient {

    private final StringRedisTemplate redisTemplate;

    public void set(RedisConst basicInf, String key, Object value) {
        key = basicInf.getPrefix() + key;
        String valueStr;
        if (value instanceof String) {
            valueStr = value.toString();
        } else {
            valueStr = JSONObject.toJSONString(value);
        }
        redisTemplate.opsForValue().set(key, valueStr, basicInf.getTTL(), basicInf.getTimeUnit());
    }

    public <R> R acquire(RedisConst basicInf, String key, Class<R> rClz) {
        key = basicInf.getPrefix() + key;
        String valueStr = redisTemplate.opsForValue().get(key);
        return JSONObject.parseObject(valueStr, rClz);
    }


}
