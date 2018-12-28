package io.kimos.talentpipe.web.rest

import com.codahale.metrics.annotation.Timed
import io.github.jhipster.web.util.ResponseUtil
import io.kimos.talentpipe.domain.SearchStatus
import io.kimos.talentpipe.service.SearchStatusService
import io.kimos.talentpipe.web.rest.errors.BadRequestAlertException
import io.kimos.talentpipe.web.rest.util.HeaderUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.net.URISyntaxException
import javax.validation.Valid

@RestController
@RequestMapping("/api")
open class SearchStatusRestController @Autowired constructor(private val searchStatusService : SearchStatusService) {

    private val log = LoggerFactory.getLogger(javaClass);

    private val ENTITY_NAME = "searchStatus"

    /**
     * POST  /search-statuses : Create a new searchStatus.
     *
     * @param searchStatus the searchStatus to create
     * @return the ResponseEntity with status 201 (Created) and with body the new searchStatus, or with status 400 (Bad Request) if the searchStatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/search-statuses")
    @Timed
    @Throws(URISyntaxException::class)
    fun createSearchStatus(@Valid @RequestBody searchStatus: SearchStatus): ResponseEntity<SearchStatus> {
        log.debug("REST request to save SearchStatus : {}", searchStatus)
        if (searchStatus.id != null) {
            throw BadRequestAlertException("A new searchStatus cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = searchStatusService.save(searchStatus)
        return ResponseEntity.created(URI("/api/search-statuses/" + result.id!!))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.id!!.toString()))
            .body(result)
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
    @Throws(URISyntaxException::class)
    fun updateSearchStatus(@Valid @RequestBody searchStatus: SearchStatus): ResponseEntity<SearchStatus> {
        log.debug("REST request to update SearchStatus : {}", searchStatus)
        if (searchStatus.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = searchStatusService.save(searchStatus)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, searchStatus.id!!.toString()))
            .body(result)
    }

    /**
     * GET  /search-statuses : get all the searchStatuses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of searchStatuses in body
     */
    @GetMapping("/search-statuses")
    @Timed
    fun getAllSearchStatuses(): List<SearchStatus> {
        log.debug("REST request to get all SearchStatuses")
        return searchStatusService.findAll()
    }

    /**
     * GET  /search-statuses/:id : get the "id" searchStatus.
     *
     * @param id the id of the searchStatus to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the searchStatus, or with status 404 (Not Found)
     */
    @GetMapping("/search-statuses/{id}")
    @Timed
    fun getSearchStatus(@PathVariable id: Long?): ResponseEntity<SearchStatus> {
        log.debug("REST request to get SearchStatus : {}", id)
        val searchStatus = searchStatusService.findOne(id)
        return ResponseUtil.wrapOrNotFound(searchStatus)
    }

    /**
     * DELETE  /search-statuses/:id : delete the "id" searchStatus.
     *
     * @param id the id of the searchStatus to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/search-statuses/{id}")
    @Timed
    fun deleteSearchStatus(@PathVariable id: Long?): ResponseEntity<Void> {
        log.debug("REST request to delete SearchStatus : {}", id)
        searchStatusService.delete(id)
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id!!.toString())).build()
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
    fun searchSearchStatuses(@RequestParam query: String): List<SearchStatus> {
        log.debug("REST request to search SearchStatuses for query {}", query)
        return searchStatusService.search(query)
    }
}
