package io.kimos.talentpipe.repository.search;

import io.kimos.talentpipe.domain.SearchStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SearchStatus entity.
 */
public interface SearchStatusSearchRepository extends ElasticsearchRepository<SearchStatus, Long> {
}
