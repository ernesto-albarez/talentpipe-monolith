package io.kimos.talentpipe.service;

import io.kimos.talentpipe.domain.WorkType;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing WorkType.
 */
public interface WorkTypeService {

    /**
     * Save a workType.
     *
     * @param workType the entity to save
     * @return the persisted entity
     */
    WorkType save(WorkType workType);

    /**
     * Get all the workTypes.
     *
     * @return the list of entities
     */
    List<WorkType> findAll();

    /**
     * Get the "id" workType.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<WorkType> findOne(Long id);

    /**
     * Delete the "id" workType.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the workType corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    List<WorkType> search(String query);
}
