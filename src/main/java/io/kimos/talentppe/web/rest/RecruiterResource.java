package io.kimos.talentppe.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.kimos.talentppe.domain.Recruiter;
import io.kimos.talentppe.service.RecruiterService;
import io.kimos.talentppe.web.rest.dto.RegistryRecruiterRequest;
import io.kimos.talentppe.web.rest.errors.BadRequestAlertException;
import io.kimos.talentppe.web.rest.util.HeaderUtil;
import io.kimos.talentppe.web.rest.util.PaginationUtil;
import io.kimos.talentppe.service.dto.RecruiterCriteria;
import io.kimos.talentppe.service.RecruiterQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Recruiter.
 */
@RestController
@RequestMapping("/api")
public class RecruiterResource {

    private final Logger log = LoggerFactory.getLogger(RecruiterResource.class);

    private static final String ENTITY_NAME = "recruiter";

    private final RecruiterService recruiterService;

    private final RecruiterQueryService recruiterQueryService;
    private final MapperFacade orikaMapper;
    public RecruiterResource(RecruiterService recruiterService, RecruiterQueryService recruiterQueryService, MapperFacade orikaMapper) {
        this.recruiterService = recruiterService;
        this.recruiterQueryService = recruiterQueryService;
        this.orikaMapper = orikaMapper;
    }

    /**
     * POST  /recruiters : Create a new recruiter.
     *
     * @param recruiter the recruiter to create
     * @return the ResponseEntity with status 201 (Created) and with body the new recruiter, or with status 400 (Bad Request) if the recruiter has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/recruiters")
    @Timed
    public ResponseEntity<Recruiter> createRecruiter(@Valid @RequestBody Recruiter recruiter) throws URISyntaxException {
        log.debug("REST request to save Recruiter : {}", recruiter);
        if (recruiter.getId() != null) {
            throw new BadRequestAlertException("A new recruiter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Recruiter result = recruiterService.save(recruiter);
        return ResponseEntity.created(new URI("/api/recruiters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/recruiters/register")
    public void registerRecruiter(@NotNull @Valid @RequestBody RegistryRecruiterRequest request) throws URISyntaxException {
        recruiterService.registryRecruiter(orikaMapper.map(request, Recruiter.class), request.getPassword());
    }

    /**
     * PUT  /recruiters : Updates an existing recruiter.
     *
     * @param recruiter the recruiter to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated recruiter,
     * or with status 400 (Bad Request) if the recruiter is not valid,
     * or with status 500 (Internal Server Error) if the recruiter couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/recruiters")
    @Timed
    public ResponseEntity<Recruiter> updateRecruiter(@Valid @RequestBody Recruiter recruiter) throws URISyntaxException {
        log.debug("REST request to update Recruiter : {}", recruiter);
        if (recruiter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Recruiter result = recruiterService.save(recruiter);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, recruiter.getId().toString()))
            .body(result);
    }

    /**
     * GET  /recruiters : get all the recruiters.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of recruiters in body
     */
    @GetMapping("/recruiters")
    @Timed
    public ResponseEntity<List<Recruiter>> getAllRecruiters(RecruiterCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Recruiters by criteria: {}", criteria);
        Page<Recruiter> page = recruiterQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/recruiters");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /recruiters/:id : get the "id" recruiter.
     *
     * @param id the id of the recruiter to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the recruiter, or with status 404 (Not Found)
     */
    @GetMapping("/recruiters/{id}")
    @Timed
    public ResponseEntity<Recruiter> getRecruiter(@PathVariable Long id) {
        log.debug("REST request to get Recruiter : {}", id);
        Optional<Recruiter> recruiter = recruiterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recruiter);
    }

    /**
     * DELETE  /recruiters/:id : delete the "id" recruiter.
     *
     * @param id the id of the recruiter to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/recruiters/{id}")
    @Timed
    public ResponseEntity<Void> deleteRecruiter(@PathVariable Long id) {
        log.debug("REST request to delete Recruiter : {}", id);
        recruiterService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/recruiters?query=:query : search for the recruiter corresponding
     * to the query.
     *
     * @param query the query of the recruiter search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/recruiters")
    @Timed
    public ResponseEntity<List<Recruiter>> searchRecruiters(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Recruiters for query {}", query);
        Page<Recruiter> page = recruiterService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/recruiters");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
