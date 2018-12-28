package io.kimos.talentpipe.service.impl;

import io.kimos.talentpipe.domain.WorkType;
import io.kimos.talentpipe.repository.WorkTypeRepository;
import io.kimos.talentpipe.repository.search.WorkTypeSearchRepository;
import io.kimos.talentpipe.service.WorkTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing WorkType.
 */
@Service
@Transactional
public class WorkTypeServiceImpl implements WorkTypeService {

    private final Logger log = LoggerFactory.getLogger(WorkTypeServiceImpl.class);

    private final WorkTypeRepository workTypeRepository;

    private final WorkTypeSearchRepository workTypeSearchRepository;

    public WorkTypeServiceImpl(WorkTypeRepository workTypeRepository, WorkTypeSearchRepository workTypeSearchRepository) {
        this.workTypeRepository = workTypeRepository;
        this.workTypeSearchRepository = workTypeSearchRepository;
    }

    /**
     * Save a workType.
     *
     * @param workType the entity to save
     * @return the persisted entity
     */
    @Override
    public WorkType save(WorkType workType) {
        log.debug("Request to save WorkType : {}", workType);
        WorkType result = workTypeRepository.save(workType);
        workTypeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the workTypes.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<WorkType> findAll() {
        log.debug("Request to get all WorkTypes");
        return workTypeRepository.findAll();
    }

    /**
     * Get one workType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<WorkType> findOne(Long id) {
        log.debug("Request to get WorkType : {}", id);
        return workTypeRepository.findById(id);
    }

    /**
     * Delete the workType by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkType : {}", id);
        workTypeRepository.deleteById(id);
        workTypeSearchRepository.deleteById(id);
    }

    /**
     * Search for the workType corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<WorkType> search(String query) {
        log.debug("Request to search WorkTypes for query {}", query);
        return StreamSupport
            .stream(workTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
