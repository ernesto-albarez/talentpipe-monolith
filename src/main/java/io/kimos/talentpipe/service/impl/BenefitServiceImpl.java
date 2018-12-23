package io.kimos.talentpipe.service.impl;

import io.kimos.talentpipe.domain.Benefit;
import io.kimos.talentpipe.repository.BenefitRepository;
import io.kimos.talentpipe.repository.search.BenefitSearchRepository;
import io.kimos.talentpipe.service.BenefitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Benefit.
 */
@Service
@Transactional
public class BenefitServiceImpl implements BenefitService {

    private final Logger log = LoggerFactory.getLogger(BenefitServiceImpl.class);

    private final BenefitRepository benefitRepository;

    private final BenefitSearchRepository benefitSearchRepository;

    public BenefitServiceImpl(BenefitRepository benefitRepository, BenefitSearchRepository benefitSearchRepository) {
        this.benefitRepository = benefitRepository;
        this.benefitSearchRepository = benefitSearchRepository;
    }

    /**
     * Save a benefit.
     *
     * @param benefit the entity to save
     * @return the persisted entity
     */
    @Override
    public Benefit save(Benefit benefit) {
        log.debug("Request to save Benefit : {}", benefit);
        Benefit result = benefitRepository.save(benefit);
        benefitSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the benefits.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Benefit> findAll(Pageable pageable) {
        log.debug("Request to get all Benefits");
        return benefitRepository.findAll(pageable);
    }


    /**
     * Get one benefit by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Benefit> findOne(Long id) {
        log.debug("Request to get Benefit : {}", id);
        return benefitRepository.findById(id);
    }

    /**
     * Delete the benefit by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Benefit : {}", id);
        benefitRepository.deleteById(id);
        benefitSearchRepository.deleteById(id);
    }

    /**
     * Search for the benefit corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Benefit> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Benefits for query {}", query);
        return benefitSearchRepository.search(queryStringQuery(query), pageable);
    }
}
