package org.course.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.course.model.UserPostStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestParams {

    private Integer pageNumber;

    private Integer pageSize;

    private String sort;

    private UserPostStatus status;

}
