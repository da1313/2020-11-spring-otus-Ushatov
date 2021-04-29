package org.course.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse {

    private final boolean result;

    private final String message;

    private final String user;

    public static GenericResponse withResult(boolean result){
        return new GenericResponse(result, null, null);
    }

    public static GenericResponse withMessage(boolean result, String message){
        return new GenericResponse(result, message, null);
    }

    public static GenericResponse withUser(boolean result, String user){
        return new GenericResponse(result, null, user);
    }

}
