package org.course.converters;

import org.course.domain.nosql.UserNosql;
import org.course.domain.sql.User;
import org.course.keyholder.EntityName;
import org.course.keyholder.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserForwardEntityConverterImpl implements EntityConverter<User, UserNosql> {

    private static final String JOB_NAME = "sqlToNosqlJob";

    private final List<KeyHolder<Long, String>> keyHolderList;

    private final KeyHolder<Long, String> keyHolder;

    public UserForwardEntityConverterImpl(@Autowired List<KeyHolder<Long, String>> keyHolderList) {
        this.keyHolderList = keyHolderList;
        keyHolder = keyHolderList.stream().filter(k -> k.getJob().equals(JOB_NAME)).findFirst()
                .orElseThrow(() -> new IllegalStateException("Can't find job with name " + JOB_NAME + " in keyholder declaration"));
    }

    @Override
    public UserNosql convert(User input) {
        String userId = keyHolder.getNewKey(EntityName.USER);
        keyHolder.saveKey(EntityName.USER, input.getId(), userId);
        return new UserNosql(userId, input.getName(), input.getPassword(), input.isActive());
    }
}
