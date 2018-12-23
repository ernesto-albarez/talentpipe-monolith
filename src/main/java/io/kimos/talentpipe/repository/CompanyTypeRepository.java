package io.kimos.talentpipe.repository;

import io.kimos.talentpipe.domain.CompanyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CompanyType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyTypeRepository extends JpaRepository<CompanyType, Long> {

}
