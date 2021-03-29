package org.course.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    USER(Set.of()),
    ADMIN(Set.of(Permission.BOOK_MANAGE, Permission.BOOK_CREATE, Permission.BOOK_UPDATE, Permission.BOOK_DELETE)),
    ADMIN_TRAINEE(Set.of(Permission.BOOK_MANAGE));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<? extends GrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> grantedAuthorities = permissions.stream()
                .map(p -> new SimpleGrantedAuthority(p.getPermission())).collect(Collectors.toSet());
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return grantedAuthorities;
    }

}
