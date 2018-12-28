package io.kimos.talentpipe.repository;

import io.kimos.talentpipe.domain.SearchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SearchStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SearchStatusRepository extends JpaRepository<SearchStatus, Long> {

}
