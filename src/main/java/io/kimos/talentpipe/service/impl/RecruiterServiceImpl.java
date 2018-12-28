package io.kimos.talentpipe.service.impl;

import io.kimos.talentpipe.domain.Recruiter;
import io.kimos.talentpipe.domain.User;
import io.kimos.talentpipe.repository.RecruiterRepository;
import io.kimos.talentpipe.repository.search.RecruiterSearchRepository;
import io.kimos.talentpipe.service.RecruiterService;
import io.kimos.talentpipe.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Recruiter.
 */
@Service
@Transactional
public class RecruiterServiceImpl implements RecruiterService {

    private final Logger log = LoggerFactory.getLogger(RecruiterServiceImpl.class);

    private final RecruiterRepository recruiterRepository;

    private final RecruiterSearchRepository recruiterSearchRepository;

    private final UserService userService;

    public RecruiterServiceImpl(RecruiterRepository recruiterRepository, RecruiterSearchRepository recruiterSearchRepository, UserService userService) {
        this.recruiterRepository = recruiterRepository;
        this.recruiterSearchRepository = recruiterSearchRepository;
        this.userService = userService;
    }

    /**
     * Save a recruiter.
     *
     * @param recruiter the entity to save
     * @return the persisted entity
     */
    @Override
    public Recruiter save(Recruiter recruiter) {
        log.debug("Request to save Recruiter : {}", recruiter);
        Recruiter result = recruiterRepository.save(recruiter);
        recruiterSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the recruiters.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Recruiter> findAll(Pageable pageable) {
        log.debug("Request to get all Recruiters");
        return recruiterRepository.findAll(pageable);
    }

    /**
     * Get one recruiter by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Recruiter> findOne(Long id) {
        log.debug("Request to get Recruiter : {}", id);
        return recruiterRepository.findById(id);
    }

    /**
     * Delete the recruiter by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Recruiter : {}", id);
        recruiterRepository.deleteById(id);
        recruiterSearchRepository.deleteById(id);
    }

    /**
     * Search for the recruiter corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Recruiter> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Recruiters for query {}", query);
        return recruiterSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Override
    public Recruiter registryRecruiter(Recruiter recruiter, String password) {
        log.debug("Request to registry Recruiter : {}", recruiter);
        User user = new User();
        user.setFirstName(recruiter.getName());
        user.setLastName(recruiter.getLastName());
        user.setLogin(recruiter.getEmail());
        user.setEmail(recruiter.getEmail());
        user.setPassword(password);
        recruiter.setUser(userService.registerRecruiterUser(user));
        return this.save(recruiter);
    }
}
