package io.kimos.talentpipe.service.impl;

import io.kimos.talentpipe.domain.TechnicalSkill;
import io.kimos.talentpipe.repository.TechnicalSkillRepository;
import io.kimos.talentpipe.repository.search.TechnicalSkillSearchRepository;
import io.kimos.talentpipe.service.TechnicalSkillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing TechnicalSkill.
 */
@Service
@Transactional
public class TechnicalSkillServiceImpl implements TechnicalSkillService {

    private final Logger log = LoggerFactory.getLogger(TechnicalSkillServiceImpl.class);

    private final TechnicalSkillRepository technicalSkillRepository;

    private final TechnicalSkillSearchRepository technicalSkillSearchRepository;

    public TechnicalSkillServiceImpl(TechnicalSkillRepository technicalSkillRepository, TechnicalSkillSearchRepository technicalSkillSearchRepository) {
        this.technicalSkillRepository = technicalSkillRepository;
        this.technicalSkillSearchRepository = technicalSkillSearchRepository;
    }

    /**
     * Save a technicalSkill.
     *
     * @param technicalSkill the entity to save
     * @return the persisted entity
     */
    @Override
    public TechnicalSkill save(TechnicalSkill technicalSkill) {
        log.debug("Request to save TechnicalSkill : {}", technicalSkill);
        TechnicalSkill result = technicalSkillRepository.save(technicalSkill);
        technicalSkillSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the technicalSkills.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TechnicalSkill> findAll(Pageable pageable) {
        log.debug("Request to get all TechnicalSkills");
        return technicalSkillRepository.findAll(pageable);
    }


    /**
     * Get one technicalSkill by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TechnicalSkill> findOne(Long id) {
        log.debug("Request to get TechnicalSkill : {}", id);
        return technicalSkillRepository.findById(id);
    }

    /**
     * Delete the technicalSkill by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TechnicalSkill : {}", id);
        technicalSkillRepository.deleteById(id);
        technicalSkillSearchRepository.deleteById(id);
    }

    /**
     * Search for the technicalSkill corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TechnicalSkill> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TechnicalSkills for query {}", query);
        return technicalSkillSearchRepository.search(queryStringQuery(query), pageable);
    }
}
