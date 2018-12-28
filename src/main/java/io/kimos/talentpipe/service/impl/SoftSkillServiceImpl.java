package io.kimos.talentpipe.service.impl;

import io.kimos.talentpipe.domain.SoftSkill;
import io.kimos.talentpipe.repository.SoftSkillRepository;
import io.kimos.talentpipe.repository.search.SoftSkillSearchRepository;
import io.kimos.talentpipe.service.SoftSkillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing SoftSkill.
 */
@Service
@Transactional
public class SoftSkillServiceImpl implements SoftSkillService {

    private final Logger log = LoggerFactory.getLogger(SoftSkillServiceImpl.class);

    private final SoftSkillRepository softSkillRepository;

    private final SoftSkillSearchRepository softSkillSearchRepository;

    public SoftSkillServiceImpl(SoftSkillRepository softSkillRepository, SoftSkillSearchRepository softSkillSearchRepository) {
        this.softSkillRepository = softSkillRepository;
        this.softSkillSearchRepository = softSkillSearchRepository;
    }

    /**
     * Save a softSkill.
     *
     * @param softSkill the entity to save
     * @return the persisted entity
     */
    @Override
    public SoftSkill save(SoftSkill softSkill) {
        log.debug("Request to save SoftSkill : {}", softSkill);
        SoftSkill result = softSkillRepository.save(softSkill);
        softSkillSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the softSkills.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SoftSkill> findAll(Pageable pageable) {
        log.debug("Request to get all SoftSkills");
        return softSkillRepository.findAll(pageable);
    }

    /**
     * Get one softSkill by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SoftSkill> findOne(Long id) {
        log.debug("Request to get SoftSkill : {}", id);
        return softSkillRepository.findById(id);
    }

    /**
     * Delete the softSkill by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SoftSkill : {}", id);
        softSkillRepository.deleteById(id);
        softSkillSearchRepository.deleteById(id);
    }

    /**
     * Search for the softSkill corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SoftSkill> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SoftSkills for query {}", query);
        return softSkillSearchRepository.search(queryStringQuery(query), pageable);
    }
}
