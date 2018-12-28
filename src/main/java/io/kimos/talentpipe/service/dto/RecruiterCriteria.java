package io.kimos.talentpipe.service.dto;

import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the Recruiter entity. This class is used in RecruiterResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /recruiters?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RecruiterCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter lastName;

    private StringFilter email;

    private StringFilter taxId;

    private StringFilter phone;

    private StringFilter street;

    private IntegerFilter number;

    private IntegerFilter floor;

    private StringFilter apartment;

    private LongFilter cityId;

    private LongFilter sectorId;

    public RecruiterCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getTaxId() {
        return taxId;
    }

    public void setTaxId(StringFilter taxId) {
        this.taxId = taxId;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getStreet() {
        return street;
    }

    public void setStreet(StringFilter street) {
        this.street = street;
    }

    public IntegerFilter getNumber() {
        return number;
    }

    public void setNumber(IntegerFilter number) {
        this.number = number;
    }

    public IntegerFilter getFloor() {
        return floor;
    }

    public void setFloor(IntegerFilter floor) {
        this.floor = floor;
    }

    public StringFilter getApartment() {
        return apartment;
    }

    public void setApartment(StringFilter apartment) {
        this.apartment = apartment;
    }

    public LongFilter getCityId() {
        return cityId;
    }

    public void setCityId(LongFilter cityId) {
        this.cityId = cityId;
    }

    public LongFilter getSectorId() {
        return sectorId;
    }

    public void setSectorId(LongFilter sectorId) {
        this.sectorId = sectorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RecruiterCriteria that = (RecruiterCriteria) o;
        return
            Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(email, that.email) &&
                Objects.equals(taxId, that.taxId) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(street, that.street) &&
                Objects.equals(number, that.number) &&
                Objects.equals(floor, that.floor) &&
                Objects.equals(apartment, that.apartment) &&
                Objects.equals(cityId, that.cityId) &&
                Objects.equals(sectorId, that.sectorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            lastName,
            email,
            taxId,
            phone,
            street,
            number,
            floor,
            apartment,
            cityId,
            sectorId
        );
    }

    @Override
    public String toString() {
        return "RecruiterCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (taxId != null ? "taxId=" + taxId + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (street != null ? "street=" + street + ", " : "") +
            (number != null ? "number=" + number + ", " : "") +
            (floor != null ? "floor=" + floor + ", " : "") +
            (apartment != null ? "apartment=" + apartment + ", " : "") +
            (cityId != null ? "cityId=" + cityId + ", " : "") +
            (sectorId != null ? "sectorId=" + sectorId + ", " : "") +
            "}";
    }

}
