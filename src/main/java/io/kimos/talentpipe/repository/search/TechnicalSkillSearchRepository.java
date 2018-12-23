package io.kimos.talentpipe.repository.search;

import io.kimos.talentpipe.domain.TechnicalSkill;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TechnicalSkill entity.
 */
public interface TechnicalSkillSearchRepository extends ElasticsearchRepository<TechnicalSkill, Long> {
}
