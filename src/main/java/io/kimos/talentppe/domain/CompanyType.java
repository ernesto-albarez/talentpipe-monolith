package io.kimos.talentppe.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CompanyType.
 */
@Entity
@Table(name = "company_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "companytype")
public class CompanyType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "normalized_name", nullable = false)
    private String normalizedName;

    @Column(name = "description")
    private String description;

    @NotNull
    @Min(value = 0)
    @Column(name = "min_employees_quantity", nullable = false)
    private Integer minEmployeesQuantity;

    @Column(name = "max_employees_quantity")
    private Integer maxEmployeesQuantity;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.normalizedName = name.trim().toLowerCase();
    }

    public CompanyType name(String name) {
        this.setName(name);
        return this;
    }
    public String getNormalizedName() {
        return normalizedName;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CompanyType description(String description) {
        this.description = description;
        return this;
    }

    public Integer getMinEmployeesQuantity() {
        return minEmployeesQuantity;
    }

    public void setMinEmployeesQuantity(Integer minEmployeesQuantity) {
        this.minEmployeesQuantity = minEmployeesQuantity;
    }

    public CompanyType minEmployeesQuantity(Integer minEmployeesQuantity) {
        this.minEmployeesQuantity = minEmployeesQuantity;
        return this;
    }

    public Integer getMaxEmployeesQuantity() {
        return maxEmployeesQuantity;
    }

    public void setMaxEmployeesQuantity(Integer maxEmployeesQuantity) {
        this.maxEmployeesQuantity = maxEmployeesQuantity;
    }

    public CompanyType maxEmployeesQuantity(Integer maxEmployeesQuantity) {
        this.maxEmployeesQuantity = maxEmployeesQuantity;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CompanyType companyType = (CompanyType) o;
        if (companyType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), companyType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CompanyType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", normalizedName='" + getNormalizedName() + "'" +
            ", description='" + getDescription() + "'" +
            ", minEmployeesQuantity=" + getMinEmployeesQuantity() +
            ", maxEmployeesQuantity=" + getMaxEmployeesQuantity() +
            "}";
    }
}
