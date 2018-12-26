package io.kimos.talentppe.repository;

import io.kimos.talentppe.domain.SearchStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SearchStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SearchStatusRepository extends JpaRepository<SearchStatus, Long> {

}
