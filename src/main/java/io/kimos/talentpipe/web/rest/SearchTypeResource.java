package io.kimos.talentpipe.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.kimos.talentpipe.domain.SearchType;
import io.kimos.talentpipe.service.SearchTypeService;
import io.kimos.talentpipe.web.rest.dto.CreateSearchTypeDTO;
import io.kimos.talentpipe.web.rest.dto.UpdateSearchTypeDTO;
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
 * REST controller for managing SearchType.
 */
@RestController
@RequestMapping("/api")
public class SearchTypeResource {

    private static final String ENTITY_NAME = "searchType";
    private final Logger log = LoggerFactory.getLogger(SearchTypeResource.class);
    private final SearchTypeService searchTypeService;
    private final MapperFacade orikaMapper;

    public SearchTypeResource(SearchTypeService searchTypeService, MapperFacade orikaMapper) {
        this.searchTypeService = searchTypeService;
        this.orikaMapper = orikaMapper;
    }

    /**
     * POST  /search-types : Create a new searchType.
     *
     * @param searchType the searchType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new searchType, or with status 400 (Bad Request) if the searchType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/search-types")
    @Timed
    public ResponseEntity<SearchType> createSearchType(@Valid @RequestBody CreateSearchTypeDTO searchType) throws URISyntaxException {
        log.debug("REST request to save SearchType : {}", searchType);
        SearchType result = searchTypeService.save(orikaMapper.map(searchType, SearchType.class));
        return ResponseEntity.created(new URI("/api/search-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /search-types : Updates an existing searchType.
     *
     * @param searchType the searchType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated searchType,
     * or with status 400 (Bad Request) if the searchType is not valid,
     * or with status 500 (Internal Server Error) if the searchType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/search-types")
    @Timed
    public ResponseEntity<SearchType> updateSearchType(@Valid @RequestBody UpdateSearchTypeDTO searchType) throws URISyntaxException {
        log.debug("REST request to update SearchType : {}", searchType);
        if (searchType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SearchType result = searchTypeService.save(orikaMapper.map(searchType, SearchType.class));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, searchType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /search-types : get all the searchTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of searchTypes in body
     */
    @GetMapping("/search-types")
    @Timed
    public ResponseEntity<List<SearchType>> getAllSearchTypes(Pageable pageable) {
        log.debug("REST request to get a page of SearchTypes");
        Page<SearchType> page = searchTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/search-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /search-types/:id : get the "id" searchType.
     *
     * @param id the id of the searchType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the searchType, or with status 404 (Not Found)
     */
    @GetMapping("/search-types/{id}")
    @Timed
    public ResponseEntity<SearchType> getSearchType(@PathVariable Long id) {
        log.debug("REST request to get SearchType : {}", id);
        Optional<SearchType> searchType = searchTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(searchType);
    }

    /**
     * DELETE  /search-types/:id : delete the "id" searchType.
     *
     * @param id the id of the searchType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/search-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteSearchType(@PathVariable Long id) {
        log.debug("REST request to delete SearchType : {}", id);
        searchTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/search-types?query=:query : search for the searchType corresponding
     * to the query.
     *
     * @param query    the query of the searchType search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/search-types")
    @Timed
    public ResponseEntity<List<SearchType>> searchSearchTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SearchTypes for query {}", query);
        Page<SearchType> page = searchTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/search-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
