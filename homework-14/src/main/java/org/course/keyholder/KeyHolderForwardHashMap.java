package org.course.keyholder;

import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@ConditionalOnProperty(name = "app.storetype", havingValue = "hashmap")
@Component
public class KeyHolderForwardHashMap implements KeyHolder<Long, String>{

    private final ConcurrentMap<EntityName, ConcurrentHashMap<Long, String>> sqlToNosql = new ConcurrentHashMap<>();

    public KeyHolderForwardHashMap() {
        Arrays.asList(EntityName.values()).forEach(e -> sqlToNosql.put(e, new ConcurrentHashMap<>()));
    }

    @Override
    public String getKey(EntityName name, Long key) {
        return sqlToNosql.get(name).get(key);
    }

    @Override
    public void saveKey(EntityName name, Long key, String value) {
        sqlToNosql.get(name).put(key, value);
    }

    @Override
    public String getNewKey(EntityName name) {
        return new ObjectId().toString();
    }

    @Override
    public void setKey(EntityName name, String value) {

    }

    @Override
    public void clear() {
        Arrays.asList(EntityName.values()).forEach(e -> sqlToNosql.get(e).clear());
    }

    @Override
    public String getJob() {
        return "sqlToNosqlJob";
    }

}
