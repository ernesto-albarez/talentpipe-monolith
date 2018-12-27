package io.kimos.talentpipe.service;

import io.kimos.talentpipe.domain.ExpertiseLevel;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing ExpertiseLevel.
 */
public interface ExpertiseLevelService {

    /**
     * Save a expertiseLevel.
     *
     * @param expertiseLevel the entity to save
     * @return the persisted entity
     */
    ExpertiseLevel save(ExpertiseLevel expertiseLevel);

    /**
     * Get all the expertiseLevels.
     *
     * @return the list of entities
     */
    List<ExpertiseLevel> findAll();


    /**
     * Get the "id" expertiseLevel.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ExpertiseLevel> findOne(Long id);

    /**
     * Delete the "id" expertiseLevel.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the expertiseLevel corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<ExpertiseLevel> search(String query);
}
