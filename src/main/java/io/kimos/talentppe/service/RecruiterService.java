package io.kimos.talentppe.service;

import io.kimos.talentppe.domain.Recruiter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Recruiter.
 */
public interface RecruiterService {

    /**
     * Save a recruiter.
     *
     * @param recruiter the entity to save
     * @return the persisted entity
     */
    Recruiter save(Recruiter recruiter);

    /**
     * Get all the recruiters.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Recruiter> findAll(Pageable pageable);


    /**
     * Get the "id" recruiter.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Recruiter> findOne(Long id);

    /**
     * Delete the "id" recruiter.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the recruiter corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Recruiter> search(String query, Pageable pageable);
}
