package io.kimos.talentpipe.service;

import io.kimos.talentpipe.domain.Role;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Role.
 */
public interface RoleService {

    /**
     * Save a role.
     *
     * @param role the entity to save
     * @return the persisted entity
     */
    Role save(Role role);

    /**
     * Get all the roles.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Role> findAll(Pageable pageable);


    /**
     * Get the "id" role.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Role> findOne(Long id);

    /**
     * Delete the "id" role.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the role corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Role> search(String query, Pageable pageable);

    Optional<Role> findByName(String roleName);
}
