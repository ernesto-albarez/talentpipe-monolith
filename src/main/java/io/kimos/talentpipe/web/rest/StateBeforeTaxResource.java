package io.kimos.talentpipe.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.kimos.talentpipe.domain.StateBeforeTax;
import io.kimos.talentpipe.service.StateBeforeTaxService;
import io.kimos.talentpipe.web.rest.errors.BadRequestAlertException;
import io.kimos.talentpipe.web.rest.util.HeaderUtil;
import io.kimos.talentpipe.web.rest.util.PaginationUtil;
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

/**
 * REST controller for managing StateBeforeTax.
 */
@RestController
@RequestMapping("/api")
public class StateBeforeTaxResource {

    private static final String ENTITY_NAME = "stateBeforeTax";
    private final Logger log = LoggerFactory.getLogger(StateBeforeTaxResource.class);
    private final StateBeforeTaxService stateBeforeTaxService;

    public StateBeforeTaxResource(StateBeforeTaxService stateBeforeTaxService) {
        this.stateBeforeTaxService = stateBeforeTaxService;
    }

    /**
     * POST  /state-before-taxes : Create a new stateBeforeTax.
     *
     * @param stateBeforeTax the stateBeforeTax to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stateBeforeTax, or with status 400 (Bad Request) if the stateBeforeTax has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/state-before-taxes")
    @Timed
    public ResponseEntity<StateBeforeTax> createStateBeforeTax(@Valid @RequestBody StateBeforeTax stateBeforeTax) throws URISyntaxException {
        log.debug("REST request to save StateBeforeTax : {}", stateBeforeTax);
        if (stateBeforeTax.getId() != null) {
            throw new BadRequestAlertException("A new stateBeforeTax cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StateBeforeTax result = stateBeforeTaxService.save(stateBeforeTax);
        return ResponseEntity.created(new URI("/api/state-before-taxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /state-before-taxes : Updates an existing stateBeforeTax.
     *
     * @param stateBeforeTax the stateBeforeTax to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stateBeforeTax,
     * or with status 400 (Bad Request) if the stateBeforeTax is not valid,
     * or with status 500 (Internal Server Error) if the stateBeforeTax couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/state-before-taxes")
    @Timed
    public ResponseEntity<StateBeforeTax> updateStateBeforeTax(@Valid @RequestBody StateBeforeTax stateBeforeTax) throws URISyntaxException {
        log.debug("REST request to update StateBeforeTax : {}", stateBeforeTax);
        if (stateBeforeTax.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StateBeforeTax result = stateBeforeTaxService.save(stateBeforeTax);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stateBeforeTax.getId().toString()))
            .body(result);
    }

    /**
     * GET  /state-before-taxes : get all the stateBeforeTaxes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stateBeforeTaxes in body
     */
    @GetMapping("/state-before-taxes")
    @Timed
    public ResponseEntity<List<StateBeforeTax>> getAllStateBeforeTaxes(Pageable pageable) {
        log.debug("REST request to get a page of StateBeforeTaxes");
        Page<StateBeforeTax> page = stateBeforeTaxService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/state-before-taxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /state-before-taxes/:id : get the "id" stateBeforeTax.
     *
     * @param id the id of the stateBeforeTax to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stateBeforeTax, or with status 404 (Not Found)
     */
    @GetMapping("/state-before-taxes/{id}")
    @Timed
    public ResponseEntity<StateBeforeTax> getStateBeforeTax(@PathVariable Long id) {
        log.debug("REST request to get StateBeforeTax : {}", id);
        Optional<StateBeforeTax> stateBeforeTax = stateBeforeTaxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stateBeforeTax);
    }

    /**
     * DELETE  /state-before-taxes/:id : delete the "id" stateBeforeTax.
     *
     * @param id the id of the stateBeforeTax to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/state-before-taxes/{id}")
    @Timed
    public ResponseEntity<Void> deleteStateBeforeTax(@PathVariable Long id) {
        log.debug("REST request to delete StateBeforeTax : {}", id);
        stateBeforeTaxService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/state-before-taxes?query=:query : search for the stateBeforeTax corresponding
     * to the query.
     *
     * @param query    the query of the stateBeforeTax search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/state-before-taxes")
    @Timed
    public ResponseEntity<List<StateBeforeTax>> searchStateBeforeTaxes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of StateBeforeTaxes for query {}", query);
        Page<StateBeforeTax> page = stateBeforeTaxService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/state-before-taxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
