package io.kimos.talentppe.repository.search;

import io.kimos.talentppe.domain.Benefit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Benefit entity.
 */
public interface BenefitSearchRepository extends ElasticsearchRepository<Benefit, Long> {
}
