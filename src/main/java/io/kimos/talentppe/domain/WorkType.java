package io.kimos.talentppe.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A WorkType.
 */
@Entity
@Table(name = "work_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "worktype")
public class WorkType implements Serializable {

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
    @Column(name = "min_quantity_hours", nullable = false)
    private Integer minQuantityHours;

    @NotNull
    @Max(value = 24)
    @Column(name = "max_quantity_hours", nullable = false)
    private Integer maxQuantityHours;

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

    public WorkType name(String name) {
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

    public WorkType description(String description) {
        this.description = description;
        return this;
    }

    public Integer getMinQuantityHours() {
        return minQuantityHours;
    }

    public void setMinQuantityHours(Integer minQuantityHours) {
        this.minQuantityHours = minQuantityHours;
    }

    public WorkType minQuantityHours(Integer minQuantityHours) {
        this.minQuantityHours = minQuantityHours;
        return this;
    }

    public Integer getMaxQuantityHours() {
        return maxQuantityHours;
    }

    public void setMaxQuantityHours(Integer maxQuantityHours) {
        this.maxQuantityHours = maxQuantityHours;
    }

    public WorkType maxQuantityHours(Integer maxQuantityHours) {
        this.maxQuantityHours = maxQuantityHours;
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
        WorkType workType = (WorkType) o;
        if (workType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), workType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WorkType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", normalizedName='" + getNormalizedName() + "'" +
            ", description='" + getDescription() + "'" +
            ", minQuantityHours=" + getMinQuantityHours() +
            ", maxQuantityHours=" + getMaxQuantityHours() +
            "}";
    }
}
