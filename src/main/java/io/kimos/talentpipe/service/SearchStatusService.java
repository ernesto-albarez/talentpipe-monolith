package io.kimos.talentpipe.service;

import io.kimos.talentpipe.domain.SearchStatus;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing SearchStatus.
 */
public interface SearchStatusService {

    /**
     * Save a searchStatus.
     *
     * @param searchStatus the entity to save
     * @return the persisted entity
     */
    SearchStatus save(SearchStatus searchStatus);

    /**
     * Get all the searchStatuses.
     *
     * @return the list of entities
     */
    List<SearchStatus> findAll();


    /**
     * Get the "id" searchStatus.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SearchStatus> findOne(Long id);

    /**
     * Delete the "id" searchStatus.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the searchStatus corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<SearchStatus> search(String query);
}
