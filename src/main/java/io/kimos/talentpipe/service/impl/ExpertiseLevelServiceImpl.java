package io.kimos.talentpipe.service.impl;

import io.kimos.talentpipe.service.ExpertiseLevelService;
import io.kimos.talentpipe.domain.ExpertiseLevel;
import io.kimos.talentpipe.repository.ExpertiseLevelRepository;
import io.kimos.talentpipe.repository.search.ExpertiseLevelSearchRepository;
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
 * Service Implementation for managing ExpertiseLevel.
 */
@Service
@Transactional
public class ExpertiseLevelServiceImpl implements ExpertiseLevelService {

    private final Logger log = LoggerFactory.getLogger(ExpertiseLevelServiceImpl.class);

    private final ExpertiseLevelRepository expertiseLevelRepository;

    private final ExpertiseLevelSearchRepository expertiseLevelSearchRepository;

    public ExpertiseLevelServiceImpl(ExpertiseLevelRepository expertiseLevelRepository, ExpertiseLevelSearchRepository expertiseLevelSearchRepository) {
        this.expertiseLevelRepository = expertiseLevelRepository;
        this.expertiseLevelSearchRepository = expertiseLevelSearchRepository;
    }

    /**
     * Save a expertiseLevel.
     *
     * @param expertiseLevel the entity to save
     * @return the persisted entity
     */
    @Override
    public ExpertiseLevel save(ExpertiseLevel expertiseLevel) {
        log.debug("Request to save ExpertiseLevel : {}", expertiseLevel);
        ExpertiseLevel result = expertiseLevelRepository.save(expertiseLevel);
        expertiseLevelSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the expertiseLevels.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ExpertiseLevel> findAll() {
        log.debug("Request to get all ExpertiseLevels");
        return expertiseLevelRepository.findAll();
    }


    /**
     * Get one expertiseLevel by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ExpertiseLevel> findOne(Long id) {
        log.debug("Request to get ExpertiseLevel : {}", id);
        return expertiseLevelRepository.findById(id);
    }

    /**
     * Delete the expertiseLevel by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExpertiseLevel : {}", id);
        expertiseLevelRepository.deleteById(id);
        expertiseLevelSearchRepository.deleteById(id);
    }

    /**
     * Search for the expertiseLevel corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ExpertiseLevel> search(String query) {
        log.debug("Request to search ExpertiseLevels for query {}", query);
        return StreamSupport
            .stream(expertiseLevelSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
