package io.kimos.talentppe.repository.search;

import io.kimos.talentppe.domain.SearchRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SearchRequest entity.
 */
public interface SearchRequestSearchRepository extends ElasticsearchRepository<SearchRequest, Long> {
}
