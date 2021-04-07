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
public class GenreForwardEntityConverterImpl implements EntityConverter<Genre, GenreNosql> {

    private static final String JOB_NAME = "sqlToNosqlJob";

    private final List<KeyHolder<Long, String>> keyHolderList;

    private final KeyHolder<Long, String> keyHolder;

    public GenreForwardEntityConverterImpl(@Autowired List<KeyHolder<Long, String>> keyHolderList) {
        this.keyHolderList = keyHolderList;
        keyHolder = keyHolderList.stream().filter(k -> k.getJob().equals(JOB_NAME)).findFirst()
                .orElseThrow(() -> new IllegalStateException("Can't find job with name " + JOB_NAME + " in keyholder declaration"));
    }

    @Override
    public GenreNosql convert(Genre input) {
        String id = keyHolder.getNewKey(EntityName.GENRE);
        keyHolder.saveKey(EntityName.GENRE, input.getId(), id);
        return new GenreNosql(id, input.getName());
    }

}
