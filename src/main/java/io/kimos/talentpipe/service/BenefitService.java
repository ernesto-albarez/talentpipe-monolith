package io.kimos.talentpipe.service;

import io.kimos.talentpipe.domain.Benefit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Benefit.
 */
public interface BenefitService {

    /**
     * Save a benefit.
     *
     * @param benefit the entity to save
     * @return the persisted entity
     */
    Benefit save(Benefit benefit);

    /**
     * Get all the benefits.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Benefit> findAll(Pageable pageable);

    /**
     * Get the "id" benefit.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Benefit> findOne(Long id);

    /**
     * Delete the "id" benefit.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the benefit corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Benefit> search(String query, Pageable pageable);
}
