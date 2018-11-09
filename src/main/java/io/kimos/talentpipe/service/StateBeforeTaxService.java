package io.kimos.talentpipe.service;

import io.kimos.talentpipe.domain.StateBeforeTax;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing StateBeforeTax.
 */
public interface StateBeforeTaxService {

    /**
     * Save a stateBeforeTax.
     *
     * @param stateBeforeTax the entity to save
     * @return the persisted entity
     */
    StateBeforeTax save(StateBeforeTax stateBeforeTax);

    /**
     * Get all the stateBeforeTaxes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StateBeforeTax> findAll(Pageable pageable);


    /**
     * Get the "id" stateBeforeTax.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<StateBeforeTax> findOne(Long id);

    /**
     * Delete the "id" stateBeforeTax.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the stateBeforeTax corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StateBeforeTax> search(String query, Pageable pageable);
}
