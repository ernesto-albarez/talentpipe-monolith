package io.kimos.talentpipe.service;

import io.kimos.talentpipe.domain.TechnicalSkill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing TechnicalSkill.
 */
public interface TechnicalSkillService {

    /**
     * Save a technicalSkill.
     *
     * @param technicalSkill the entity to save
     * @return the persisted entity
     */
    TechnicalSkill save(TechnicalSkill technicalSkill);

    /**
     * Get all the technicalSkills.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TechnicalSkill> findAll(Pageable pageable);

    /**
     * Get the "id" technicalSkill.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TechnicalSkill> findOne(Long id);

    /**
     * Delete the "id" technicalSkill.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the technicalSkill corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TechnicalSkill> search(String query, Pageable pageable);
}
