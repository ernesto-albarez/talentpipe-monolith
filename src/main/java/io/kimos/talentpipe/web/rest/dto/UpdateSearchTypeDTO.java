package io.kimos.talentpipe.web.rest.dto;

import javax.validation.constraints.NotNull;

public class UpdateSearchTypeDTO extends CreateSearchTypeDTO {

    @NotNull
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
