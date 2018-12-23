package io.kimos.talentpipe.web.rest.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CreateCompanyTypeDTO {
    @NotNull
    private String name;
    private String description;
    @NotNull
    @Min(value = 0)
    private Integer minEmployeesQuantity;
    private Integer maxEmployeesQuantity;

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

    public Integer getMinEmployeesQuantity() {
        return minEmployeesQuantity;
    }

    public void setMinEmployeesQuantity(Integer minEmployeesQuantity) {
        this.minEmployeesQuantity = minEmployeesQuantity;
    }

    public Integer getMaxEmployeesQuantity() {
        return maxEmployeesQuantity;
    }

    public void setMaxEmployeesQuantity(Integer maxEmployeesQuantity) {
        this.maxEmployeesQuantity = maxEmployeesQuantity;
    }
}
