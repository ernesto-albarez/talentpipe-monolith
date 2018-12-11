package io.kimos.talentppe.web.rest.dto;

import javax.validation.constraints.NotNull;

public class UpdateSectorDTO extends CreateSectorDTO {
    @NotNull
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
