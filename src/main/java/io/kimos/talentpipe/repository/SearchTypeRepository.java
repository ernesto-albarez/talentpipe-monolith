package io.kimos.talentpipe.repository;

import io.kimos.talentpipe.domain.SearchType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SearchType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SearchTypeRepository extends JpaRepository<SearchType, Long> {

}
