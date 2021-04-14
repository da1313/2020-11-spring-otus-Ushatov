package org.course.keyholder;

import org.course.keyholder.poller.KeyPoller;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@ConditionalOnProperty(name = "app.storetype", havingValue = "redis")
@Component
public class KeyHolderBackwardRedis implements KeyHolder<String, Long> {

    private final RedisOperations<Object, Object> redisOperations;

    private final KeyPoller keyPoller;

    public KeyHolderBackwardRedis(RedisOperations<Object, Object> redisOperations, KeyPoller keyPoller) {
        this.redisOperations = redisOperations;
        this.keyPoller = keyPoller;
    }

    @Override
    public Long getKey(EntityName name, String key) {
        return (Long) redisOperations.opsForHash().get(name, key);
    }

    @Override
    public void saveKey(EntityName name, String key, Long value) {
        redisOperations.opsForHash().put(name, key, value);
    }

    @Override
    public Long getNewKey(EntityName name) {
        return keyPoller.getNextKey(name);
    }

    @Override
    public void clear() {
        Arrays.asList(EntityName.values()).forEach(e -> redisOperations.delete(e));
    }

    @Override
    public String getJob() {
        return "nosqlToSqlJob";
    }
}
