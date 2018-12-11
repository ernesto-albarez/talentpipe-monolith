package io.kimos.talentppe.web.rest.dto;

import javax.validation.constraints.NotNull;

public class UpdateTechnicalSkillDTO extends CreateSoftSkillDTO {
    @NotNull
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
