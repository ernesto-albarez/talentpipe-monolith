package io.kimos.talentppe.repository.search;

import io.kimos.talentppe.domain.Recruiter;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Recruiter entity.
 */
public interface RecruiterSearchRepository extends ElasticsearchRepository<Recruiter, Long> {
}
