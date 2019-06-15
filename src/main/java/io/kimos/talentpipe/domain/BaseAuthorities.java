package io.kimos.talentpipe.domain;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public abstract class BaseAuthorities {
    public static final SimpleGrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");
}
