package io.kimos.talentpipe.web.rest.errors;

public class UserNotFoundException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public UserNotFoundException() {
        super(ErrorConstants.USER_NOT_FOUND_EXCEPTION, "Login name already used!", "userManagement", "userexists");
    }
}
