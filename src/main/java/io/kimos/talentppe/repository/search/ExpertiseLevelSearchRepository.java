package io.kimos.talentppe.repository.search;

import io.kimos.talentppe.domain.ExpertiseLevel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ExpertiseLevel entity.
 */
public interface ExpertiseLevelSearchRepository extends ElasticsearchRepository<ExpertiseLevel, Long> {
}
