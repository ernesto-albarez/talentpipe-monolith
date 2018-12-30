package io.kimos.talentpipe.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.kimos.talentpipe.domain.Role;
import io.kimos.talentpipe.service.RoleService;
import io.kimos.talentpipe.web.rest.errors.BadRequestAlertException;
import io.kimos.talentpipe.web.rest.util.HeaderUtil;
import io.kimos.talentpipe.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Role.
 */
@RestController
@RequestMapping("/api")
public class RoleResource {

    private final Logger log = LoggerFactory.getLogger(RoleResource.class);

    private static final String ENTITY_NAME = "role";

    private final RoleService roleService;

    public RoleResource(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * POST  /roles : Create a new role.
     *
     * @param role the role to create
     * @return the ResponseEntity with status 201 (Created) and with body the new role, or with status 400 (Bad Request) if the role has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/roles")
    @Timed
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) throws URISyntaxException {
        log.debug("REST request to save Role : {}", role);
        if (role.getId() != null) {
            throw new BadRequestAlertException("A new role cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Role result = roleService.save(role);
        return ResponseEntity.created(new URI("/api/roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /roles : Updates an existing role.
     *
     * @param role the role to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated role,
     * or with status 400 (Bad Request) if the role is not valid,
     * or with status 500 (Internal Server Error) if the role couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/roles")
    @Timed
    public ResponseEntity<Role> updateRole(@Valid @RequestBody Role role) throws URISyntaxException {
        log.debug("REST request to update Role : {}", role);
        if (role.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Role result = roleService.save(role);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, role.getId().toString()))
            .body(result);
    }

    /**
     * GET  /roles : get all the roles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of roles in body
     */
    @GetMapping("/roles")
    @Timed
    public ResponseEntity<List<Role>> getAllRoles(Pageable pageable) {
        log.debug("REST request to get a page of Roles");
        Page<Role> page = roleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/roles");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /roles/:id : get the "id" role.
     *
     * @param id the id of the role to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the role, or with status 404 (Not Found)
     */
    @GetMapping("/roles/{id}")
    @Timed
    public ResponseEntity<Role> getRole(@PathVariable Long id) {
        log.debug("REST request to get Role : {}", id);
        Optional<Role> role = roleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(role);
    }

    /**
     * DELETE  /roles/:id : delete the "id" role.
     *
     * @param id the id of the role to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/roles/{id}")
    @Timed
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        log.debug("REST request to delete Role : {}", id);
        roleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/roles?query=:query : search for the role corresponding
     * to the query.
     *
     * @param query the query of the role search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/roles")
    @Timed
    public ResponseEntity<List<Role>> searchRoles(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Roles for query {}", query);
        Page<Role> page = roleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/roles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
