package io.kimos.talentpipe.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SearchStatus.
 */
@Entity
@Table(name = "search_status")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "searchstatus")
public class SearchStatus implements Serializable {

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

    public SearchStatus name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
        this.normalizedName = name.trim().toLowerCase();
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public String getDescription() {
        return description;
    }

    public SearchStatus description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
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
        SearchStatus searchStatus = (SearchStatus) o;
        if (searchStatus.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), searchStatus.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SearchStatus{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", normalizedName='" + getNormalizedName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
