package com.technologies.iapps.service;

import com.technologies.iapps.domain.*; // for static metamodels
import com.technologies.iapps.domain.Epaper;
import com.technologies.iapps.repository.EpaperRepository;
import com.technologies.iapps.service.criteria.EpaperCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Epaper} entities in the database.
 * The main input is a {@link EpaperCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Epaper} or a {@link Page} of {@link Epaper} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EpaperQueryService extends QueryService<Epaper> {

    private final Logger log = LoggerFactory.getLogger(EpaperQueryService.class);

    private final EpaperRepository epaperRepository;

    public EpaperQueryService(EpaperRepository epaperRepository) {
        this.epaperRepository = epaperRepository;
    }

    /**
     * Return a {@link List} of {@link Epaper} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Epaper> findByCriteria(EpaperCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Epaper> specification = createSpecification(criteria);
        return epaperRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Epaper} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Epaper> findByCriteria(EpaperCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Epaper> specification = createSpecification(criteria);
        return epaperRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EpaperCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Epaper> specification = createSpecification(criteria);
        return epaperRepository.count(specification);
    }

    /**
     * Function to convert {@link EpaperCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Epaper> createSpecification(EpaperCriteria criteria) {
        Specification<Epaper> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Epaper_.id));
            }
            if (criteria.getNewspaperName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNewspaperName(), Epaper_.newspaperName));
            }
            if (criteria.getWidth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWidth(), Epaper_.width));
            }
            if (criteria.getHeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHeight(), Epaper_.height));
            }
            if (criteria.getDpi() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDpi(), Epaper_.dpi));
            }
            if (criteria.getUploadtime() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUploadtime(), Epaper_.uploadtime));
            }
            if (criteria.getFilename() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFilename(), Epaper_.filename));
            }
        }
        return specification;
    }
}
