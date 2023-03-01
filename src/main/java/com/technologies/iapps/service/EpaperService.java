package com.technologies.iapps.service;

import com.technologies.iapps.domain.Epaper;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Epaper}.
 */
public interface EpaperService {
    /**
     * Save a epaper.
     *
     * @param epaper the entity to save.
     * @return the persisted entity.
     */
    Epaper save(Epaper epaper);

    /**
     * Parse and save an ePaper entity.
     *
     * @param rawEpaperXml the request body to be parsed and saved.
     * @param filename name of the uploaded file
     * @return the persisted entity.
     */
    Epaper save(String rawEpaperXml, String filename);

    /**
     * Updates a epaper.
     *
     * @param epaper the entity to update.
     * @return the persisted entity.
     */
    Epaper update(Epaper epaper);

    /**
     * Partially updates a epaper.
     *
     * @param epaper the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Epaper> partialUpdate(Epaper epaper);

    /**
     * Get all the epapers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Epaper> findAll(Pageable pageable);

    /**
     * Get the "id" epaper.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Epaper> findOne(Long id);

    /**
     * Delete the "id" epaper.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
