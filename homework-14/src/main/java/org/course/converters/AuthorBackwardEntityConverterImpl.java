package org.course.converters;

import org.course.domain.nosql.AuthorNosql;
import org.course.domain.sql.Author;
import org.course.keyholder.EntityName;
import org.course.keyholder.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorBackwardEntityConverterImpl implements EntityConverter<AuthorNosql, Author> {

    private final static String JOB_NAME = "nosqlToSqlJob";

    private final List<KeyHolder<String, Long>> keyHolderList;

    private final KeyHolder<String, Long> keyHolder;

    public AuthorBackwardEntityConverterImpl(@Autowired List<KeyHolder<String, Long>> keyHolderList) {
        this.keyHolderList = keyHolderList;
        keyHolder = keyHolderList.stream().filter(k -> k.getJob().equals(JOB_NAME)).findFirst()
                .orElseThrow(() -> new IllegalStateException("Can't find job with name " + JOB_NAME + " in keyholder declaration"));
    }

    @Override
    public Author convert(AuthorNosql input) {
        long id = keyHolder.getNewKey(EntityName.AUTHOR);
        keyHolder.saveKey(EntityName.AUTHOR, input.getId(), id);
        return new Author(id, input.getName());
    }

}
