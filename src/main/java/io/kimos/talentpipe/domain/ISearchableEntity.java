package io.kimos.talentpipe.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface ISearchableEntity {
    @JsonProperty(value = "searcheableString")
    default String getNormalizedSearchableString() {
        return this.getSearcheableString();
    }

    String getSearcheableString();

}
