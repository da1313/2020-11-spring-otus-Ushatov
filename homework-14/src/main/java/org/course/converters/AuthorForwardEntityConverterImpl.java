package org.course.converters;

import lombok.RequiredArgsConstructor;
import org.course.domain.nosql.AuthorNosql;
import org.course.domain.sql.Author;
import org.course.keyholder.EntityName;
import org.course.keyholder.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthorForwardEntityConverterImpl implements EntityConverter<Author, AuthorNosql> {

    private final KeyHolder<Long, String> keyHolder;

    @Override
    public AuthorNosql convert(Author input) {
        String id = keyHolder.getNewKey(EntityName.AUTHOR);
        keyHolder.saveKey(EntityName.AUTHOR, input.getId(), id);
        return new AuthorNosql(id, input.getName());
    }
}
