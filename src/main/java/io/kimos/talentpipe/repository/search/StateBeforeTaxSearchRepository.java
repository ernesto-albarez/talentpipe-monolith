package io.kimos.talentpipe.repository.search;

import io.kimos.talentpipe.domain.StateBeforeTax;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StateBeforeTax entity.
 */
public interface StateBeforeTaxSearchRepository extends ElasticsearchRepository<StateBeforeTax, Long> {
}
