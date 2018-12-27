package io.kimos.talentpipe.repository.search;

import io.kimos.talentpipe.domain.SearchRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SearchRequest entity.
 */
public interface SearchRequestSearchRepository extends ElasticsearchRepository<SearchRequest, Long> {
}
