package io.kimos.talentpipe.service.impl;

import io.kimos.talentpipe.domain.Sector;
import io.kimos.talentpipe.repository.SectorRepository;
import io.kimos.talentpipe.repository.search.SectorSearchRepository;
import io.kimos.talentpipe.service.SectorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Sector.
 */
@Service
@Transactional
public class SectorServiceImpl implements SectorService {

    private final Logger log = LoggerFactory.getLogger(SectorServiceImpl.class);

    private final SectorRepository sectorRepository;

    private final SectorSearchRepository sectorSearchRepository;

    public SectorServiceImpl(SectorRepository sectorRepository, SectorSearchRepository sectorSearchRepository) {
        this.sectorRepository = sectorRepository;
        this.sectorSearchRepository = sectorSearchRepository;
    }

    /**
     * Save a sector.
     *
     * @param sector the entity to save
     * @return the persisted entity
     */
    @Override
    public Sector save(Sector sector) {
        log.debug("Request to save Sector : {}", sector);
        Sector result = sectorRepository.save(sector);
        sectorSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the sectors.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Sector> findAll(Pageable pageable) {
        log.debug("Request to get all Sectors");
        return sectorRepository.findAll(pageable);
    }

    /**
     * Get one sector by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Sector> findOne(Long id) {
        log.debug("Request to get Sector : {}", id);
        return sectorRepository.findById(id);
    }

    /**
     * Delete the sector by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sector : {}", id);
        sectorRepository.deleteById(id);
        sectorSearchRepository.deleteById(id);
    }

    /**
     * Search for the sector corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Sector> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Sectors for query {}", query);
        return sectorSearchRepository.search(queryStringQuery(query), pageable);
    }
}
