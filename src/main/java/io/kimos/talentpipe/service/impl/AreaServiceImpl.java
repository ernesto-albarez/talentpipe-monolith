package io.kimos.talentpipe.service.impl;

import io.kimos.talentpipe.domain.Area;
import io.kimos.talentpipe.repository.AreaRepository;
import io.kimos.talentpipe.repository.search.AreaSearchRepository;
import io.kimos.talentpipe.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Area.
 */
@Service
@Transactional
public class AreaServiceImpl implements AreaService {

    private final Logger log = LoggerFactory.getLogger(AreaServiceImpl.class);

    private final AreaRepository areaRepository;

    private final AreaSearchRepository areaSearchRepository;

    public AreaServiceImpl(AreaRepository areaRepository, AreaSearchRepository areaSearchRepository) {
        this.areaRepository = areaRepository;
        this.areaSearchRepository = areaSearchRepository;
    }

    /**
     * Save a area.
     *
     * @param area the entity to save
     * @return the persisted entity
     */
    @Override
    public Area save(Area area) {
        log.debug("Request to save Area : {}", area);
        Area result = areaRepository.save(area);
        areaSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the areas.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Area> findAll(Pageable pageable) {
        log.debug("Request to get all Areas");
        return areaRepository.findAll(pageable);
    }

    /**
     * Get one area by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Area> findOne(Long id) {
        log.debug("Request to get Area : {}", id);
        return areaRepository.findById(id);
    }

    /**
     * Delete the area by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Area : {}", id);
        areaRepository.deleteById(id);
        areaSearchRepository.deleteById(id);
    }

    /**
     * Search for the area corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Area> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Areas for query {}", query);
        return areaSearchRepository.search(queryStringQuery(query), pageable);
    }
}
