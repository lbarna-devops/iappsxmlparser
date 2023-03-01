package com.technologies.iapps.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.technologies.iapps.domain.Epaper;
import com.technologies.iapps.repository.EpaperRepository;
import com.technologies.iapps.service.EpaperService;
import com.technologies.iapps.web.rest.dto.EpaperRequest;
import com.technologies.iapps.web.rest.dto.ScreenInfo;
import java.time.Instant;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Epaper}.
 */
@Service
@Transactional
public class EpaperServiceImpl implements EpaperService {

    private final Logger log = LoggerFactory.getLogger(EpaperServiceImpl.class);

    private final EpaperRepository epaperRepository;

    public EpaperServiceImpl(EpaperRepository epaperRepository) {
        this.epaperRepository = epaperRepository;
    }

    @Override
    public Epaper save(Epaper epaper) {
        log.debug("Request to save Epaper : {}", epaper);
        return epaperRepository.save(epaper);
    }

    @Override
    public Epaper save(String rawEpaperXml, String filename) {
        log.debug("Request to save Epaper : {}", rawEpaperXml);
        Epaper epaper = parseEpaperXml(rawEpaperXml);
        epaper.setFilename(filename);
        return epaperRepository.save(epaper);
    }

    private Epaper parseEpaperXml(String rawEpaperXml) {
        XmlMapper xmlMapper = new XmlMapper();
        EpaperRequest xmlObject = null;
        try {
            xmlObject = xmlMapper.readValue(rawEpaperXml, EpaperRequest.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse request: {}", rawEpaperXml);
        }
        return convertXmlbjectToEpaper(xmlObject);
    }

    private Epaper convertXmlbjectToEpaper(EpaperRequest xmlObject) {
        Epaper epaper = new Epaper();
        epaper.setNewspaperName(xmlObject.getDeviceInfo().getAppInfo().getNewspaperName());
        ScreenInfo screenInfo = xmlObject.getDeviceInfo().getScreenInfo();
        epaper.setWidth(screenInfo.getWidth());
        epaper.setHeight(screenInfo.getHeight());
        epaper.setDpi(screenInfo.getDpi());
        epaper.setUploadtime(Instant.now().toString());

        return epaper;
    }

    @Override
    public Epaper update(Epaper epaper) {
        log.debug("Request to update Epaper : {}", epaper);
        return epaperRepository.save(epaper);
    }

    @Override
    public Optional<Epaper> partialUpdate(Epaper epaper) {
        log.debug("Request to partially update Epaper : {}", epaper);

        return epaperRepository
            .findById(epaper.getId())
            .map(existingEpaper -> {
                if (epaper.getNewspaperName() != null) {
                    existingEpaper.setNewspaperName(epaper.getNewspaperName());
                }
                if (epaper.getWidth() != null) {
                    existingEpaper.setWidth(epaper.getWidth());
                }
                if (epaper.getHeight() != null) {
                    existingEpaper.setHeight(epaper.getHeight());
                }
                if (epaper.getDpi() != null) {
                    existingEpaper.setDpi(epaper.getDpi());
                }
                if (epaper.getUploadtime() != null) {
                    existingEpaper.setUploadtime(epaper.getUploadtime());
                }
                if (epaper.getFilename() != null) {
                    existingEpaper.setFilename(epaper.getFilename());
                }

                return existingEpaper;
            })
            .map(epaperRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Epaper> findAll(Pageable pageable) {
        log.debug("Request to get all Epapers");
        return epaperRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Epaper> findOne(Long id) {
        log.debug("Request to get Epaper : {}", id);
        return epaperRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Epaper : {}", id);
        epaperRepository.deleteById(id);
    }
}
