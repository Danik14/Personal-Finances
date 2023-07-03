package slash.financing.enums;

import static slash.financing.enums.Permission.ADMIN_CREATE;
import static slash.financing.enums.Permission.ADMIN_DELETE;
import static slash.financing.enums.Permission.ADMIN_READ;
import static slash.financing.enums.Permission.ADMIN_UPDATE;
import static slash.financing.enums.Permission.VERIFIED_CREATE;
import static slash.financing.enums.Permission.VERIFIED_DELETE;
import static slash.financing.enums.Permission.VERIFIED_READ;
import static slash.financing.enums.Permission.VERIFIED_UPDATE;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserRole {

        USER(Collections.emptySet()),
        VERIFIED_USER(
                        Set.of(
                                        VERIFIED_READ,
                                        VERIFIED_UPDATE,
                                        VERIFIED_DELETE,
                                        VERIFIED_CREATE)),
        ADMIN(
                        Set.of(
                                        ADMIN_READ,
                                        ADMIN_UPDATE,
                                        ADMIN_DELETE,
                                        ADMIN_CREATE));

        @Getter
        private final Set<Permission> permissions;

        public List<SimpleGrantedAuthority> getAuthorities() {
                var authorities = getPermissions()
                                .stream()
                                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                                .collect(Collectors.toList());
                authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
                return authorities;
        }
}