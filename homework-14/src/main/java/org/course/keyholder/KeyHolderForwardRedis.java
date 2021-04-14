package org.course.keyholder;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@ConditionalOnProperty(name = "app.storetype", havingValue = "redis")
@Component
@RequiredArgsConstructor
public class KeyHolderForwardRedis implements KeyHolder<Long, String> {

    private final RedisOperations<Object, Object> redisOperations;

    @Override
    public String getKey(EntityName name, Long key) {
        return (String) redisOperations.opsForHash().get(name, key);
    }

    @Override
    public void saveKey(EntityName name, Long key, String value) {
        redisOperations.opsForHash().put(name, key, value);
    }

    @Override
    public String getNewKey(EntityName name) {
        return new ObjectId().toString();
    }

    @Override
    public void clear() {
        Arrays.asList(EntityName.values()).forEach(e -> redisOperations.delete(e));
    }

    @Override
    public String getJob() {
        return "sqlToNosqlJob";
    }

}
