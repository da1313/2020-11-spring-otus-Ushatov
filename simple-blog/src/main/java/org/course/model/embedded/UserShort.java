package org.course.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserShort {

    private String id;

    private String firstName;

    private String lastName;

    private String avatarUrl;

}
