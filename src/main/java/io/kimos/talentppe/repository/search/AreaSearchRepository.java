package io.kimos.talentppe.repository.search;

import io.kimos.talentppe.domain.Area;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Area entity.
 */
public interface AreaSearchRepository extends ElasticsearchRepository<Area, Long> {
}
