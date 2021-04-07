package org.course.converters;

import org.course.domain.nosql.UserNosql;
import org.course.domain.sql.User;
import org.course.keyholder.EntityName;
import org.course.keyholder.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserBackwardEntityConverterImpl implements EntityConverter<UserNosql, User> {

    private final static String JOB_NAME = "nosqlToSqlJob";

    private final List<KeyHolder<String, Long>> keyHolderList;

    private final KeyHolder<String, Long> keyHolder;

    public UserBackwardEntityConverterImpl(@Autowired List<KeyHolder<String, Long>> keyHolderList) {
        this.keyHolderList = keyHolderList;
        keyHolder = keyHolderList.stream().filter(k -> k.getJob().equals(JOB_NAME)).findFirst()
                .orElseThrow(() -> new IllegalStateException("Can't find job with name " + JOB_NAME + " in keyholder declaration"));
    }

    @Override
    public User convert(UserNosql input) {
        Long userId = keyHolder.getNewKey(EntityName.USER);
        keyHolder.saveKey(EntityName.USER, input.getId(), userId);
        return new User(userId, input.getName(), input.getPassword(), input.isActive());
    }
}
