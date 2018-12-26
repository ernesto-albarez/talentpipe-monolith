package io.kimos.talentppe.repository.search;

import io.kimos.talentppe.domain.SearchStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SearchStatus entity.
 */
public interface SearchStatusSearchRepository extends ElasticsearchRepository<SearchStatus, Long> {
}
