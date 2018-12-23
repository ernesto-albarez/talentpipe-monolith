package io.kimos.talentpipe.repository;

import io.kimos.talentpipe.domain.Recruiter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Recruiter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter, Long>, JpaSpecificationExecutor<Recruiter> {

}
