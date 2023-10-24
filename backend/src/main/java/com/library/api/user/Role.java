package com.library.api.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.library.api.user.Permission.*;

@RequiredArgsConstructor
public enum Role {
    CLIENT(Collections.emptySet()),
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    LIBRARIAN_CREATE,
                    LIBRARIAN_UPDATE,
                    LIBRARIAN_DELETE,
                    LIBRARIAN_READ
            )
    ),
    LIBRARIAN(
            Set.of(
                    LIBRARIAN_CREATE,
                    LIBRARIAN_UPDATE,
                    LIBRARIAN_DELETE,
                    LIBRARIAN_READ
            )
    );

    @Getter
    private final Set<Permission> permissions;

    final public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
