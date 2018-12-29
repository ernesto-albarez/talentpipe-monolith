package io.kimos.talentpipe.web.rest.errors;

public class UserNotAuthenticatedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public UserNotAuthenticatedException() {
        super(ErrorConstants.USER_NOT_AUTHENTICATED_EXCEPTION, "Login name already used!", "userManagement", "userexists");
    }
}
