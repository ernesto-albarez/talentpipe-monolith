package io.kimos.talentpipe.repository;

import io.kimos.talentpipe.domain.WorkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the WorkType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkTypeRepository extends JpaRepository<WorkType, Long> {

}
