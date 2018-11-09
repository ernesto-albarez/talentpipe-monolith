package io.kimos.talentpipe.repository.search;

import io.kimos.talentpipe.domain.SearchType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SearchType entity.
 */
public interface SearchTypeSearchRepository extends ElasticsearchRepository<SearchType, Long> {
}
