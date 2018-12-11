package io.kimos.talentppe.web.rest.dto;

import javax.validation.constraints.NotNull;

public class UpdateWorkTypeDTO extends CreateWorkTypeDTO {
    @NotNull
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
