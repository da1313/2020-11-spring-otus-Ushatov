package org.course.dao;

import lombok.AllArgsConstructor;
import org.course.dao.intefaces.AuthorDao;
import org.course.dao.mappers.AuthorMapper;
import org.course.domain.Author;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@AllArgsConstructor
public class AuthorDaoJdbc implements AuthorDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public Optional<Author> findById(long id) {
        Map<String, Long> params = Collections.singletonMap("id", id);
        try{
            return Optional.ofNullable(namedParameterJdbcOperations
                    .queryForObject("select id, name from authors where id =:id", params, new AuthorMapper()));
        } catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public List<Author> findAll() {
        return namedParameterJdbcOperations.query("select id, name from authors", new AuthorMapper());
    }

    @Override
    public void deleteById(long id) {
        Map<String, Long> params = Collections.singletonMap("id", id);
        namedParameterJdbcOperations.update("delete from authors where id = :id", params);
    }

    @Override
    public void delete() {
        namedParameterJdbcOperations.getJdbcOperations().execute("delete from authors");
    }

    @Override
    public Long create(Author author) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", author.getName());
        namedParameterJdbcOperations.update("insert into authors(name) values(:name)", params, keyHolder);
        author.setId((Long) keyHolder.getKey());
        return (Long) keyHolder.getKey();
    }

    @Override
    public void update(Author author) {
        if (author.getId() == null){
            throw new IllegalArgumentException("Identifier not set. Can't update the entity, consider persist it first!");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", author.getId());
        params.put("name", author.getName());
        namedParameterJdbcOperations.update("update authors set name = :name where id = :id", params);
    }

    @Override
    public long count() {
        Long result = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select count(a.id) from authors a", Long.class);
        return result == null ? 0 : result;
    }
}
