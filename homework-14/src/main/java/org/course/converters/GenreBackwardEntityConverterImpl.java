package org.course.converters;

import org.course.domain.nosql.GenreNosql;
import org.course.domain.sql.Genre;
import org.course.keyholder.EntityName;
import org.course.keyholder.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenreBackwardEntityConverterImpl implements EntityConverter<GenreNosql, Genre> {

    private final static String JOB_NAME = "nosqlToSqlJob";

    private final List<KeyHolder<String, Long>> keyHolderList;

    private final KeyHolder<String, Long> keyHolder;

    public GenreBackwardEntityConverterImpl(@Autowired List<KeyHolder<String, Long>> keyHolderList) {
        this.keyHolderList = keyHolderList;
        keyHolder = keyHolderList.stream().filter(k -> k.getJob().equals(JOB_NAME)).findFirst()
                .orElseThrow(() -> new IllegalStateException("Can't find job with name " + JOB_NAME + " in keyholder declaration"));
    }

    @Override
    public Genre convert(GenreNosql input) {
        long id = keyHolder.getNewKey(EntityName.GENRE);
        keyHolder.saveKey(EntityName.GENRE, input.getId(), id);
        return new Genre(id, input.getName());
    }
}
