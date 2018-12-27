package io.kimos.talentpipe.web.rest.dto;

import javax.validation.constraints.NotNull;

public class UpdateExpertiseLevelDTO extends CreateAreaDTO {
    @NotNull
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
