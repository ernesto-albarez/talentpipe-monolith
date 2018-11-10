package io.kimos.talentppe.repository;

import io.kimos.talentppe.domain.Benefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Benefit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BenefitRepository extends JpaRepository<Benefit, Long> {

}
