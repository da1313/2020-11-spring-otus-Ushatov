package org.course.service.intefaces;

public interface ReadEntityService {

    String readEntityById(String tableName, long id);

    String readEntities(String tableName);

}
