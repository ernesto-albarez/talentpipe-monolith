package io.kimos.talentpipe.service;

import io.kimos.talentpipe.domain.CompanyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing CompanyType.
 */
public interface CompanyTypeService {

    /**
     * Save a companyType.
     *
     * @param companyType the entity to save
     * @return the persisted entity
     */
    CompanyType save(CompanyType companyType);

    /**
     * Get all the companyTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CompanyType> findAll(Pageable pageable);


    /**
     * Get the "id" companyType.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CompanyType> findOne(Long id);

    /**
     * Delete the "id" companyType.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the companyType corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CompanyType> search(String query, Pageable pageable);
}
