package io.kimos.talentpipe.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.kimos.talentpipe.domain.ExpertiseLevel;
import io.kimos.talentpipe.service.ExpertiseLevelService;
import io.kimos.talentpipe.web.rest.dto.CreateExpertiseLevelDTO;
import io.kimos.talentpipe.web.rest.dto.UpdateExpertiseLevelDTO;
import io.kimos.talentpipe.web.rest.errors.BadRequestAlertException;
import io.kimos.talentpipe.web.rest.util.HeaderUtil;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ExpertiseLevel.
 */
@RestController
@RequestMapping("/api")
public class ExpertiseLevelResource {

    private static final String ENTITY_NAME = "expertiseLevel";
    private final Logger log = LoggerFactory.getLogger(ExpertiseLevelResource.class);
    private final ExpertiseLevelService expertiseLevelService;
    private final MapperFacade orikaMapper;

    public ExpertiseLevelResource(ExpertiseLevelService expertiseLevelService, MapperFacade orikaMapper) {
        this.expertiseLevelService = expertiseLevelService;
        this.orikaMapper = orikaMapper;
    }

    /**
     * POST  /expertise-levels : Create a new expertiseLevel.
     *
     * @param expertiseLevel the expertiseLevel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new expertiseLevel, or with status 400 (Bad Request) if the expertiseLevel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/expertise-levels")
    @Timed
    public ResponseEntity<ExpertiseLevel> createExpertiseLevel(@Valid @RequestBody CreateExpertiseLevelDTO expertiseLevel) throws URISyntaxException {
        log.debug("REST request to save ExpertiseLevel : {}", expertiseLevel);
        ExpertiseLevel result = expertiseLevelService.save(orikaMapper.map(expertiseLevel, ExpertiseLevel.class));
        return ResponseEntity.created(new URI("/api/expertise-levels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /expertise-levels : Updates an existing expertiseLevel.
     *
     * @param expertiseLevel the expertiseLevel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated expertiseLevel,
     * or with status 400 (Bad Request) if the expertiseLevel is not valid,
     * or with status 500 (Internal Server Error) if the expertiseLevel couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/expertise-levels")
    @Timed
    public ResponseEntity<ExpertiseLevel> updateExpertiseLevel(@Valid @RequestBody UpdateExpertiseLevelDTO expertiseLevel) throws URISyntaxException {
        log.debug("REST request to update ExpertiseLevel : {}", expertiseLevel);
        if (expertiseLevel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExpertiseLevel result = expertiseLevelService.save(orikaMapper.map(expertiseLevel, ExpertiseLevel.class));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, expertiseLevel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /expertise-levels : get all the expertiseLevels.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of expertiseLevels in body
     */
    @GetMapping("/expertise-levels")
    @Timed
    public List<ExpertiseLevel> getAllExpertiseLevels() {
        log.debug("REST request to get all ExpertiseLevels");
        return expertiseLevelService.findAll();
    }

    /**
     * GET  /expertise-levels/:id : get the "id" expertiseLevel.
     *
     * @param id the id of the expertiseLevel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the expertiseLevel, or with status 404 (Not Found)
     */
    @GetMapping("/expertise-levels/{id}")
    @Timed
    public ResponseEntity<ExpertiseLevel> getExpertiseLevel(@PathVariable Long id) {
        log.debug("REST request to get ExpertiseLevel : {}", id);
        Optional<ExpertiseLevel> expertiseLevel = expertiseLevelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(expertiseLevel);
    }

    /**
     * DELETE  /expertise-levels/:id : delete the "id" expertiseLevel.
     *
     * @param id the id of the expertiseLevel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/expertise-levels/{id}")
    @Timed
    public ResponseEntity<Void> deleteExpertiseLevel(@PathVariable Long id) {
        log.debug("REST request to delete ExpertiseLevel : {}", id);
        expertiseLevelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/expertise-levels?query=:query : search for the expertiseLevel corresponding
     * to the query.
     *
     * @param query the query of the expertiseLevel search
     * @return the result of the search
     */
    @GetMapping("/_search/expertise-levels")
    @Timed
    public List<ExpertiseLevel> searchExpertiseLevels(@RequestParam String query) {
        log.debug("REST request to search ExpertiseLevels for query {}", query);
        return expertiseLevelService.search(query);
    }

}
