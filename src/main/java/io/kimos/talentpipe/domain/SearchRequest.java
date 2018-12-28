package io.kimos.talentpipe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A SearchRequest.
 */
@Entity
@Table(name = "search_request")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "searchrequest")
public class SearchRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Size(max = 2048)
    @Column(name = "description", length = 2048, nullable = false)
    private String description;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "min_salary", precision = 10, scale = 2, nullable = false)
    private BigDecimal minSalary;

    @Column(name = "max_salary", precision = 10, scale = 2)
    private BigDecimal maxSalary;

    @NotNull
    @Column(name = "position", nullable = false)
    private String position;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Company company;

    @ManyToOne
    @JsonIgnoreProperties("")
    private ExpertiseLevel expertiseLevel;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "search_request_required_technical_skills",
        joinColumns = @JoinColumn(name = "search_requests_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "required_technical_skills_id", referencedColumnName = "id"))
    private Set<TechnicalSkill> requiredTechnicalSkills = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "search_request_non_required_technical_skills",
        joinColumns = @JoinColumn(name = "search_requests_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "non_required_technical_skills_id", referencedColumnName = "id"))
    private Set<TechnicalSkill> nonRequiredTechnicalSkills = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "search_request_required_soft_skills",
        joinColumns = @JoinColumn(name = "search_requests_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "required_soft_skills_id", referencedColumnName = "id"))
    private Set<SoftSkill> requiredSoftSkills = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "search_request_non_required_soft_skills",
        joinColumns = @JoinColumn(name = "search_requests_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "non_required_soft_skills_id", referencedColumnName = "id"))
    private Set<SoftSkill> nonRequiredSoftSkills = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "search_request_benefits",
        joinColumns = @JoinColumn(name = "search_requests_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "benefits_id", referencedColumnName = "id"))
    private Set<Benefit> benefits = new HashSet<>();

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

    public SearchRequest name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public SearchRequest description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMinSalary() {
        return minSalary;
    }

    public SearchRequest minSalary(BigDecimal minSalary) {
        this.minSalary = minSalary;
        return this;
    }

    public void setMinSalary(BigDecimal minSalary) {
        this.minSalary = minSalary;
    }

    public BigDecimal getMaxSalary() {
        return maxSalary;
    }

    public SearchRequest maxSalary(BigDecimal maxSalary) {
        this.maxSalary = maxSalary;
        return this;
    }

    public void setMaxSalary(BigDecimal maxSalary) {
        this.maxSalary = maxSalary;
    }

    public String getPosition() {
        return position;
    }

    public SearchRequest position(String position) {
        this.position = position;
        return this;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Company getCompany() {
        return company;
    }

    public SearchRequest company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ExpertiseLevel getExpertiseLevel() {
        return expertiseLevel;
    }

    public SearchRequest expertiseLevel(ExpertiseLevel expertiseLevel) {
        this.expertiseLevel = expertiseLevel;
        return this;
    }

    public void setExpertiseLevel(ExpertiseLevel expertiseLevel) {
        this.expertiseLevel = expertiseLevel;
    }

    public Set<TechnicalSkill> getRequiredTechnicalSkills() {
        return requiredTechnicalSkills;
    }

    public SearchRequest requiredTechnicalSkills(Set<TechnicalSkill> technicalSkills) {
        this.requiredTechnicalSkills = technicalSkills;
        return this;
    }

    public SearchRequest addRequiredTechnicalSkills(TechnicalSkill technicalSkill) {
        this.requiredTechnicalSkills.add(technicalSkill);
        return this;
    }

    public SearchRequest removeRequiredTechnicalSkills(TechnicalSkill technicalSkill) {
        this.requiredTechnicalSkills.remove(technicalSkill);
        return this;
    }

    public void setRequiredTechnicalSkills(Set<TechnicalSkill> technicalSkills) {
        this.requiredTechnicalSkills = technicalSkills;
    }

    public Set<TechnicalSkill> getNonRequiredTechnicalSkills() {
        return nonRequiredTechnicalSkills;
    }

    public SearchRequest nonRequiredTechnicalSkills(Set<TechnicalSkill> technicalSkills) {
        this.nonRequiredTechnicalSkills = technicalSkills;
        return this;
    }

    public SearchRequest addNonRequiredTechnicalSkills(TechnicalSkill technicalSkill) {
        this.nonRequiredTechnicalSkills.add(technicalSkill);
        return this;
    }

    public SearchRequest removeNonRequiredTechnicalSkills(TechnicalSkill technicalSkill) {
        this.nonRequiredTechnicalSkills.remove(technicalSkill);
        return this;
    }

    public void setNonRequiredTechnicalSkills(Set<TechnicalSkill> technicalSkills) {
        this.nonRequiredTechnicalSkills = technicalSkills;
    }

    public Set<SoftSkill> getRequiredSoftSkills() {
        return requiredSoftSkills;
    }

    public SearchRequest requiredSoftSkills(Set<SoftSkill> SoftSkills) {
        this.requiredSoftSkills = SoftSkills;
        return this;
    }

    public SearchRequest addRequiredSoftSkills(SoftSkill SoftSkill) {
        this.requiredSoftSkills.add(SoftSkill);
        return this;
    }

    public SearchRequest removeRequiredSoftSkills(SoftSkill SoftSkill) {
        this.requiredSoftSkills.remove(SoftSkill);
        return this;
    }

    public void setRequiredSoftSkills(Set<SoftSkill> SoftSkills) {
        this.requiredSoftSkills = SoftSkills;
    }

    public Set<SoftSkill> getNonRequiredSoftSkills() {
        return nonRequiredSoftSkills;
    }

    public SearchRequest nonRequiredSoftSkills(Set<SoftSkill> softSkills) {
        this.nonRequiredSoftSkills = softSkills;
        return this;
    }

    public SearchRequest addNonRequiredSoftSkills(SoftSkill softSkill) {
        this.nonRequiredSoftSkills.add(softSkill);
        return this;
    }

    public SearchRequest removeNonRequiredSoftSkills(SoftSkill softSkill) {
        this.nonRequiredSoftSkills.remove(softSkill);
        return this;
    }

    public void setNonRequiredSoftSkills(Set<SoftSkill> softSkills) {
        this.nonRequiredSoftSkills = softSkills;
    }

    public Set<Benefit> getBenefits() {
        return benefits;
    }

    public SearchRequest benefits(Set<Benefit> Benefits) {
        this.benefits = Benefits;
        return this;
    }

    public SearchRequest addBenefits(Benefit Benefit) {
        this.benefits.add(Benefit);
        return this;
    }

    public SearchRequest removeBenefits(Benefit Benefit) {
        this.benefits.remove(Benefit);
        return this;
    }

    public void setBenefits(Set<Benefit> Benefits) {
        this.benefits = Benefits;
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
        SearchRequest searchRequest = (SearchRequest) o;
        if (searchRequest.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), searchRequest.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SearchRequest{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", minSalary=" + getMinSalary() +
            ", maxSalary=" + getMaxSalary() +
            ", position='" + getPosition() + "'" +
            "}";
    }
}
