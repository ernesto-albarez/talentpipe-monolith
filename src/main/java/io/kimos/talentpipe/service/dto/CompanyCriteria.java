package io.kimos.talentpipe.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Company entity. This class is used in CompanyResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /companies?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CompanyCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter taxName;

    private StringFilter taxId;

    private StringFilter email;

    private StringFilter name;

    private StringFilter street;

    private IntegerFilter floor;

    private IntegerFilter number;

    private StringFilter apartment;

    private StringFilter postalCode;

    private StringFilter phone;

    private StringFilter contactName;

    private LongFilter mainUserId;

    private LongFilter sectorId;

    private LongFilter cityId;

    private LongFilter companyTypeId;

    public CompanyCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTaxName() {
        return taxName;
    }

    public void setTaxName(StringFilter taxName) {
        this.taxName = taxName;
    }

    public StringFilter getTaxId() {
        return taxId;
    }

    public void setTaxId(StringFilter taxId) {
        this.taxId = taxId;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getStreet() {
        return street;
    }

    public void setStreet(StringFilter street) {
        this.street = street;
    }

    public IntegerFilter getFloor() {
        return floor;
    }

    public void setFloor(IntegerFilter floor) {
        this.floor = floor;
    }

    public IntegerFilter getNumber() {
        return number;
    }

    public void setNumber(IntegerFilter number) {
        this.number = number;
    }

    public StringFilter getApartment() {
        return apartment;
    }

    public void setApartment(StringFilter apartment) {
        this.apartment = apartment;
    }

    public StringFilter getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(StringFilter postalCode) {
        this.postalCode = postalCode;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getContactName() {
        return contactName;
    }

    public void setContactName(StringFilter contactName) {
        this.contactName = contactName;
    }

    public LongFilter getMainUserId() {
        return mainUserId;
    }

    public void setMainUserId(LongFilter mainUserId) {
        this.mainUserId = mainUserId;
    }

    public LongFilter getSectorId() {
        return sectorId;
    }

    public void setSectorId(LongFilter sectorId) {
        this.sectorId = sectorId;
    }

    public LongFilter getCityId() {
        return cityId;
    }

    public void setCityId(LongFilter cityId) {
        this.cityId = cityId;
    }

    public LongFilter getCompanyTypeId() {
        return companyTypeId;
    }

    public void setCompanyTypeId(LongFilter companyTypeId) {
        this.companyTypeId = companyTypeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CompanyCriteria that = (CompanyCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(taxName, that.taxName) &&
            Objects.equals(taxId, that.taxId) &&
            Objects.equals(email, that.email) &&
            Objects.equals(name, that.name) &&
            Objects.equals(street, that.street) &&
            Objects.equals(floor, that.floor) &&
            Objects.equals(number, that.number) &&
            Objects.equals(apartment, that.apartment) &&
            Objects.equals(postalCode, that.postalCode) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(contactName, that.contactName) &&
            Objects.equals(mainUserId, that.mainUserId) &&
            Objects.equals(sectorId, that.sectorId) &&
            Objects.equals(cityId, that.cityId) &&
            Objects.equals(companyTypeId, that.companyTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        taxName,
        taxId,
        email,
        name,
        street,
        floor,
        number,
        apartment,
        postalCode,
        phone,
        contactName,
        mainUserId,
        sectorId,
        cityId,
        companyTypeId
        );
    }

    @Override
    public String toString() {
        return "CompanyCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (taxName != null ? "taxName=" + taxName + ", " : "") +
                (taxId != null ? "taxId=" + taxId + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (street != null ? "street=" + street + ", " : "") +
                (floor != null ? "floor=" + floor + ", " : "") +
                (number != null ? "number=" + number + ", " : "") +
                (apartment != null ? "apartment=" + apartment + ", " : "") +
                (postalCode != null ? "postalCode=" + postalCode + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (contactName != null ? "contactName=" + contactName + ", " : "") +
                (mainUserId != null ? "mainUserId=" + mainUserId + ", " : "") +
                (sectorId != null ? "sectorId=" + sectorId + ", " : "") +
                (cityId != null ? "cityId=" + cityId + ", " : "") +
                (companyTypeId != null ? "companyTypeId=" + companyTypeId + ", " : "") +
            "}";
    }

}
