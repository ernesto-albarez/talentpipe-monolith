package io.kimos.talentpipe.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.kimos.talentpipe.domain.Benefit;
import io.kimos.talentpipe.service.BenefitService;
import io.kimos.talentpipe.web.rest.dto.CreateBenefitDTO;
import io.kimos.talentpipe.web.rest.dto.UpdateBenefitDTO;
import io.kimos.talentpipe.web.rest.errors.BadRequestAlertException;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Benefit.
 */
@RestController
@RequestMapping("/api")
public class BenefitResource {

    private static final String ENTITY_NAME = "benefit";
    private final Logger log = LoggerFactory.getLogger(BenefitResource.class);
    private final BenefitService benefitService;
    private final MapperFacade orikaMapper;

    public BenefitResource(BenefitService benefitService, MapperFacade orikaMapper) {
        this.benefitService = benefitService;
        this.orikaMapper = orikaMapper;
    }

    /**
     * POST  /benefits : Create a new benefit.
     *
     * @param benefit the benefit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new benefit, or with status 400 (Bad Request) if the benefit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/benefits")
    @Timed
    public ResponseEntity<Benefit> createBenefit(@Valid @RequestBody CreateBenefitDTO benefit) throws URISyntaxException {
        log.debug("REST request to save Benefit : {}", benefit);
        Benefit result = benefitService.save(orikaMapper.map(benefit, Benefit.class));
        return ResponseEntity.created(new URI("/api/benefits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /benefits : Updates an existing benefit.
     *
     * @param benefit the benefit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated benefit,
     * or with status 400 (Bad Request) if the benefit is not valid,
     * or with status 500 (Internal Server Error) if the benefit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/benefits")
    @Timed
    public ResponseEntity<Benefit> updateBenefit(@Valid @RequestBody UpdateBenefitDTO benefit) throws URISyntaxException {
        log.debug("REST request to update Benefit : {}", benefit);
        if (benefit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Benefit result = benefitService.save(orikaMapper.map(benefit, Benefit.class));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, benefit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /benefits : get all the benefits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of benefits in body
     */
    @GetMapping("/benefits")
    @Timed
    public ResponseEntity<List<Benefit>> getAllBenefits(Pageable pageable) {
        log.debug("REST request to get a page of Benefits");
        Page<Benefit> page = benefitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/benefits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /benefits/:id : get the "id" benefit.
     *
     * @param id the id of the benefit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the benefit, or with status 404 (Not Found)
     */
    @GetMapping("/benefits/{id}")
    @Timed
    public ResponseEntity<Benefit> getBenefit(@PathVariable Long id) {
        log.debug("REST request to get Benefit : {}", id);
        Optional<Benefit> benefit = benefitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(benefit);
    }

    /**
     * DELETE  /benefits/:id : delete the "id" benefit.
     *
     * @param id the id of the benefit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/benefits/{id}")
    @Timed
    public ResponseEntity<Void> deleteBenefit(@PathVariable Long id) {
        log.debug("REST request to delete Benefit : {}", id);
        benefitService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/benefits?query=:query : search for the benefit corresponding
     * to the query.
     *
     * @param query    the query of the benefit search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/benefits")
    @Timed
    public ResponseEntity<List<Benefit>> searchBenefits(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Benefits for query {}", query);
        Page<Benefit> page = benefitService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/benefits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
