package org.course.converters;

import lombok.RequiredArgsConstructor;
import org.course.domain.nosql.AuthorNosql;
import org.course.domain.sql.Author;
import org.course.keyholder.EntityName;
import org.course.keyholder.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthorBackwardEntityConverterImpl implements EntityConverter<AuthorNosql, Author> {

    private final KeyHolder<String, Long> keyHolder;

    @Override
    public Author convert(AuthorNosql input) {
        long id = keyHolder.getNewKey(EntityName.AUTHOR);
        keyHolder.saveKey(EntityName.AUTHOR, input.getId(), id);
        return new Author(id, input.getName());
    }

}
