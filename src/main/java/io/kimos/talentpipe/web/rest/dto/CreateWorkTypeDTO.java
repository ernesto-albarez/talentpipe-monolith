package io.kimos.talentpipe.web.rest.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateWorkTypeDTO {
    @NotNull
    @NotBlank
    private String name;
    private String description;
    @NotNull
    @Min(value = 0)
    private Integer minQuantityHours;

    @NotNull
    @Max(value = 24)
    private Integer maxQuantityHours;
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

    public Integer getMinQuantityHours() {
        return minQuantityHours;
    }

    public void setMinQuantityHours(Integer minQuantityHours) {
        this.minQuantityHours = minQuantityHours;
    }

    public Integer getMaxQuantityHours() {
        return maxQuantityHours;
    }

    public void setMaxQuantityHours(Integer maxQuantityHours) {
        this.maxQuantityHours = maxQuantityHours;
    }
}
