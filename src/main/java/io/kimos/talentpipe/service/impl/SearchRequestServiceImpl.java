package io.kimos.talentpipe.service.impl;

import io.kimos.talentpipe.service.SearchRequestService;
import io.kimos.talentpipe.domain.SearchRequest;
import io.kimos.talentpipe.repository.SearchRequestRepository;
import io.kimos.talentpipe.repository.search.SearchRequestSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SearchRequest.
 */
@Service
@Transactional
public class SearchRequestServiceImpl implements SearchRequestService {

    private final Logger log = LoggerFactory.getLogger(SearchRequestServiceImpl.class);

    private final SearchRequestRepository searchRequestRepository;

    private final SearchRequestSearchRepository searchRequestSearchRepository;

    public SearchRequestServiceImpl(SearchRequestRepository searchRequestRepository, SearchRequestSearchRepository searchRequestSearchRepository) {
        this.searchRequestRepository = searchRequestRepository;
        this.searchRequestSearchRepository = searchRequestSearchRepository;
    }

    /**
     * Save a searchRequest.
     *
     * @param searchRequest the entity to save
     * @return the persisted entity
     */
    @Override
    public SearchRequest save(SearchRequest searchRequest) {
        log.debug("Request to save SearchRequest : {}", searchRequest);
        SearchRequest result = searchRequestRepository.save(searchRequest);
        searchRequestSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the searchRequests.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SearchRequest> findAll(Pageable pageable) {
        log.debug("Request to get all SearchRequests");
        return searchRequestRepository.findAll(pageable);
    }

    /**
     * Get all the SearchRequest with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<SearchRequest> findAllWithEagerRelationships(Pageable pageable) {
        return searchRequestRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one searchRequest by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SearchRequest> findOne(Long id) {
        log.debug("Request to get SearchRequest : {}", id);
        return searchRequestRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the searchRequest by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SearchRequest : {}", id);
        searchRequestRepository.deleteById(id);
        searchRequestSearchRepository.deleteById(id);
    }

    /**
     * Search for the searchRequest corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SearchRequest> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SearchRequests for query {}", query);
        return searchRequestSearchRepository.search(queryStringQuery(query), pageable);    }
}
