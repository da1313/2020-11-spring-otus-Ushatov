package org.course.converters;

import lombok.RequiredArgsConstructor;
import org.course.domain.nosql.UserNosql;
import org.course.domain.sql.User;
import org.course.keyholder.EntityName;
import org.course.keyholder.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserBackwardEntityConverterImpl implements EntityConverter<UserNosql, User> {

    private final KeyHolder<String, Long> keyHolder;

    @Override
    public User convert(UserNosql input) {
        Long userId = keyHolder.getNewKey(EntityName.USER);
        keyHolder.saveKey(EntityName.USER, input.getId(), userId);
        return new User(userId, input.getName(), input.getPassword(), input.isActive());
    }
}
