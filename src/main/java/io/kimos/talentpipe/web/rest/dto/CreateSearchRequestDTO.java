package io.kimos.talentpipe.web.rest.dto;

import io.kimos.talentpipe.domain.Benefit;
import io.kimos.talentpipe.domain.ExpertiseLevel;
import io.kimos.talentpipe.domain.SoftSkill;
import io.kimos.talentpipe.domain.TechnicalSkill;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class CreateSearchRequestDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Size(max = 2048)
    private String description;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal minSalary;

    private BigDecimal maxSalary;

    @NotNull
    private String position;
    private ExpertiseLevel expertiseLevel;
    private Set<TechnicalSkill> requiredTechnicalSkills = new HashSet<>();
    private Set<TechnicalSkill> nonRequiredTechnicalSkills = new HashSet<>();
    private Set<SoftSkill> requiredSoftSkills = new HashSet<>();
    private Set<SoftSkill> nonRequiredSoftSkills = new HashSet<>();
    private Set<Benefit> benefits = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(BigDecimal minSalary) {
        this.minSalary = minSalary;
    }

    public BigDecimal getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(BigDecimal maxSalary) {
        this.maxSalary = maxSalary;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public ExpertiseLevel getExpertiseLevel() {
        return expertiseLevel;
    }

    public void setExpertiseLevel(ExpertiseLevel expertiseLevel) {
        this.expertiseLevel = expertiseLevel;
    }

    public Set<TechnicalSkill> getRequiredTechnicalSkills() {
        return requiredTechnicalSkills;
    }

    public void setRequiredTechnicalSkills(Set<TechnicalSkill> requiredTechnicalSkills) {
        this.requiredTechnicalSkills = requiredTechnicalSkills;
    }

    public Set<TechnicalSkill> getNonRequiredTechnicalSkills() {
        return nonRequiredTechnicalSkills;
    }

    public void setNonRequiredTechnicalSkills(Set<TechnicalSkill> nonRequiredTechnicalSkills) {
        this.nonRequiredTechnicalSkills = nonRequiredTechnicalSkills;
    }

    public Set<SoftSkill> getRequiredSoftSkills() {
        return requiredSoftSkills;
    }

    public void setRequiredSoftSkills(Set<SoftSkill> requiredSoftSkills) {
        this.requiredSoftSkills = requiredSoftSkills;
    }

    public Set<SoftSkill> getNonRequiredSoftSkills() {
        return nonRequiredSoftSkills;
    }

    public void setNonRequiredSoftSkills(Set<SoftSkill> nonRequiredSoftSkills) {
        this.nonRequiredSoftSkills = nonRequiredSoftSkills;
    }

    public Set<Benefit> getBenefits() {
        return benefits;
    }

    public void setBenefits(Set<Benefit> benefits) {
        this.benefits = benefits;
    }
}
