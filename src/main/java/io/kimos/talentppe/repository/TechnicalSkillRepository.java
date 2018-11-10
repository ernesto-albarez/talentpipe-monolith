package io.kimos.talentppe.repository;

import io.kimos.talentppe.domain.TechnicalSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TechnicalSkill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TechnicalSkillRepository extends JpaRepository<TechnicalSkill, Long> {

}
