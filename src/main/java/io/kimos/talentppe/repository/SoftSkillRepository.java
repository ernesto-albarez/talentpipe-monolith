package io.kimos.talentppe.repository;

import io.kimos.talentppe.domain.SoftSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SoftSkill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SoftSkillRepository extends JpaRepository<SoftSkill, Long> {

}
