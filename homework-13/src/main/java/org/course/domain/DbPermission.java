package org.course.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Data
@Table(name = "permissions")
@AllArgsConstructor
@NoArgsConstructor
public class DbPermission implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "permission")
    @Enumerated(EnumType.STRING)
    private DbPermissionEnum permission;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private DbRole role;

    @Override
    public String getAuthority() {
        return permission.name();
    }
}
