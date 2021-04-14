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
public class UserForwardEntityConverterImpl implements EntityConverter<User, UserNosql> {

    private final KeyHolder<Long, String> keyHolder;

    @Override
    public UserNosql convert(User input) {
        String userId = keyHolder.getNewKey(EntityName.USER);
        keyHolder.saveKey(EntityName.USER, input.getId(), userId);
        return new UserNosql(userId, input.getName(), input.getPassword(), input.isActive());
    }
}
