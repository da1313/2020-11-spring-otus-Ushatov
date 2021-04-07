package org.course.keyholder;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ConditionalOnProperty(name = "app.storetype", havingValue = "redis")
@Component
public class KeyHolderBackwardRedis implements KeyHolder<String, Long> {

    private final RedisOperations<Object, Object> redisOperations;

    private final ConcurrentHashMap<EntityName, AtomicLong> idGenerators = new ConcurrentHashMap<>();

    public KeyHolderBackwardRedis(RedisOperations<Object, Object> redisOperations) {
        this.redisOperations = redisOperations;
        Arrays.asList(EntityName.values()).forEach(e -> idGenerators.put(e, new AtomicLong()));
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
        return idGenerators.get(name).getAndIncrement();
    }

    @Override
    public void setKey(EntityName name, Long value) {
        idGenerators.get(name).set(value);
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
