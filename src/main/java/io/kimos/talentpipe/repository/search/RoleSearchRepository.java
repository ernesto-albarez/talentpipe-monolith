package io.kimos.talentpipe.repository.search;

import io.kimos.talentpipe.domain.Role;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Role entity.
 */
public interface RoleSearchRepository extends ElasticsearchRepository<Role, Long> {
}
