package io.kimos.talentpipe.repository;

import io.kimos.talentpipe.domain.TechnicalSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TechnicalSkill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TechnicalSkillRepository extends JpaRepository<TechnicalSkill, Long> {

}
