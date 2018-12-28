package io.kimos.talentpipe.service;

import io.kimos.talentpipe.domain.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing SearchType.
 */
public interface SearchTypeService {

    /**
     * Save a searchType.
     *
     * @param searchType the entity to save
     * @return the persisted entity
     */
    SearchType save(SearchType searchType);

    /**
     * Get all the searchTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SearchType> findAll(Pageable pageable);

    /**
     * Get the "id" searchType.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SearchType> findOne(Long id);

    /**
     * Delete the "id" searchType.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the searchType corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SearchType> search(String query, Pageable pageable);
}
