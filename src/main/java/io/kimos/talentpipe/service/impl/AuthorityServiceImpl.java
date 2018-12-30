package io.kimos.talentpipe.service.impl;

import io.kimos.talentpipe.service.AuthorityService;
import io.kimos.talentpipe.domain.Authority;
import io.kimos.talentpipe.repository.AuthorityRepository;
import io.kimos.talentpipe.repository.search.AuthoritySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Authority.
 */
@Service
@Transactional
public class AuthorityServiceImpl implements AuthorityService {

    private final Logger log = LoggerFactory.getLogger(AuthorityServiceImpl.class);

    private final AuthorityRepository authorityRepository;

    private final AuthoritySearchRepository authoritySearchRepository;

    public AuthorityServiceImpl(AuthorityRepository authorityRepository, AuthoritySearchRepository authoritySearchRepository) {
        this.authorityRepository = authorityRepository;
        this.authoritySearchRepository = authoritySearchRepository;
    }

    /**
     * Save a authority.
     *
     * @param authority the entity to save
     * @return the persisted entity
     */
    @Override
    public Authority save(Authority authority) {
        log.debug("Request to save Authority : {}", authority);
        Authority result = authorityRepository.save(authority);
        authoritySearchRepository.save(result);
        return result;
    }

    /**
     * Get all the authorities.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Authority> findAll(Pageable pageable) {
        log.debug("Request to get all Authorities");
        return authorityRepository.findAll(pageable);
    }


    /**
     * Get one authority by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Authority> findOne(Long id) {
        log.debug("Request to get Authority : {}", id);
        return authorityRepository.findById(id);
    }

    /**
     * Delete the authority by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Authority : {}", id);
        authorityRepository.deleteById(id);
        authoritySearchRepository.deleteById(id);
    }

    /**
     * Search for the authority corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Authority> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Authorities for query {}", query);
        return authoritySearchRepository.search(queryStringQuery(query), pageable);    }
}
