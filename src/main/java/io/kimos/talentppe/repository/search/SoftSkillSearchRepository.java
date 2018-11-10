package io.kimos.talentppe.repository.search;

import io.kimos.talentppe.domain.SoftSkill;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SoftSkill entity.
 */
public interface SoftSkillSearchRepository extends ElasticsearchRepository<SoftSkill, Long> {
}
