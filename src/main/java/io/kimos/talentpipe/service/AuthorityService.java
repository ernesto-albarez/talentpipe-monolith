package io.kimos.talentpipe.service;

import io.kimos.talentpipe.domain.Authority;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Authority.
 */
public interface AuthorityService {

    /**
     * Save a authority.
     *
     * @param authority the entity to save
     * @return the persisted entity
     */
    Authority save(Authority authority);

    /**
     * Get all the authorities.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Authority> findAll(Pageable pageable);


    /**
     * Get the "id" authority.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Authority> findOne(Long id);

    /**
     * Delete the "id" authority.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the authority corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Authority> search(String query, Pageable pageable);
}
