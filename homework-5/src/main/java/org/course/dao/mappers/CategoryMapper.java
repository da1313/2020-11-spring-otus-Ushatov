package org.course.dao.mappers;

import org.course.domain.Category;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryMapper implements RowMapper<Category> {

    @Override
    public Category mapRow(ResultSet resultSet, int i) throws SQLException {
        long categoryId = resultSet.getLong("categoryId");
        String categoryName = resultSet.getString("categoryName");
        return new Category(categoryId, categoryName);
    }
}
