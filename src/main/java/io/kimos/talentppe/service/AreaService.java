package io.kimos.talentppe.service;

import io.kimos.talentppe.domain.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Area.
 */
public interface AreaService {

    /**
     * Save a area.
     *
     * @param area the entity to save
     * @return the persisted entity
     */
    Area save(Area area);

    /**
     * Get all the areas.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Area> findAll(Pageable pageable);


    /**
     * Get the "id" area.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Area> findOne(Long id);

    /**
     * Delete the "id" area.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the area corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Area> search(String query, Pageable pageable);
}
