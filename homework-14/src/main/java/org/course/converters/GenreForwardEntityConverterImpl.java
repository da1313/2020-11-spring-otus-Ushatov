package org.course.converters;

import lombok.RequiredArgsConstructor;
import org.course.domain.nosql.GenreNosql;
import org.course.domain.sql.Genre;
import org.course.keyholder.EntityName;
import org.course.keyholder.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreForwardEntityConverterImpl implements EntityConverter<Genre, GenreNosql> {

    private final KeyHolder<Long, String> keyHolder;

    @Override
    public GenreNosql convert(Genre input) {
        String id = keyHolder.getNewKey(EntityName.GENRE);
        keyHolder.saveKey(EntityName.GENRE, input.getId(), id);
        return new GenreNosql(id, input.getName());
    }

}
