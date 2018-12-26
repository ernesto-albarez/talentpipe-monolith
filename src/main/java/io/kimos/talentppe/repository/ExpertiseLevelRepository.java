package io.kimos.talentppe.repository;

import io.kimos.talentppe.domain.ExpertiseLevel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ExpertiseLevel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpertiseLevelRepository extends JpaRepository<ExpertiseLevel, Long> {

}
