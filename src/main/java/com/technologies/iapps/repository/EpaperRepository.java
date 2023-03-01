package com.technologies.iapps.repository;

import com.technologies.iapps.domain.Epaper;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Epaper entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EpaperRepository extends JpaRepository<Epaper, Long>, JpaSpecificationExecutor<Epaper> {}
