package org.course.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {

    @Id
    private String id;

    private String name;

    private List<Permission> permissionList;

    @Override
    public String getAuthority() {
        return "ROLE_" + name;
    }
}
