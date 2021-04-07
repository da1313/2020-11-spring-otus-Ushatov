package org.course.keyholder;

public interface KeyHolder<T1, T2> {

    T2 getKey(EntityName name, T1 key);

    void saveKey(EntityName name, T1 key, T2 value);

    T2 getNewKey(EntityName name);

    void setKey(EntityName name, T2 value);

    void clear();

    String getJob();

}
