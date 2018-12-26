package io.kimos.talentppe.service.impl;

import io.kimos.talentppe.service.SearchStatusService;
import io.kimos.talentppe.domain.SearchStatus;
import io.kimos.talentppe.repository.SearchStatusRepository;
import io.kimos.talentppe.repository.search.SearchStatusSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SearchStatus.
 */
@Service
@Transactional
public class SearchStatusServiceImpl implements SearchStatusService {

    private final Logger log = LoggerFactory.getLogger(SearchStatusServiceImpl.class);

    private final SearchStatusRepository searchStatusRepository;

    private final SearchStatusSearchRepository searchStatusSearchRepository;

    public SearchStatusServiceImpl(SearchStatusRepository searchStatusRepository, SearchStatusSearchRepository searchStatusSearchRepository) {
        this.searchStatusRepository = searchStatusRepository;
        this.searchStatusSearchRepository = searchStatusSearchRepository;
    }

    /**
     * Save a searchStatus.
     *
     * @param searchStatus the entity to save
     * @return the persisted entity
     */
    @Override
    public SearchStatus save(SearchStatus searchStatus) {
        log.debug("Request to save SearchStatus : {}", searchStatus);
        SearchStatus result = searchStatusRepository.save(searchStatus);
        searchStatusSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the searchStatuses.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SearchStatus> findAll() {
        log.debug("Request to get all SearchStatuses");
        return searchStatusRepository.findAll();
    }


    /**
     * Get one searchStatus by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SearchStatus> findOne(Long id) {
        log.debug("Request to get SearchStatus : {}", id);
        return searchStatusRepository.findById(id);
    }

    /**
     * Delete the searchStatus by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SearchStatus : {}", id);
        searchStatusRepository.deleteById(id);
        searchStatusSearchRepository.deleteById(id);
    }

    /**
     * Search for the searchStatus corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SearchStatus> search(String query) {
        log.debug("Request to search SearchStatuses for query {}", query);
        return StreamSupport
            .stream(searchStatusSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
