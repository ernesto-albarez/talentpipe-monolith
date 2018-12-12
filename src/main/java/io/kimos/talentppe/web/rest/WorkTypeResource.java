package io.kimos.talentppe.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.kimos.talentppe.domain.WorkType;
import io.kimos.talentppe.service.WorkTypeService;
import io.kimos.talentppe.web.rest.dto.CreateWorkTypeDTO;
import io.kimos.talentppe.web.rest.dto.UpdateWorkTypeDTO;
import io.kimos.talentppe.web.rest.errors.BadRequestAlertException;
import io.kimos.talentppe.web.rest.util.HeaderUtil;
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
 * REST controller for managing WorkType.
 */
@RestController
@RequestMapping("/api")
public class WorkTypeResource {

    private static final String ENTITY_NAME = "workType";
    private final Logger log = LoggerFactory.getLogger(WorkTypeResource.class);
    private final WorkTypeService workTypeService;

    private final MapperFacade orikaMapper;

    public WorkTypeResource(WorkTypeService workTypeService, MapperFacade orikaMapper) {
        this.workTypeService = workTypeService;
        this.orikaMapper = orikaMapper;
    }

    /**
     * POST  /work-types : Create a new workType.
     *
     * @param workType the workType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workType, or with status 400 (Bad Request) if the workType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/work-types")
    @Timed
    public ResponseEntity<WorkType> createWorkType(@Valid @RequestBody CreateWorkTypeDTO workType) throws URISyntaxException {
        log.debug("REST request to save WorkType : {}", workType);
        WorkType result = workTypeService.save(orikaMapper.map(workType, WorkType.class));
        return ResponseEntity.created(new URI("/api/work-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /work-types : Updates an existing workType.
     *
     * @param workType the workType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workType,
     * or with status 400 (Bad Request) if the workType is not valid,
     * or with status 500 (Internal Server Error) if the workType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/work-types")
    @Timed
    public ResponseEntity<WorkType> updateWorkType(@Valid @RequestBody UpdateWorkTypeDTO workType) throws URISyntaxException {
        log.debug("REST request to update WorkType : {}", workType);
        if (workType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WorkType result = workTypeService.save(orikaMapper.map(workType, WorkType.class));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, workType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /work-types : get all the workTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of workTypes in body
     */
    @GetMapping("/work-types")
    @Timed
    public List<WorkType> getAllWorkTypes() {
        log.debug("REST request to get all WorkTypes");
        return workTypeService.findAll();
    }

    /**
     * GET  /work-types/:id : get the "id" workType.
     *
     * @param id the id of the workType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workType, or with status 404 (Not Found)
     */
    @GetMapping("/work-types/{id}")
    @Timed
    public ResponseEntity<WorkType> getWorkType(@PathVariable Long id) {
        log.debug("REST request to get WorkType : {}", id);
        Optional<WorkType> workType = workTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workType);
    }

    /**
     * DELETE  /work-types/:id : delete the "id" workType.
     *
     * @param id the id of the workType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/work-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteWorkType(@PathVariable Long id) {
        log.debug("REST request to delete WorkType : {}", id);
        workTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/work-types?query=:query : search for the workType corresponding
     * to the query.
     *
     * @param query the query of the workType search
     * @return the result of the search
     */
    @GetMapping("/_search/work-types")
    @Timed
    public List<WorkType> searchWorkTypes(@RequestParam String query) {
        log.debug("REST request to search WorkTypes for query {}", query);
        return workTypeService.search(query);
    }

}
