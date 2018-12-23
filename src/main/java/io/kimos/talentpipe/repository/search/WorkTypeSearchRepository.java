package io.kimos.talentpipe.repository.search;

import io.kimos.talentpipe.domain.WorkType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the WorkType entity.
 */
public interface WorkTypeSearchRepository extends ElasticsearchRepository<WorkType, Long> {
}
