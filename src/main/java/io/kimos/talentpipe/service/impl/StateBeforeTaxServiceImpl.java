package io.kimos.talentpipe.service.impl;

import io.kimos.talentpipe.domain.StateBeforeTax;
import io.kimos.talentpipe.repository.StateBeforeTaxRepository;
import io.kimos.talentpipe.repository.search.StateBeforeTaxSearchRepository;
import io.kimos.talentpipe.service.StateBeforeTaxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing StateBeforeTax.
 */
@Service
@Transactional
public class StateBeforeTaxServiceImpl implements StateBeforeTaxService {

    private final Logger log = LoggerFactory.getLogger(StateBeforeTaxServiceImpl.class);

    private final StateBeforeTaxRepository stateBeforeTaxRepository;

    private final StateBeforeTaxSearchRepository stateBeforeTaxSearchRepository;

    public StateBeforeTaxServiceImpl(StateBeforeTaxRepository stateBeforeTaxRepository, StateBeforeTaxSearchRepository stateBeforeTaxSearchRepository) {
        this.stateBeforeTaxRepository = stateBeforeTaxRepository;
        this.stateBeforeTaxSearchRepository = stateBeforeTaxSearchRepository;
    }

    /**
     * Save a stateBeforeTax.
     *
     * @param stateBeforeTax the entity to save
     * @return the persisted entity
     */
    @Override
    public StateBeforeTax save(StateBeforeTax stateBeforeTax) {
        log.debug("Request to save StateBeforeTax : {}", stateBeforeTax);
        StateBeforeTax result = stateBeforeTaxRepository.save(stateBeforeTax);
        stateBeforeTaxSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the stateBeforeTaxes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StateBeforeTax> findAll(Pageable pageable) {
        log.debug("Request to get all StateBeforeTaxes");
        return stateBeforeTaxRepository.findAll(pageable);
    }


    /**
     * Get one stateBeforeTax by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StateBeforeTax> findOne(Long id) {
        log.debug("Request to get StateBeforeTax : {}", id);
        return stateBeforeTaxRepository.findById(id);
    }

    /**
     * Delete the stateBeforeTax by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StateBeforeTax : {}", id);
        stateBeforeTaxRepository.deleteById(id);
        stateBeforeTaxSearchRepository.deleteById(id);
    }

    /**
     * Search for the stateBeforeTax corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StateBeforeTax> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StateBeforeTaxes for query {}", query);
        return stateBeforeTaxSearchRepository.search(queryStringQuery(query), pageable);
    }
}
