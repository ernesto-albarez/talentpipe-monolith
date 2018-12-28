package io.kimos.talentpipe.service.impl;

import io.kimos.talentpipe.domain.CompanyType;
import io.kimos.talentpipe.repository.CompanyTypeRepository;
import io.kimos.talentpipe.repository.search.CompanyTypeSearchRepository;
import io.kimos.talentpipe.service.CompanyTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing CompanyType.
 */
@Service
@Transactional
public class CompanyTypeServiceImpl implements CompanyTypeService {

    private final Logger log = LoggerFactory.getLogger(CompanyTypeServiceImpl.class);

    private final CompanyTypeRepository companyTypeRepository;

    private final CompanyTypeSearchRepository companyTypeSearchRepository;

    public CompanyTypeServiceImpl(CompanyTypeRepository companyTypeRepository, CompanyTypeSearchRepository companyTypeSearchRepository) {
        this.companyTypeRepository = companyTypeRepository;
        this.companyTypeSearchRepository = companyTypeSearchRepository;
    }

    /**
     * Save a companyType.
     *
     * @param companyType the entity to save
     * @return the persisted entity
     */
    @Override
    public CompanyType save(CompanyType companyType) {
        log.debug("Request to save CompanyType : {}", companyType);
        CompanyType result = companyTypeRepository.save(companyType);
        companyTypeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the companyTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CompanyType> findAll(Pageable pageable) {
        log.debug("Request to get all CompanyTypes");
        return companyTypeRepository.findAll(pageable);
    }

    /**
     * Get one companyType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CompanyType> findOne(Long id) {
        log.debug("Request to get CompanyType : {}", id);
        return companyTypeRepository.findById(id);
    }

    /**
     * Delete the companyType by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CompanyType : {}", id);
        companyTypeRepository.deleteById(id);
        companyTypeSearchRepository.deleteById(id);
    }

    /**
     * Search for the companyType corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CompanyType> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CompanyTypes for query {}", query);
        return companyTypeSearchRepository.search(queryStringQuery(query), pageable);
    }
}
