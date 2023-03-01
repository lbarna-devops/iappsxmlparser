package com.technologies.iapps.web.rest;

import com.technologies.iapps.domain.Epaper;
import com.technologies.iapps.repository.EpaperRepository;
import com.technologies.iapps.service.EpaperQueryService;
import com.technologies.iapps.service.EpaperService;
import com.technologies.iapps.service.criteria.EpaperCriteria;
import com.technologies.iapps.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.technologies.iapps.domain.Epaper}.
 */
@RestController
@RequestMapping("/api")
public class EpaperResource {

    private final Logger log = LoggerFactory.getLogger(EpaperResource.class);

    private static final String ENTITY_NAME = "epaper";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EpaperService epaperService;

    private final EpaperRepository epaperRepository;

    private final EpaperQueryService epaperQueryService;

    public EpaperResource(EpaperService epaperService, EpaperRepository epaperRepository, EpaperQueryService epaperQueryService) {
        this.epaperService = epaperService;
        this.epaperRepository = epaperRepository;
        this.epaperQueryService = epaperQueryService;
    }

    /**
     * {@code POST  /epapers} : Create a new epaper.
     *
     * @param epaper the epaper to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new epaper, or with status {@code 400 (Bad Request)} if the epaper has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/epapers")
    public ResponseEntity<Epaper> createEpaper(@RequestBody Epaper epaper) throws URISyntaxException {
        log.debug("REST request to save Epaper : {}", epaper);
        if (epaper.getId() != null) {
            throw new BadRequestAlertException("A new epaper cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Epaper result = epaperService.save(epaper);
        return ResponseEntity
            .created(new URI("/api/epapers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/parseepaper")
    public ResponseEntity<Epaper> parseEpaper(@RequestBody String rawEpaperXml, @RequestParam String filename) throws URISyntaxException {
        log.debug("REST request to save Epaper : {}", rawEpaperXml);
        Epaper result = epaperService.save(rawEpaperXml, filename);
        return ResponseEntity
            .created(new URI("/api/epapers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /epapers/:id} : Updates an existing epaper.
     *
     * @param id the id of the epaper to save.
     * @param epaper the epaper to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated epaper,
     * or with status {@code 400 (Bad Request)} if the epaper is not valid,
     * or with status {@code 500 (Internal Server Error)} if the epaper couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/epapers/{id}")
    public ResponseEntity<Epaper> updateEpaper(@PathVariable(value = "id", required = false) final Long id, @RequestBody Epaper epaper)
        throws URISyntaxException {
        log.debug("REST request to update Epaper : {}, {}", id, epaper);
        if (epaper.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, epaper.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!epaperRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Epaper result = epaperService.update(epaper);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, epaper.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /epapers/:id} : Partial updates given fields of an existing epaper, field will ignore if it is null
     *
     * @param id the id of the epaper to save.
     * @param epaper the epaper to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated epaper,
     * or with status {@code 400 (Bad Request)} if the epaper is not valid,
     * or with status {@code 404 (Not Found)} if the epaper is not found,
     * or with status {@code 500 (Internal Server Error)} if the epaper couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/epapers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Epaper> partialUpdateEpaper(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Epaper epaper
    ) throws URISyntaxException {
        log.debug("REST request to partial update Epaper partially : {}, {}", id, epaper);
        if (epaper.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, epaper.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!epaperRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Epaper> result = epaperService.partialUpdate(epaper);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, epaper.getId().toString())
        );
    }

    /**
     * {@code GET  /epapers} : get all the epapers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of epapers in body.
     */
    @GetMapping("/epapers")
    public ResponseEntity<List<Epaper>> getAllEpapers(
        EpaperCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Epapers by criteria: {}", criteria);
        Page<Epaper> page = epaperQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /epapers/count} : count all the epapers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/epapers/count")
    public ResponseEntity<Long> countEpapers(EpaperCriteria criteria) {
        log.debug("REST request to count Epapers by criteria: {}", criteria);
        return ResponseEntity.ok().body(epaperQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /epapers/:id} : get the "id" epaper.
     *
     * @param id the id of the epaper to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the epaper, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/epapers/{id}")
    public ResponseEntity<Epaper> getEpaper(@PathVariable Long id) {
        log.debug("REST request to get Epaper : {}", id);
        Optional<Epaper> epaper = epaperService.findOne(id);
        return ResponseUtil.wrapOrNotFound(epaper);
    }

    /**
     * {@code DELETE  /epapers/:id} : delete the "id" epaper.
     *
     * @param id the id of the epaper to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/epapers/{id}")
    public ResponseEntity<Void> deleteEpaper(@PathVariable Long id) {
        log.debug("REST request to delete Epaper : {}", id);
        epaperService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
