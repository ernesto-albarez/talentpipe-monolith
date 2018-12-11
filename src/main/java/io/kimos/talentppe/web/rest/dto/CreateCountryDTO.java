package io.kimos.talentppe.web.rest.dto;

import javax.validation.constraints.NotNull;

public class CreateCountryDTO {
    @NotNull
    private String name;
    @NotNull
    private String code;
    private String phoneCode;
    private String currency;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
