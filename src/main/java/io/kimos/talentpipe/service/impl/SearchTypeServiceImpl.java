package io.kimos.talentpipe.service.impl;

import io.kimos.talentpipe.domain.SearchType;
import io.kimos.talentpipe.repository.SearchTypeRepository;
import io.kimos.talentpipe.repository.search.SearchTypeSearchRepository;
import io.kimos.talentpipe.service.SearchTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing SearchType.
 */
@Service
@Transactional
public class SearchTypeServiceImpl implements SearchTypeService {

    private final Logger log = LoggerFactory.getLogger(SearchTypeServiceImpl.class);

    private final SearchTypeRepository searchTypeRepository;

    private final SearchTypeSearchRepository searchTypeSearchRepository;

    public SearchTypeServiceImpl(SearchTypeRepository searchTypeRepository, SearchTypeSearchRepository searchTypeSearchRepository) {
        this.searchTypeRepository = searchTypeRepository;
        this.searchTypeSearchRepository = searchTypeSearchRepository;
    }

    /**
     * Save a searchType.
     *
     * @param searchType the entity to save
     * @return the persisted entity
     */
    @Override
    public SearchType save(SearchType searchType) {
        log.debug("Request to save SearchType : {}", searchType);
        SearchType result = searchTypeRepository.save(searchType);
        searchTypeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the searchTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SearchType> findAll(Pageable pageable) {
        log.debug("Request to get all SearchTypes");
        return searchTypeRepository.findAll(pageable);
    }


    /**
     * Get one searchType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SearchType> findOne(Long id) {
        log.debug("Request to get SearchType : {}", id);
        return searchTypeRepository.findById(id);
    }

    /**
     * Delete the searchType by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SearchType : {}", id);
        searchTypeRepository.deleteById(id);
        searchTypeSearchRepository.deleteById(id);
    }

    /**
     * Search for the searchType corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SearchType> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SearchTypes for query {}", query);
        return searchTypeSearchRepository.search(queryStringQuery(query), pageable);
    }
}
