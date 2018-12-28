package io.kimos.talentpipe.repository;

import io.kimos.talentpipe.domain.SearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the SearchRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SearchRequestRepository extends JpaRepository<SearchRequest, Long> {

    @Query(value = "select distinct search_request from SearchRequest search_request left join fetch search_request.requiredTechnicalSkills left join fetch search_request.nonRequiredTechnicalSkills left join fetch search_request.requiredSoftSkills left join fetch search_request.nonRequiredSoftSkills left join fetch search_request.benefits",
        countQuery = "select count(distinct search_request) from SearchRequest search_request")
    Page<SearchRequest> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct search_request from SearchRequest search_request left join fetch search_request.requiredTechnicalSkills left join fetch search_request.nonRequiredTechnicalSkills left join fetch search_request.requiredSoftSkills left join fetch search_request.nonRequiredSoftSkills left join fetch search_request.benefits")
    List<SearchRequest> findAllWithEagerRelationships();

    @Query("select search_request from SearchRequest search_request left join fetch search_request.requiredTechnicalSkills left join fetch search_request.nonRequiredTechnicalSkills left join fetch search_request.requiredSoftSkills left join fetch search_request.nonRequiredSoftSkills left join fetch search_request.benefits where search_request.id =:id")
    Optional<SearchRequest> findOneWithEagerRelationships(@Param("id") Long id);

}
