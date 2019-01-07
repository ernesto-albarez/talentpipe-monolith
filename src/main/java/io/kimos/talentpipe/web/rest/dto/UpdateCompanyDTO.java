package io.kimos.talentpipe.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.kimos.talentpipe.domain.City;
import io.kimos.talentpipe.domain.CompanyType;
import io.kimos.talentpipe.domain.Sector;
import io.kimos.talentpipe.domain.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

public class UpdateCompanyDTO {

    @NotNull
    private Long id;

    @NotNull
    private String taxName;

    @NotNull
    private String taxId;

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String street;

    @NotNull
    private Integer number;

    private Integer floor;

    private String apartment;

    @NotNull
    private String postalCode;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String phonePrefix;

    @NotNull
    private String contactName;

    @NotNull
    private String contactLastName;

    @JsonIgnoreProperties("")
    private UpdateSectorDTO sector;

    @JsonIgnoreProperties("")
    private UpdateCityDTO city;

    @JsonIgnoreProperties("")
    private UpdateCompanyTypeDTO companyType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhonePrefix() {
        return phonePrefix;
    }

    public void setPhonePrefix(String phonePrefix) {
        this.phonePrefix = phonePrefix;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactLastName() {
        return contactLastName;
    }

    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }

    public UpdateSectorDTO getSector() {
        return sector;
    }

    public void setSector(UpdateSectorDTO sector) {
        this.sector = sector;
    }

    public UpdateCityDTO getCity() {
        return city;
    }

    public void setCity(UpdateCityDTO city) {
        this.city = city;
    }

    public UpdateCompanyTypeDTO getCompanyType() {
        return companyType;
    }

    public void setCompanyType(UpdateCompanyTypeDTO companyType) {
        this.companyType = companyType;
    }
}
