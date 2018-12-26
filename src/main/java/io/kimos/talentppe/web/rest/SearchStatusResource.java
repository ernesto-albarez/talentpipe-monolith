package io.kimos.talentppe.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.kimos.talentppe.domain.SearchStatus;
import io.kimos.talentppe.service.SearchStatusService;
import io.kimos.talentppe.web.rest.errors.BadRequestAlertException;
import io.kimos.talentppe.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing SearchStatus.
 */
@RestController
@RequestMapping("/api")
public class SearchStatusResource {

    private final Logger log = LoggerFactory.getLogger(SearchStatusResource.class);

    private static final String ENTITY_NAME = "searchStatus";

    private final SearchStatusService searchStatusService;

    public SearchStatusResource(SearchStatusService searchStatusService) {
        this.searchStatusService = searchStatusService;
    }

    /**
     * POST  /search-statuses : Create a new searchStatus.
     *
     * @param searchStatus the searchStatus to create
     * @return the ResponseEntity with status 201 (Created) and with body the new searchStatus, or with status 400 (Bad Request) if the searchStatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/search-statuses")
    @Timed
    public ResponseEntity<SearchStatus> createSearchStatus(@Valid @RequestBody SearchStatus searchStatus) throws URISyntaxException {
        log.debug("REST request to save SearchStatus : {}", searchStatus);
        if (searchStatus.getId() != null) {
            throw new BadRequestAlertException("A new searchStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SearchStatus result = searchStatusService.save(searchStatus);
        return ResponseEntity.created(new URI("/api/search-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /search-statuses : Updates an existing searchStatus.
     *
     * @param searchStatus the searchStatus to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated searchStatus,
     * or with status 400 (Bad Request) if the searchStatus is not valid,
     * or with status 500 (Internal Server Error) if the searchStatus couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/search-statuses")
    @Timed
    public ResponseEntity<SearchStatus> updateSearchStatus(@Valid @RequestBody SearchStatus searchStatus) throws URISyntaxException {
        log.debug("REST request to update SearchStatus : {}", searchStatus);
        if (searchStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SearchStatus result = searchStatusService.save(searchStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, searchStatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /search-statuses : get all the searchStatuses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of searchStatuses in body
     */
    @GetMapping("/search-statuses")
    @Timed
    public List<SearchStatus> getAllSearchStatuses() {
        log.debug("REST request to get all SearchStatuses");
        return searchStatusService.findAll();
    }

    /**
     * GET  /search-statuses/:id : get the "id" searchStatus.
     *
     * @param id the id of the searchStatus to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the searchStatus, or with status 404 (Not Found)
     */
    @GetMapping("/search-statuses/{id}")
    @Timed
    public ResponseEntity<SearchStatus> getSearchStatus(@PathVariable Long id) {
        log.debug("REST request to get SearchStatus : {}", id);
        Optional<SearchStatus> searchStatus = searchStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(searchStatus);
    }

    /**
     * DELETE  /search-statuses/:id : delete the "id" searchStatus.
     *
     * @param id the id of the searchStatus to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/search-statuses/{id}")
    @Timed
    public ResponseEntity<Void> deleteSearchStatus(@PathVariable Long id) {
        log.debug("REST request to delete SearchStatus : {}", id);
        searchStatusService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/search-statuses?query=:query : search for the searchStatus corresponding
     * to the query.
     *
     * @param query the query of the searchStatus search
     * @return the result of the search
     */
    @GetMapping("/_search/search-statuses")
    @Timed
    public List<SearchStatus> searchSearchStatuses(@RequestParam String query) {
        log.debug("REST request to search SearchStatuses for query {}", query);
        return searchStatusService.search(query);
    }

}
