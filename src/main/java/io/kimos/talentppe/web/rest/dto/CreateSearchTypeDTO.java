package io.kimos.talentppe.web.rest.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateSearchTypeDTO {
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String normalizedName;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
