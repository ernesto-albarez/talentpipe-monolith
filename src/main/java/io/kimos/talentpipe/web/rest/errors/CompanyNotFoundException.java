package io.kimos.talentpipe.web.rest.errors;

public class CompanyNotFoundException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public CompanyNotFoundException() {
        super(ErrorConstants.COMPANY_NOT_FOUND_EXCEPTION, "Company wasnt found!", "companyManagment", "companyNotFound");
    }
}
