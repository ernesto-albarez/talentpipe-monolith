package io.kimos.talentppe.repository.search;

import io.kimos.talentppe.domain.WorkType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the WorkType entity.
 */
public interface WorkTypeSearchRepository extends ElasticsearchRepository<WorkType, Long> {
}
