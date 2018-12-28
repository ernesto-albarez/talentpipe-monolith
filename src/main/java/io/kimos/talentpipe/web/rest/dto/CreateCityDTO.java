package io.kimos.talentpipe.web.rest.dto;

import javax.validation.constraints.NotNull;

public class CreateCityDTO {

    @NotNull
    private String name;
    @NotNull
    private String postalCode;
    private Long countryId;

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }
}
