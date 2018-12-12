package io.kimos.talentppe.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.kimos.talentppe.domain.TechnicalSkill;
import io.kimos.talentppe.service.TechnicalSkillService;
import io.kimos.talentppe.web.rest.dto.CreateTechnicalSkillDTO;
import io.kimos.talentppe.web.rest.dto.UpdateTechnicalSkillDTO;
import io.kimos.talentppe.web.rest.errors.BadRequestAlertException;
import io.kimos.talentppe.web.rest.util.HeaderUtil;
import io.kimos.talentppe.web.rest.util.PaginationUtil;
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
 * REST controller for managing TechnicalSkill.
 */
@RestController
@RequestMapping("/api")
public class TechnicalSkillResource {

    private static final String ENTITY_NAME = "technicalSkill";
    private final Logger log = LoggerFactory.getLogger(TechnicalSkillResource.class);
    private final TechnicalSkillService technicalSkillService;
    private final MapperFacade orikaMapper;

    public TechnicalSkillResource(TechnicalSkillService technicalSkillService, MapperFacade orikaMapper) {
        this.technicalSkillService = technicalSkillService;
        this.orikaMapper = orikaMapper;
    }

    /**
     * POST  /technical-skills : Create a new technicalSkill.
     *
     * @param technicalSkill the technicalSkill to create
     * @return the ResponseEntity with status 201 (Created) and with body the new technicalSkill, or with status 400 (Bad Request) if the technicalSkill has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/technical-skills")
    @Timed
    public ResponseEntity<TechnicalSkill> createTechnicalSkill(@Valid @RequestBody CreateTechnicalSkillDTO technicalSkill) throws URISyntaxException {
        log.debug("REST request to save TechnicalSkill : {}", technicalSkill);
        TechnicalSkill result = technicalSkillService.save(orikaMapper.map(technicalSkill, TechnicalSkill.class));
        return ResponseEntity.created(new URI("/api/technical-skills/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /technical-skills : Updates an existing technicalSkill.
     *
     * @param technicalSkill the technicalSkill to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated technicalSkill,
     * or with status 400 (Bad Request) if the technicalSkill is not valid,
     * or with status 500 (Internal Server Error) if the technicalSkill couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/technical-skills")
    @Timed
    public ResponseEntity<TechnicalSkill> updateTechnicalSkill(@Valid @RequestBody UpdateTechnicalSkillDTO technicalSkill) throws URISyntaxException {
        log.debug("REST request to update TechnicalSkill : {}", technicalSkill);
        if (technicalSkill.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TechnicalSkill result = technicalSkillService.save(orikaMapper.map(technicalSkill, TechnicalSkill.class));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, technicalSkill.getId().toString()))
            .body(result);
    }

    /**
     * GET  /technical-skills : get all the technicalSkills.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of technicalSkills in body
     */
    @GetMapping("/technical-skills")
    @Timed
    public ResponseEntity<List<TechnicalSkill>> getAllTechnicalSkills(Pageable pageable) {
        log.debug("REST request to get a page of TechnicalSkills");
        Page<TechnicalSkill> page = technicalSkillService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/technical-skills");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /technical-skills/:id : get the "id" technicalSkill.
     *
     * @param id the id of the technicalSkill to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the technicalSkill, or with status 404 (Not Found)
     */
    @GetMapping("/technical-skills/{id}")
    @Timed
    public ResponseEntity<TechnicalSkill> getTechnicalSkill(@PathVariable Long id) {
        log.debug("REST request to get TechnicalSkill : {}", id);
        Optional<TechnicalSkill> technicalSkill = technicalSkillService.findOne(id);
        return ResponseUtil.wrapOrNotFound(technicalSkill);
    }

    /**
     * DELETE  /technical-skills/:id : delete the "id" technicalSkill.
     *
     * @param id the id of the technicalSkill to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/technical-skills/{id}")
    @Timed
    public ResponseEntity<Void> deleteTechnicalSkill(@PathVariable Long id) {
        log.debug("REST request to delete TechnicalSkill : {}", id);
        technicalSkillService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/technical-skills?query=:query : search for the technicalSkill corresponding
     * to the query.
     *
     * @param query    the query of the technicalSkill search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/technical-skills")
    @Timed
    public ResponseEntity<List<TechnicalSkill>> searchTechnicalSkills(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TechnicalSkills for query {}", query);
        Page<TechnicalSkill> page = technicalSkillService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/technical-skills");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
