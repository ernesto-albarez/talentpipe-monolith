package io.kimos.talentppe.repository.search;

import io.kimos.talentppe.domain.TechnicalSkill;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TechnicalSkill entity.
 */
public interface TechnicalSkillSearchRepository extends ElasticsearchRepository<TechnicalSkill, Long> {
}
