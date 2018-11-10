package io.kimos.talentppe.repository.search;

import io.kimos.talentppe.domain.SearchType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SearchType entity.
 */
public interface SearchTypeSearchRepository extends ElasticsearchRepository<SearchType, Long> {
}
