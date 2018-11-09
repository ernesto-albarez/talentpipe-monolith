package io.kimos.talentpipe.repository.search;

import io.kimos.talentpipe.domain.Benefit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Benefit entity.
 */
public interface BenefitSearchRepository extends ElasticsearchRepository<Benefit, Long> {
}
