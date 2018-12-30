package io.kimos.talentpipe.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.kimos.talentpipe.domain.SearchStatus;
import io.kimos.talentpipe.service.SearchStatusService;
import io.kimos.talentpipe.service.UserService;
import io.kimos.talentpipe.web.dto.CreateSearchStatusRequest;
import io.kimos.talentpipe.web.rest.errors.BadRequestAlertException;
import io.kimos.talentpipe.web.rest.errors.UserNotAuthenticatedException;
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
 * REST controller for managing SearchStatus.
 */
@RestController
@RequestMapping("/api")
public class SearchStatusResource {

    private static final String ENTITY_NAME = "searchStatus";
    private final Logger log = LoggerFactory.getLogger(SearchStatusResource.class);
    private final SearchStatusService searchStatusService;

    private final UserService userService;

    private final MapperFacade orikaMapper;

    public SearchStatusResource(SearchStatusService searchStatusService, MapperFacade orikaMapper, UserService userService) {
        this.searchStatusService = searchStatusService;
        this.orikaMapper = orikaMapper;
        this.userService = userService;
    }

    /**
     * POST  /search-statuses : Create a new searchStatus.
     *
     * @param request the searchStatus to create
     * @return the ResponseEntity with status 201 (Created) and with body the new searchStatus, or with status 400 (Bad Request) if the searchStatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/search-statuses")
    @Timed
    public ResponseEntity<SearchStatus> createSearchStatus(@Valid @RequestBody CreateSearchStatusRequest request) throws URISyntaxException {
        log.debug("REST request to save SearchStatus : {}", request);
        SearchStatus searchStatus = orikaMapper.map(request, SearchStatus.class);
        searchStatus.setCompany(userService.getUserWithAuthorities().orElseThrow(UserNotAuthenticatedException::new).getCompany());
        searchStatus = searchStatusService.save(searchStatus);
        return ResponseEntity.created(new URI("/api/search-statuses/" + searchStatus.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, searchStatus.getId().toString()))
            .body(searchStatus);
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
