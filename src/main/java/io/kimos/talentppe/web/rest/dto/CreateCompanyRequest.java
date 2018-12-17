package io.kimos.talentppe.web.rest.dto;

import javax.validation.constraints.NotNull;

public class CreateCompanyRequest {
    @NotNull
    private String taxName;

    @NotNull
    private String taxId;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String companyName;

    @NotNull
    private String contactName;

    @NotNull
    private String phonePrefix;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String contactLastName;

    @NotNull
    private Boolean acceptTermsOfService;

    @NotNull
    private Boolean wantNewsLetter;

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhonePrefix() {
        return phonePrefix;
    }

    public void setPhonePrefix(String phonePrefix) {
        this.phonePrefix = phonePrefix;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContactLastName() {
        return contactLastName;
    }

    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }

    public Boolean getAcceptTermsOfService() {
        return acceptTermsOfService;
    }

    public void setAcceptTermsOfService(Boolean acceptTermsOfService) {
        this.acceptTermsOfService = acceptTermsOfService;
    }

    public Boolean getWantNewsLetter() {
        return wantNewsLetter;
    }

    public void setWantNewsLetter(Boolean wantNewsLetter) {
        this.wantNewsLetter = wantNewsLetter;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "CreateCompanyRequest{" +
            "taxName='" + taxName + '\'' +
            ", taxId='" + taxId + '\'' +
            ", email='" + email + '\'' +
            ", companyName='" + companyName + '\'' +
            ", contactName='" + contactName + '\'' +
            ", phonePrefix='" + phonePrefix + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", contactLastName='" + contactLastName + '\'' +
            ", acceptTermsOfService=" + acceptTermsOfService +
            ", wantNewsLetter=" + wantNewsLetter +
            '}';
    }
}
