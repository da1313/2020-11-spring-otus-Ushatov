package org.course.dao;

import lombok.AllArgsConstructor;
import org.course.dao.intefaces.GenreDao;
import org.course.dao.mappers.GenreMapper;
import org.course.domain.Genre;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@AllArgsConstructor
public class GenreDaoJdbc implements GenreDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public Optional<Genre> findById(long id) {
        try{
            Map<String, Long> params = Collections.singletonMap("id", id);
            return Optional.ofNullable(namedParameterJdbcOperations
                    .queryForObject("select id, name from genres where id =:id", params, new GenreMapper()));
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> findAll() {
        return namedParameterJdbcOperations.query("select id, name from genres", new GenreMapper());
    }

    @Override
    public void deleteById(long id) {
        Map<String, Long> params = Collections.singletonMap("id", id);
        namedParameterJdbcOperations.update("delete from genres where id = :id", params);
    }

    @Override
    public void delete() {
        namedParameterJdbcOperations.getJdbcOperations().execute("delete from genres");
    }

    @Override
    public Long create(Genre genre) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", genre.getName());
        namedParameterJdbcOperations.update("insert into genres(name) values(:name)", params, keyHolder);
        genre.setId((Long) keyHolder.getKey());
        return (Long) keyHolder.getKey();
    }

    @Override
    public void update(Genre genre) {
        if (genre.getId() == null){
            throw new IllegalArgumentException("Identifier not set. Can't update the entity, consider persist it first!");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", genre.getId());
        params.put("name", genre.getName());
        namedParameterJdbcOperations.update("update genres set name = :name where id = :id", params);
    }

    @Override
    public long count() {
        Long result = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select count(g.id) from genres g", Long.class);
        return result == null ? 0 : result;
    }
}
