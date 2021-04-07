package org.course.converters;

import org.course.domain.nosql.AuthorNosql;
import org.course.domain.sql.Author;
import org.course.keyholder.EntityName;
import org.course.keyholder.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorForwardEntityConverterImpl implements EntityConverter<Author, AuthorNosql> {

    private static final String JOB_NAME = "sqlToNosqlJob";

    private final List<KeyHolder<Long, String>> keyHolderList;

    private final KeyHolder<Long, String> keyHolder;

    public AuthorForwardEntityConverterImpl(@Autowired List<KeyHolder<Long, String>> keyHolderList) {
        this.keyHolderList = keyHolderList;
        keyHolder = keyHolderList.stream().filter(k -> k.getJob().equals(JOB_NAME)).findFirst()
                .orElseThrow(() -> new IllegalStateException("Can't find job with name " + JOB_NAME + " in keyholder declaration"));
    }

    @Override
    public AuthorNosql convert(Author input) {
        String id = keyHolder.getNewKey(EntityName.AUTHOR);
        keyHolder.saveKey(EntityName.AUTHOR, input.getId(), id);
        return new AuthorNosql(id, input.getName());
    }
}
