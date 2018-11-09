package io.kimos.talentpipe.repository;

import io.kimos.talentpipe.domain.StateBeforeTax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StateBeforeTax entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StateBeforeTaxRepository extends JpaRepository<StateBeforeTax, Long> {

}
