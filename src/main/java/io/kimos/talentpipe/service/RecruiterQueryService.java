package io.kimos.talentpipe.service;

import io.github.jhipster.service.QueryService;
import io.kimos.talentpipe.domain.City_;
import io.kimos.talentpipe.domain.Recruiter;
import io.kimos.talentpipe.domain.Recruiter_;
import io.kimos.talentpipe.domain.Sector_;
import io.kimos.talentpipe.repository.RecruiterRepository;
import io.kimos.talentpipe.repository.search.RecruiterSearchRepository;
import io.kimos.talentpipe.service.dto.RecruiterCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for executing complex queries for Recruiter entities in the database.
 * The main input is a {@link RecruiterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Recruiter} or a {@link Page} of {@link Recruiter} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RecruiterQueryService extends QueryService<Recruiter> {

    private final Logger log = LoggerFactory.getLogger(RecruiterQueryService.class);

    private final RecruiterRepository recruiterRepository;

    private final RecruiterSearchRepository recruiterSearchRepository;

    public RecruiterQueryService(RecruiterRepository recruiterRepository, RecruiterSearchRepository recruiterSearchRepository) {
        this.recruiterRepository = recruiterRepository;
        this.recruiterSearchRepository = recruiterSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Recruiter} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Recruiter> findByCriteria(RecruiterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Recruiter> specification = createSpecification(criteria);
        return recruiterRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Recruiter} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Recruiter> findByCriteria(RecruiterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Recruiter> specification = createSpecification(criteria);
        return recruiterRepository.findAll(specification, page);
    }

    /**
     * Function to convert RecruiterCriteria to a {@link Specification}
     */
    private Specification<Recruiter> createSpecification(RecruiterCriteria criteria) {
        Specification<Recruiter> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Recruiter_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Recruiter_.name));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Recruiter_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Recruiter_.email));
            }
            if (criteria.getTaxId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTaxId(), Recruiter_.taxId));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Recruiter_.phoneNumber));
            }
            if (criteria.getStreet() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreet(), Recruiter_.street));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumber(), Recruiter_.number));
            }
            if (criteria.getFloor() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFloor(), Recruiter_.floor));
            }
            if (criteria.getApartment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getApartment(), Recruiter_.apartment));
            }
            if (criteria.getCityId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCityId(), Recruiter_.city, City_.id));
            }
            if (criteria.getSectorId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSectorId(), Recruiter_.sector, Sector_.id));
            }
        }
        return specification;
    }
}
