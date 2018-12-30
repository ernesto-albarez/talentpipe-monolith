package io.kimos.talentpipe.repository.search;

import io.kimos.talentpipe.domain.Authority;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Authority entity.
 */
public interface AuthoritySearchRepository extends ElasticsearchRepository<Authority, Long> {
}
