package io.kimos.talentppe.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.kimos.talentppe.domain.SoftSkill;
import io.kimos.talentppe.service.SoftSkillService;
import io.kimos.talentppe.web.rest.dto.CreateSoftSkillDTO;
import io.kimos.talentppe.web.rest.dto.UpdateSoftSkillDTO;
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
 * REST controller for managing SoftSkill.
 */
@RestController
@RequestMapping("/api")
public class SoftSkillResource {

    private static final String ENTITY_NAME = "softSkill";
    private final Logger log = LoggerFactory.getLogger(SoftSkillResource.class);
    private final SoftSkillService softSkillService;
    private final MapperFacade orikaMapper;

    public SoftSkillResource(SoftSkillService softSkillService, MapperFacade orikaMapper) {
        this.softSkillService = softSkillService;
        this.orikaMapper = orikaMapper;
    }

    /**
     * POST  /soft-skills : Create a new softSkill.
     *
     * @param softSkill the softSkill to create
     * @return the ResponseEntity with status 201 (Created) and with body the new softSkill, or with status 400 (Bad Request) if the softSkill has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/soft-skills")
    @Timed
    public ResponseEntity<SoftSkill> createSoftSkill(@Valid @RequestBody CreateSoftSkillDTO softSkill) throws URISyntaxException {
        log.debug("REST request to save SoftSkill : {}", softSkill);
        SoftSkill result = softSkillService.save(orikaMapper.map(softSkill, SoftSkill.class));
        return ResponseEntity.created(new URI("/api/soft-skills/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /soft-skills : Updates an existing softSkill.
     *
     * @param softSkill the softSkill to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated softSkill,
     * or with status 400 (Bad Request) if the softSkill is not valid,
     * or with status 500 (Internal Server Error) if the softSkill couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/soft-skills")
    @Timed
    public ResponseEntity<SoftSkill> updateSoftSkill(@Valid @RequestBody UpdateSoftSkillDTO softSkill) throws URISyntaxException {
        log.debug("REST request to update SoftSkill : {}", softSkill);
        if (softSkill.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SoftSkill result = softSkillService.save(orikaMapper.map(softSkill, SoftSkill.class));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, softSkill.getId().toString()))
            .body(result);
    }

    /**
     * GET  /soft-skills : get all the softSkills.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of softSkills in body
     */
    @GetMapping("/soft-skills")
    @Timed
    public ResponseEntity<List<SoftSkill>> getAllSoftSkills(Pageable pageable) {
        log.debug("REST request to get a page of SoftSkills");
        Page<SoftSkill> page = softSkillService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/soft-skills");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /soft-skills/:id : get the "id" softSkill.
     *
     * @param id the id of the softSkill to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the softSkill, or with status 404 (Not Found)
     */
    @GetMapping("/soft-skills/{id}")
    @Timed
    public ResponseEntity<SoftSkill> getSoftSkill(@PathVariable Long id) {
        log.debug("REST request to get SoftSkill : {}", id);
        Optional<SoftSkill> softSkill = softSkillService.findOne(id);
        return ResponseUtil.wrapOrNotFound(softSkill);
    }

    /**
     * DELETE  /soft-skills/:id : delete the "id" softSkill.
     *
     * @param id the id of the softSkill to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/soft-skills/{id}")
    @Timed
    public ResponseEntity<Void> deleteSoftSkill(@PathVariable Long id) {
        log.debug("REST request to delete SoftSkill : {}", id);
        softSkillService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/soft-skills?query=:query : search for the softSkill corresponding
     * to the query.
     *
     * @param query    the query of the softSkill search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/soft-skills")
    @Timed
    public ResponseEntity<List<SoftSkill>> searchSoftSkills(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SoftSkills for query {}", query);
        Page<SoftSkill> page = softSkillService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/soft-skills");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
