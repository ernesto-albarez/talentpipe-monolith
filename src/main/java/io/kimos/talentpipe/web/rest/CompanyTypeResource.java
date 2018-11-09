package io.kimos.talentpipe.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.kimos.talentpipe.domain.CompanyType;
import io.kimos.talentpipe.service.CompanyTypeService;
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
 * REST controller for managing CompanyType.
 */
@RestController
@RequestMapping("/api")
public class CompanyTypeResource {

    private static final String ENTITY_NAME = "companyType";
    private final Logger log = LoggerFactory.getLogger(CompanyTypeResource.class);
    private final CompanyTypeService companyTypeService;

    public CompanyTypeResource(CompanyTypeService companyTypeService) {
        this.companyTypeService = companyTypeService;
    }

    /**
     * POST  /company-types : Create a new companyType.
     *
     * @param companyType the companyType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new companyType, or with status 400 (Bad Request) if the companyType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/company-types")
    @Timed
    public ResponseEntity<CompanyType> createCompanyType(@Valid @RequestBody CompanyType companyType) throws URISyntaxException {
        log.debug("REST request to save CompanyType : {}", companyType);
        if (companyType.getId() != null) {
            throw new BadRequestAlertException("A new companyType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompanyType result = companyTypeService.save(companyType);
        return ResponseEntity.created(new URI("/api/company-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /company-types : Updates an existing companyType.
     *
     * @param companyType the companyType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated companyType,
     * or with status 400 (Bad Request) if the companyType is not valid,
     * or with status 500 (Internal Server Error) if the companyType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/company-types")
    @Timed
    public ResponseEntity<CompanyType> updateCompanyType(@Valid @RequestBody CompanyType companyType) throws URISyntaxException {
        log.debug("REST request to update CompanyType : {}", companyType);
        if (companyType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CompanyType result = companyTypeService.save(companyType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, companyType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /company-types : get all the companyTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of companyTypes in body
     */
    @GetMapping("/company-types")
    @Timed
    public ResponseEntity<List<CompanyType>> getAllCompanyTypes(Pageable pageable) {
        log.debug("REST request to get a page of CompanyTypes");
        Page<CompanyType> page = companyTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/company-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /company-types/:id : get the "id" companyType.
     *
     * @param id the id of the companyType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the companyType, or with status 404 (Not Found)
     */
    @GetMapping("/company-types/{id}")
    @Timed
    public ResponseEntity<CompanyType> getCompanyType(@PathVariable Long id) {
        log.debug("REST request to get CompanyType : {}", id);
        Optional<CompanyType> companyType = companyTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(companyType);
    }

    /**
     * DELETE  /company-types/:id : delete the "id" companyType.
     *
     * @param id the id of the companyType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/company-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompanyType(@PathVariable Long id) {
        log.debug("REST request to delete CompanyType : {}", id);
        companyTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/company-types?query=:query : search for the companyType corresponding
     * to the query.
     *
     * @param query    the query of the companyType search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/company-types")
    @Timed
    public ResponseEntity<List<CompanyType>> searchCompanyTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CompanyTypes for query {}", query);
        Page<CompanyType> page = companyTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/company-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
