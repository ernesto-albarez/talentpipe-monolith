package io.kimos.talentpipe.service;

import io.kimos.talentpipe.domain.Sector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Sector.
 */
public interface SectorService {

    /**
     * Save a sector.
     *
     * @param sector the entity to save
     * @return the persisted entity
     */
    Sector save(Sector sector);

    /**
     * Get all the sectors.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Sector> findAll(Pageable pageable);


    /**
     * Get the "id" sector.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Sector> findOne(Long id);

    /**
     * Delete the "id" sector.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the sector corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Sector> search(String query, Pageable pageable);
}
