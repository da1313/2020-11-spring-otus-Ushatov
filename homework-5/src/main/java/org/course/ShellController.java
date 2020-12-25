package org.course;

import lombok.AllArgsConstructor;
import org.course.config.AppProperties;
import org.course.service.intefaces.*;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@AllArgsConstructor
public class ShellController {

    private final CreateEntityService createEntityService;
    private final ReadEntityService readEntityService;
    private final UpdateEntityService updateEntityService;
    private final DeleteEntityService deleteEntityService;
    private final CountEntityService countEntityService;
    private final AppProperties appProperties;

    @ShellMethod(value = "create row of the specified table", key = {"create", "add"})
    public String createEntity(String tableName){
        return createEntityService.createEntity(tableName);
    }

    @ShellMethod(value = "view specified table", key = {"show-table", "st"})
    public String viewTable(String tableName){
        return readEntityService.readEntities(tableName);
    }

    @ShellMethod(value = "view row of the specified table", key = {"show-row", "sr"})
    public String viewEntity(String tableName, long id){
        try {
            return readEntityService.readEntityById(tableName, id);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }

    @ShellMethod(value = "update row of specified table", key = {"update", "u"})
    public String updateEntity(String tableName){
        return updateEntityService.updateEntity(tableName);
    }

    @ShellMethod(value = "delete row or all rows (use all) of the specified table", key = {"delete", "d"})
    public String deleteEntity(String tableName, String flag){
        if (flag.equals("a") || flag.equals("all")){
            return deleteEntityService.deleteAll(tableName);
        } else {
            return deleteEntityService.deleteEntityById(tableName);
        }
    }

    @ShellMethod(value = "view row count of the specified table", key = {"count"})
    public String entityCount(String tableName){
        return countEntityService.getCount(tableName);
    }

    @ShellMethod(value = "view all supported tables", key = {"tables", "t"})
    public String viewAllTables(){
        return appProperties.getTables().toString();
    }

}
