package io.kimos.talentppe.repository;

import io.kimos.talentppe.domain.CompanyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CompanyType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyTypeRepository extends JpaRepository<CompanyType, Long> {

}
