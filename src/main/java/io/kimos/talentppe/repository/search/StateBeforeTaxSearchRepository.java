package io.kimos.talentppe.repository.search;

import io.kimos.talentppe.domain.StateBeforeTax;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StateBeforeTax entity.
 */
public interface StateBeforeTaxSearchRepository extends ElasticsearchRepository<StateBeforeTax, Long> {
}
