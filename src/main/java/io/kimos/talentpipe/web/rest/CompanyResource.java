package io.kimos.talentpipe.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.kimos.talentpipe.domain.Company;
import io.kimos.talentpipe.security.SecurityUtils;
import io.kimos.talentpipe.service.CompanyQueryService;
import io.kimos.talentpipe.service.CompanyService;
import io.kimos.talentpipe.service.UserService;
import io.kimos.talentpipe.service.dto.CompanyCriteria;
import io.kimos.talentpipe.web.rest.dto.CreateCompanyRequest;
import io.kimos.talentpipe.web.rest.errors.BadRequestAlertException;
import io.kimos.talentpipe.web.rest.errors.UserNotAuthenticatedException;
import io.kimos.talentpipe.web.rest.util.HeaderUtil;
import io.kimos.talentpipe.web.rest.util.PaginationUtil;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Company.
 */
@RestController
@RequestMapping("/api")
public class CompanyResource {

    private final Logger log = LoggerFactory.getLogger(CompanyResource.class);

    private static final String ENTITY_NAME = "company";

    private final CompanyService companyService;

    private final CompanyQueryService companyQueryService;

    private final MapperFacade orikaMapper;

    public CompanyResource(CompanyService companyService, CompanyQueryService companyQueryService, MapperFacade orikaMapper) {
        this.companyService = companyService;
        this.companyQueryService = companyQueryService;
        this.orikaMapper = orikaMapper;
    }

    /**
     * POST  /companies : Create a new company.
     *
     * @param company the company to create
     * @return the ResponseEntity with status 201 (Created) and with body the new company, or with status 400 (Bad Request) if the company has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/companies")
    @Timed
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) throws URISyntaxException {
        log.debug("REST request to save Company : {}", company);
        if (company.getId() != null) {
            throw new BadRequestAlertException("A new company cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Company result = companyService.save(company);
        return ResponseEntity.created(new URI("/api/companies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/companies/register")
    @Timed
    public void registerCompany(@Valid @RequestBody CreateCompanyRequest request) {
        companyService.createCompany(orikaMapper.map(request, Company.class), request.getPassword());
    }

    /**
     * PUT  /companies : Updates an existing company.
     *
     * @param company the company to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated company,
     * or with status 400 (Bad Request) if the company is not valid,
     * or with status 500 (Internal Server Error) if the company couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/companies")
    @Timed
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company) throws URISyntaxException {
        log.debug("REST request to update Company : {}", company);
        if (company.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Company result = companyService.save(company);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, company.getId().toString()))
            .body(result);
    }

    /**
     * GET  /companies : get all the companies.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of companies in body
     */
    @GetMapping("/companies")
    @Timed
    public ResponseEntity<List<Company>> getAllCompanies(CompanyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Companies by criteria: {}", criteria);
        Page<Company> page = companyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/companies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /companies/:id : get the "id" company.
     *
     * @param id the id of the company to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the company, or with status 404 (Not Found)
     */
    @GetMapping("/companies/{id}")
    @Timed
    public ResponseEntity<Company> getCompany(@PathVariable Long id) {
        log.debug("REST request to get Company : {}", id);
        Optional<Company> company = companyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(company);
    }

    /**
     * GET  /companies/me : get the company for the user that is authenticated.
     *
     * @param id the id of the company to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the company, or with status 404 (Not Found)
     */
    @PreAuthorize("hasAnyRole('ROLE_COMPANY_ADMIN')")
    @GetMapping("/companies/me")
    @Timed
    public ResponseEntity<Company> getCurrentUserCompany() {
        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(UserNotAuthenticatedException::new);
        Optional<Company> optionalCompany = companyService.findByCurrentUserLogin(login);
        return ResponseUtil.wrapOrNotFound(optionalCompany);
    }

    /**
     * DELETE  /companies/:id : delete the "id" company.
     *
     * @param id the id of the company to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/companies/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        log.debug("REST request to delete Company : {}", id);
        companyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/companies?query=:query : search for the company corresponding
     * to the query.
     *
     * @param query    the query of the company search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/companies")
    @Timed
    public ResponseEntity<List<Company>> searchCompanies(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Companies for query {}", query);
        Page<Company> page = companyService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/companies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
