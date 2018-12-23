package io.kimos.talentpipe.web.rest.dto;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RegistryRecruiterRequest {
    @NotNull
    @NotEmpty
    private String taxId;
    @NotNull
    @NotEmpty
    @Email
    private String email;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    private String lastName;
    @NotNull
    @NotEmpty
    private String phonePrefix;
    @NotNull
    @NotEmpty
    private String phoneNumber;
    @NotNull
    @NotEmpty
    private String password;
    @AssertTrue
    private Boolean aceptTermsOfService;
    private Boolean wantNewsLetter;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAceptTermsOfService() {
        return aceptTermsOfService;
    }

    public void setAceptTermsOfService(Boolean aceptTermsOfService) {
        this.aceptTermsOfService = aceptTermsOfService;
    }

    public Boolean getWantNewsLetter() {
        return wantNewsLetter;
    }

    public void setWantNewsLetter(Boolean wantNewsLetter) {
        this.wantNewsLetter = wantNewsLetter;
    }

    @Override
    public String toString() {
        return "RegistryRecruiterRequest{" +
            "taxId='" + taxId + '\'' +
            ", email='" + email + '\'' +
            ", name='" + name + '\'' +
            ", lastName='" + lastName + '\'' +
            ", phonePrefix='" + phonePrefix + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", password='" + password + '\'' +
            ", aceptTermsOfService=" + aceptTermsOfService +
            ", wantNewsLetter=" + wantNewsLetter +
            '}';
    }
}
