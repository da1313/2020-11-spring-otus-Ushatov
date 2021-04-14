package org.course.keyholder;

import org.course.keyholder.poller.KeyPoller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@ConditionalOnProperty(name = "app.storetype", havingValue = "hashmap")
@Component
public class KeyHolderBackwardHashMap implements KeyHolder<String, Long> {

    private final ConcurrentMap<EntityName, ConcurrentHashMap<String, Long>> nosqlToSql = new ConcurrentHashMap<>();

    private final KeyPoller keyPoller;

    private final EntityManager entityManager;

    @Autowired
    public KeyHolderBackwardHashMap(KeyPoller keyPoller, EntityManager entityManager) {
        this.keyPoller = keyPoller;
        this.entityManager = entityManager;
        Arrays.asList(EntityName.values()).forEach(e -> nosqlToSql.put(e, new ConcurrentHashMap<>()));
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
        return keyPoller.getNextKey(name);
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
