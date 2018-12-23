package io.kimos.talentpipe.repository.search;

import io.kimos.talentpipe.domain.Recruiter;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Recruiter entity.
 */
public interface RecruiterSearchRepository extends ElasticsearchRepository<Recruiter, Long> {
}
