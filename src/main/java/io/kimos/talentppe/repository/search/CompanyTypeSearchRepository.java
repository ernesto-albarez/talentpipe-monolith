package io.kimos.talentppe.repository.search;

import io.kimos.talentppe.domain.CompanyType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CompanyType entity.
 */
public interface CompanyTypeSearchRepository extends ElasticsearchRepository<CompanyType, Long> {
}
