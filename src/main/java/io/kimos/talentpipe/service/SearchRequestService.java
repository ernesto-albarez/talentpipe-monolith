package io.kimos.talentpipe.service;

import io.kimos.talentpipe.domain.SearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing SearchRequest.
 */
public interface SearchRequestService {

    /**
     * Save a searchRequest.
     *
     * @param searchRequest the entity to save
     * @return the persisted entity
     */
    SearchRequest save(SearchRequest searchRequest);

    /**
     * Get all the searchRequests.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SearchRequest> findAll(Pageable pageable);

    /**
     * Get all the SearchRequest with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<SearchRequest> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" searchRequest.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SearchRequest> findOne(Long id);

    /**
     * Delete the "id" searchRequest.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the searchRequest corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SearchRequest> search(String query, Pageable pageable);
}
