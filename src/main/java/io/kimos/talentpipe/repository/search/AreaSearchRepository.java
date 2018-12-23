package io.kimos.talentpipe.repository.search;

import io.kimos.talentpipe.domain.Area;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Area entity.
 */
public interface AreaSearchRepository extends ElasticsearchRepository<Area, Long> {
}
