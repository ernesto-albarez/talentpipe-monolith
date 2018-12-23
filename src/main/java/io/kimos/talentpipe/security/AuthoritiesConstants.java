package io.kimos.talentpipe.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
    public static final String RECRUITER = "ROLE_RECRUITER";
    public static final String COMPANY = "ROLE_COMPANY";

    private AuthoritiesConstants() {
    }
}