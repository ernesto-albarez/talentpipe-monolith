package io.kimos.talentpipe.repository.search;

import io.kimos.talentpipe.domain.CompanyType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CompanyType entity.
 */
public interface CompanyTypeSearchRepository extends ElasticsearchRepository<CompanyType, Long> {
}
