package io.kimos.talentpipe.repository.search;

import io.kimos.talentpipe.domain.ExpertiseLevel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ExpertiseLevel entity.
 */
public interface ExpertiseLevelSearchRepository extends ElasticsearchRepository<ExpertiseLevel, Long> {
}
