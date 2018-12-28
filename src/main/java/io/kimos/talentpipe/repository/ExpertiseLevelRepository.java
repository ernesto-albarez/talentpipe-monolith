package io.kimos.talentpipe.repository;

import io.kimos.talentpipe.domain.ExpertiseLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ExpertiseLevel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpertiseLevelRepository extends JpaRepository<ExpertiseLevel, Long> {

}
