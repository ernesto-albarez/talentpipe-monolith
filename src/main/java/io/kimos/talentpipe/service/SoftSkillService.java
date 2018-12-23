package io.kimos.talentpipe.service;

import io.kimos.talentpipe.domain.SoftSkill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing SoftSkill.
 */
public interface SoftSkillService {

    /**
     * Save a softSkill.
     *
     * @param softSkill the entity to save
     * @return the persisted entity
     */
    SoftSkill save(SoftSkill softSkill);

    /**
     * Get all the softSkills.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SoftSkill> findAll(Pageable pageable);


    /**
     * Get the "id" softSkill.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SoftSkill> findOne(Long id);

    /**
     * Delete the "id" softSkill.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the softSkill corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SoftSkill> search(String query, Pageable pageable);
}
