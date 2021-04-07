package org.course.keyholder;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@ConditionalOnProperty(name = "app.storetype", havingValue = "hashmap")
@Component
public class KeyHolderBackwardHashMap implements KeyHolder<String, Long> {

    private final ConcurrentMap<EntityName, ConcurrentHashMap<String, Long>> nosqlToSql = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<EntityName, AtomicLong> idGenerators = new ConcurrentHashMap<>();

    public KeyHolderBackwardHashMap() {
        Arrays.asList(EntityName.values()).forEach(e -> nosqlToSql.put(e, new ConcurrentHashMap<>()));
        Arrays.asList(EntityName.values()).forEach(e -> idGenerators.put(e, new AtomicLong()));
    }

    @Override
    public Long getKey(EntityName name, String key) {
        return nosqlToSql.get(name).get(key);
    }

    @Override
    public void saveKey(EntityName name, String key, Long value) {
        nosqlToSql.get(name).put(key, value);
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
        Arrays.asList(EntityName.values()).forEach(e -> nosqlToSql.get(e).clear());
    }

    @Override
    public String getJob() {
        return "nosqlToSqlJob";
    }

}
