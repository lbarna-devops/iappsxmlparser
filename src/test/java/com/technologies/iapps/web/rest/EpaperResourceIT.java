package com.technologies.iapps.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.technologies.iapps.IntegrationTest;
import com.technologies.iapps.domain.Epaper;
import com.technologies.iapps.repository.EpaperRepository;
import com.technologies.iapps.service.criteria.EpaperCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EpaperResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EpaperResourceIT {

    private static final String DEFAULT_NEWSPAPER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NEWSPAPER_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_WIDTH = 1;
    private static final Integer UPDATED_WIDTH = 2;
    private static final Integer SMALLER_WIDTH = 1 - 1;

    private static final Integer DEFAULT_HEIGHT = 1;
    private static final Integer UPDATED_HEIGHT = 2;
    private static final Integer SMALLER_HEIGHT = 1 - 1;

    private static final Integer DEFAULT_DPI = 1;
    private static final Integer UPDATED_DPI = 2;
    private static final Integer SMALLER_DPI = 1 - 1;

    private static final String DEFAULT_UPLOADTIME = "AAAAAAAAAA";
    private static final String UPDATED_UPLOADTIME = "BBBBBBBBBB";

    private static final String DEFAULT_FILENAME = "AAAAAAAAAA";
    private static final String UPDATED_FILENAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/epapers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EpaperRepository epaperRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEpaperMockMvc;

    private Epaper epaper;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Epaper createEntity(EntityManager em) {
        Epaper epaper = new Epaper()
            .newspaperName(DEFAULT_NEWSPAPER_NAME)
            .width(DEFAULT_WIDTH)
            .height(DEFAULT_HEIGHT)
            .dpi(DEFAULT_DPI)
            .uploadtime(DEFAULT_UPLOADTIME)
            .filename(DEFAULT_FILENAME);
        return epaper;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Epaper createUpdatedEntity(EntityManager em) {
        Epaper epaper = new Epaper()
            .newspaperName(UPDATED_NEWSPAPER_NAME)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .dpi(UPDATED_DPI)
            .uploadtime(UPDATED_UPLOADTIME)
            .filename(UPDATED_FILENAME);
        return epaper;
    }

    @BeforeEach
    public void initTest() {
        epaper = createEntity(em);
    }

    @Test
    @Transactional
    void createEpaper() throws Exception {
        int databaseSizeBeforeCreate = epaperRepository.findAll().size();
        // Create the Epaper
        restEpaperMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(epaper)))
            .andExpect(status().isCreated());

        // Validate the Epaper in the database
        List<Epaper> epaperList = epaperRepository.findAll();
        assertThat(epaperList).hasSize(databaseSizeBeforeCreate + 1);
        Epaper testEpaper = epaperList.get(epaperList.size() - 1);
        assertThat(testEpaper.getNewspaperName()).isEqualTo(DEFAULT_NEWSPAPER_NAME);
        assertThat(testEpaper.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testEpaper.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testEpaper.getDpi()).isEqualTo(DEFAULT_DPI);
        assertThat(testEpaper.getUploadtime()).isEqualTo(DEFAULT_UPLOADTIME);
        assertThat(testEpaper.getFilename()).isEqualTo(DEFAULT_FILENAME);
    }

    @Test
    @Transactional
    void createEpaperWithExistingId() throws Exception {
        // Create the Epaper with an existing ID
        epaper.setId(1L);

        int databaseSizeBeforeCreate = epaperRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEpaperMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(epaper)))
            .andExpect(status().isBadRequest());

        // Validate the Epaper in the database
        List<Epaper> epaperList = epaperRepository.findAll();
        assertThat(epaperList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEpapers() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList
        restEpaperMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(epaper.getId().intValue())))
            .andExpect(jsonPath("$.[*].newspaperName").value(hasItem(DEFAULT_NEWSPAPER_NAME)))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].dpi").value(hasItem(DEFAULT_DPI)))
            .andExpect(jsonPath("$.[*].uploadtime").value(hasItem(DEFAULT_UPLOADTIME)))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME)));
    }

    @Test
    @Transactional
    void getEpaper() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get the epaper
        restEpaperMockMvc
            .perform(get(ENTITY_API_URL_ID, epaper.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(epaper.getId().intValue()))
            .andExpect(jsonPath("$.newspaperName").value(DEFAULT_NEWSPAPER_NAME))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.dpi").value(DEFAULT_DPI))
            .andExpect(jsonPath("$.uploadtime").value(DEFAULT_UPLOADTIME))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME));
    }

    @Test
    @Transactional
    void getEpapersByIdFiltering() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        Long id = epaper.getId();

        defaultEpaperShouldBeFound("id.equals=" + id);
        defaultEpaperShouldNotBeFound("id.notEquals=" + id);

        defaultEpaperShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEpaperShouldNotBeFound("id.greaterThan=" + id);

        defaultEpaperShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEpaperShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEpapersByNewspaperNameIsEqualToSomething() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where newspaperName equals to DEFAULT_NEWSPAPER_NAME
        defaultEpaperShouldBeFound("newspaperName.equals=" + DEFAULT_NEWSPAPER_NAME);

        // Get all the epaperList where newspaperName equals to UPDATED_NEWSPAPER_NAME
        defaultEpaperShouldNotBeFound("newspaperName.equals=" + UPDATED_NEWSPAPER_NAME);
    }

    @Test
    @Transactional
    void getAllEpapersByNewspaperNameIsInShouldWork() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where newspaperName in DEFAULT_NEWSPAPER_NAME or UPDATED_NEWSPAPER_NAME
        defaultEpaperShouldBeFound("newspaperName.in=" + DEFAULT_NEWSPAPER_NAME + "," + UPDATED_NEWSPAPER_NAME);

        // Get all the epaperList where newspaperName equals to UPDATED_NEWSPAPER_NAME
        defaultEpaperShouldNotBeFound("newspaperName.in=" + UPDATED_NEWSPAPER_NAME);
    }

    @Test
    @Transactional
    void getAllEpapersByNewspaperNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where newspaperName is not null
        defaultEpaperShouldBeFound("newspaperName.specified=true");

        // Get all the epaperList where newspaperName is null
        defaultEpaperShouldNotBeFound("newspaperName.specified=false");
    }

    @Test
    @Transactional
    void getAllEpapersByNewspaperNameContainsSomething() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where newspaperName contains DEFAULT_NEWSPAPER_NAME
        defaultEpaperShouldBeFound("newspaperName.contains=" + DEFAULT_NEWSPAPER_NAME);

        // Get all the epaperList where newspaperName contains UPDATED_NEWSPAPER_NAME
        defaultEpaperShouldNotBeFound("newspaperName.contains=" + UPDATED_NEWSPAPER_NAME);
    }

    @Test
    @Transactional
    void getAllEpapersByNewspaperNameNotContainsSomething() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where newspaperName does not contain DEFAULT_NEWSPAPER_NAME
        defaultEpaperShouldNotBeFound("newspaperName.doesNotContain=" + DEFAULT_NEWSPAPER_NAME);

        // Get all the epaperList where newspaperName does not contain UPDATED_NEWSPAPER_NAME
        defaultEpaperShouldBeFound("newspaperName.doesNotContain=" + UPDATED_NEWSPAPER_NAME);
    }

    @Test
    @Transactional
    void getAllEpapersByWidthIsEqualToSomething() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where width equals to DEFAULT_WIDTH
        defaultEpaperShouldBeFound("width.equals=" + DEFAULT_WIDTH);

        // Get all the epaperList where width equals to UPDATED_WIDTH
        defaultEpaperShouldNotBeFound("width.equals=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllEpapersByWidthIsInShouldWork() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where width in DEFAULT_WIDTH or UPDATED_WIDTH
        defaultEpaperShouldBeFound("width.in=" + DEFAULT_WIDTH + "," + UPDATED_WIDTH);

        // Get all the epaperList where width equals to UPDATED_WIDTH
        defaultEpaperShouldNotBeFound("width.in=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllEpapersByWidthIsNullOrNotNull() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where width is not null
        defaultEpaperShouldBeFound("width.specified=true");

        // Get all the epaperList where width is null
        defaultEpaperShouldNotBeFound("width.specified=false");
    }

    @Test
    @Transactional
    void getAllEpapersByWidthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where width is greater than or equal to DEFAULT_WIDTH
        defaultEpaperShouldBeFound("width.greaterThanOrEqual=" + DEFAULT_WIDTH);

        // Get all the epaperList where width is greater than or equal to UPDATED_WIDTH
        defaultEpaperShouldNotBeFound("width.greaterThanOrEqual=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllEpapersByWidthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where width is less than or equal to DEFAULT_WIDTH
        defaultEpaperShouldBeFound("width.lessThanOrEqual=" + DEFAULT_WIDTH);

        // Get all the epaperList where width is less than or equal to SMALLER_WIDTH
        defaultEpaperShouldNotBeFound("width.lessThanOrEqual=" + SMALLER_WIDTH);
    }

    @Test
    @Transactional
    void getAllEpapersByWidthIsLessThanSomething() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where width is less than DEFAULT_WIDTH
        defaultEpaperShouldNotBeFound("width.lessThan=" + DEFAULT_WIDTH);

        // Get all the epaperList where width is less than UPDATED_WIDTH
        defaultEpaperShouldBeFound("width.lessThan=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllEpapersByWidthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where width is greater than DEFAULT_WIDTH
        defaultEpaperShouldNotBeFound("width.greaterThan=" + DEFAULT_WIDTH);

        // Get all the epaperList where width is greater than SMALLER_WIDTH
        defaultEpaperShouldBeFound("width.greaterThan=" + SMALLER_WIDTH);
    }

    @Test
    @Transactional
    void getAllEpapersByHeightIsEqualToSomething() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where height equals to DEFAULT_HEIGHT
        defaultEpaperShouldBeFound("height.equals=" + DEFAULT_HEIGHT);

        // Get all the epaperList where height equals to UPDATED_HEIGHT
        defaultEpaperShouldNotBeFound("height.equals=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllEpapersByHeightIsInShouldWork() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where height in DEFAULT_HEIGHT or UPDATED_HEIGHT
        defaultEpaperShouldBeFound("height.in=" + DEFAULT_HEIGHT + "," + UPDATED_HEIGHT);

        // Get all the epaperList where height equals to UPDATED_HEIGHT
        defaultEpaperShouldNotBeFound("height.in=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllEpapersByHeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where height is not null
        defaultEpaperShouldBeFound("height.specified=true");

        // Get all the epaperList where height is null
        defaultEpaperShouldNotBeFound("height.specified=false");
    }

    @Test
    @Transactional
    void getAllEpapersByHeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where height is greater than or equal to DEFAULT_HEIGHT
        defaultEpaperShouldBeFound("height.greaterThanOrEqual=" + DEFAULT_HEIGHT);

        // Get all the epaperList where height is greater than or equal to UPDATED_HEIGHT
        defaultEpaperShouldNotBeFound("height.greaterThanOrEqual=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllEpapersByHeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where height is less than or equal to DEFAULT_HEIGHT
        defaultEpaperShouldBeFound("height.lessThanOrEqual=" + DEFAULT_HEIGHT);

        // Get all the epaperList where height is less than or equal to SMALLER_HEIGHT
        defaultEpaperShouldNotBeFound("height.lessThanOrEqual=" + SMALLER_HEIGHT);
    }

    @Test
    @Transactional
    void getAllEpapersByHeightIsLessThanSomething() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where height is less than DEFAULT_HEIGHT
        defaultEpaperShouldNotBeFound("height.lessThan=" + DEFAULT_HEIGHT);

        // Get all the epaperList where height is less than UPDATED_HEIGHT
        defaultEpaperShouldBeFound("height.lessThan=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllEpapersByHeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where height is greater than DEFAULT_HEIGHT
        defaultEpaperShouldNotBeFound("height.greaterThan=" + DEFAULT_HEIGHT);

        // Get all the epaperList where height is greater than SMALLER_HEIGHT
        defaultEpaperShouldBeFound("height.greaterThan=" + SMALLER_HEIGHT);
    }

    @Test
    @Transactional
    void getAllEpapersByDpiIsEqualToSomething() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where dpi equals to DEFAULT_DPI
        defaultEpaperShouldBeFound("dpi.equals=" + DEFAULT_DPI);

        // Get all the epaperList where dpi equals to UPDATED_DPI
        defaultEpaperShouldNotBeFound("dpi.equals=" + UPDATED_DPI);
    }

    @Test
    @Transactional
    void getAllEpapersByDpiIsInShouldWork() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where dpi in DEFAULT_DPI or UPDATED_DPI
        defaultEpaperShouldBeFound("dpi.in=" + DEFAULT_DPI + "," + UPDATED_DPI);

        // Get all the epaperList where dpi equals to UPDATED_DPI
        defaultEpaperShouldNotBeFound("dpi.in=" + UPDATED_DPI);
    }

    @Test
    @Transactional
    void getAllEpapersByDpiIsNullOrNotNull() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where dpi is not null
        defaultEpaperShouldBeFound("dpi.specified=true");

        // Get all the epaperList where dpi is null
        defaultEpaperShouldNotBeFound("dpi.specified=false");
    }

    @Test
    @Transactional
    void getAllEpapersByDpiIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where dpi is greater than or equal to DEFAULT_DPI
        defaultEpaperShouldBeFound("dpi.greaterThanOrEqual=" + DEFAULT_DPI);

        // Get all the epaperList where dpi is greater than or equal to UPDATED_DPI
        defaultEpaperShouldNotBeFound("dpi.greaterThanOrEqual=" + UPDATED_DPI);
    }

    @Test
    @Transactional
    void getAllEpapersByDpiIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where dpi is less than or equal to DEFAULT_DPI
        defaultEpaperShouldBeFound("dpi.lessThanOrEqual=" + DEFAULT_DPI);

        // Get all the epaperList where dpi is less than or equal to SMALLER_DPI
        defaultEpaperShouldNotBeFound("dpi.lessThanOrEqual=" + SMALLER_DPI);
    }

    @Test
    @Transactional
    void getAllEpapersByDpiIsLessThanSomething() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where dpi is less than DEFAULT_DPI
        defaultEpaperShouldNotBeFound("dpi.lessThan=" + DEFAULT_DPI);

        // Get all the epaperList where dpi is less than UPDATED_DPI
        defaultEpaperShouldBeFound("dpi.lessThan=" + UPDATED_DPI);
    }

    @Test
    @Transactional
    void getAllEpapersByDpiIsGreaterThanSomething() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where dpi is greater than DEFAULT_DPI
        defaultEpaperShouldNotBeFound("dpi.greaterThan=" + DEFAULT_DPI);

        // Get all the epaperList where dpi is greater than SMALLER_DPI
        defaultEpaperShouldBeFound("dpi.greaterThan=" + SMALLER_DPI);
    }

    @Test
    @Transactional
    void getAllEpapersByUploadtimeIsEqualToSomething() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where uploadtime equals to DEFAULT_UPLOADTIME
        defaultEpaperShouldBeFound("uploadtime.equals=" + DEFAULT_UPLOADTIME);

        // Get all the epaperList where uploadtime equals to UPDATED_UPLOADTIME
        defaultEpaperShouldNotBeFound("uploadtime.equals=" + UPDATED_UPLOADTIME);
    }

    @Test
    @Transactional
    void getAllEpapersByUploadtimeIsInShouldWork() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where uploadtime in DEFAULT_UPLOADTIME or UPDATED_UPLOADTIME
        defaultEpaperShouldBeFound("uploadtime.in=" + DEFAULT_UPLOADTIME + "," + UPDATED_UPLOADTIME);

        // Get all the epaperList where uploadtime equals to UPDATED_UPLOADTIME
        defaultEpaperShouldNotBeFound("uploadtime.in=" + UPDATED_UPLOADTIME);
    }

    @Test
    @Transactional
    void getAllEpapersByUploadtimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where uploadtime is not null
        defaultEpaperShouldBeFound("uploadtime.specified=true");

        // Get all the epaperList where uploadtime is null
        defaultEpaperShouldNotBeFound("uploadtime.specified=false");
    }

    @Test
    @Transactional
    void getAllEpapersByUploadtimeContainsSomething() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where uploadtime contains DEFAULT_UPLOADTIME
        defaultEpaperShouldBeFound("uploadtime.contains=" + DEFAULT_UPLOADTIME);

        // Get all the epaperList where uploadtime contains UPDATED_UPLOADTIME
        defaultEpaperShouldNotBeFound("uploadtime.contains=" + UPDATED_UPLOADTIME);
    }

    @Test
    @Transactional
    void getAllEpapersByUploadtimeNotContainsSomething() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where uploadtime does not contain DEFAULT_UPLOADTIME
        defaultEpaperShouldNotBeFound("uploadtime.doesNotContain=" + DEFAULT_UPLOADTIME);

        // Get all the epaperList where uploadtime does not contain UPDATED_UPLOADTIME
        defaultEpaperShouldBeFound("uploadtime.doesNotContain=" + UPDATED_UPLOADTIME);
    }

    @Test
    @Transactional
    void getAllEpapersByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where filename equals to DEFAULT_FILENAME
        defaultEpaperShouldBeFound("filename.equals=" + DEFAULT_FILENAME);

        // Get all the epaperList where filename equals to UPDATED_FILENAME
        defaultEpaperShouldNotBeFound("filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllEpapersByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where filename in DEFAULT_FILENAME or UPDATED_FILENAME
        defaultEpaperShouldBeFound("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME);

        // Get all the epaperList where filename equals to UPDATED_FILENAME
        defaultEpaperShouldNotBeFound("filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllEpapersByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where filename is not null
        defaultEpaperShouldBeFound("filename.specified=true");

        // Get all the epaperList where filename is null
        defaultEpaperShouldNotBeFound("filename.specified=false");
    }

    @Test
    @Transactional
    void getAllEpapersByFilenameContainsSomething() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where filename contains DEFAULT_FILENAME
        defaultEpaperShouldBeFound("filename.contains=" + DEFAULT_FILENAME);

        // Get all the epaperList where filename contains UPDATED_FILENAME
        defaultEpaperShouldNotBeFound("filename.contains=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllEpapersByFilenameNotContainsSomething() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        // Get all the epaperList where filename does not contain DEFAULT_FILENAME
        defaultEpaperShouldNotBeFound("filename.doesNotContain=" + DEFAULT_FILENAME);

        // Get all the epaperList where filename does not contain UPDATED_FILENAME
        defaultEpaperShouldBeFound("filename.doesNotContain=" + UPDATED_FILENAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEpaperShouldBeFound(String filter) throws Exception {
        restEpaperMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(epaper.getId().intValue())))
            .andExpect(jsonPath("$.[*].newspaperName").value(hasItem(DEFAULT_NEWSPAPER_NAME)))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].dpi").value(hasItem(DEFAULT_DPI)))
            .andExpect(jsonPath("$.[*].uploadtime").value(hasItem(DEFAULT_UPLOADTIME)))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME)));

        // Check, that the count call also returns 1
        restEpaperMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEpaperShouldNotBeFound(String filter) throws Exception {
        restEpaperMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEpaperMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEpaper() throws Exception {
        // Get the epaper
        restEpaperMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEpaper() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        int databaseSizeBeforeUpdate = epaperRepository.findAll().size();

        // Update the epaper
        Epaper updatedEpaper = epaperRepository.findById(epaper.getId()).get();
        // Disconnect from session so that the updates on updatedEpaper are not directly saved in db
        em.detach(updatedEpaper);
        updatedEpaper
            .newspaperName(UPDATED_NEWSPAPER_NAME)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .dpi(UPDATED_DPI)
            .uploadtime(UPDATED_UPLOADTIME)
            .filename(UPDATED_FILENAME);

        restEpaperMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEpaper.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEpaper))
            )
            .andExpect(status().isOk());

        // Validate the Epaper in the database
        List<Epaper> epaperList = epaperRepository.findAll();
        assertThat(epaperList).hasSize(databaseSizeBeforeUpdate);
        Epaper testEpaper = epaperList.get(epaperList.size() - 1);
        assertThat(testEpaper.getNewspaperName()).isEqualTo(UPDATED_NEWSPAPER_NAME);
        assertThat(testEpaper.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testEpaper.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testEpaper.getDpi()).isEqualTo(UPDATED_DPI);
        assertThat(testEpaper.getUploadtime()).isEqualTo(UPDATED_UPLOADTIME);
        assertThat(testEpaper.getFilename()).isEqualTo(UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void putNonExistingEpaper() throws Exception {
        int databaseSizeBeforeUpdate = epaperRepository.findAll().size();
        epaper.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEpaperMockMvc
            .perform(
                put(ENTITY_API_URL_ID, epaper.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(epaper))
            )
            .andExpect(status().isBadRequest());

        // Validate the Epaper in the database
        List<Epaper> epaperList = epaperRepository.findAll();
        assertThat(epaperList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEpaper() throws Exception {
        int databaseSizeBeforeUpdate = epaperRepository.findAll().size();
        epaper.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEpaperMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(epaper))
            )
            .andExpect(status().isBadRequest());

        // Validate the Epaper in the database
        List<Epaper> epaperList = epaperRepository.findAll();
        assertThat(epaperList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEpaper() throws Exception {
        int databaseSizeBeforeUpdate = epaperRepository.findAll().size();
        epaper.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEpaperMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(epaper)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Epaper in the database
        List<Epaper> epaperList = epaperRepository.findAll();
        assertThat(epaperList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEpaperWithPatch() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        int databaseSizeBeforeUpdate = epaperRepository.findAll().size();

        // Update the epaper using partial update
        Epaper partialUpdatedEpaper = new Epaper();
        partialUpdatedEpaper.setId(epaper.getId());

        partialUpdatedEpaper.width(UPDATED_WIDTH).height(UPDATED_HEIGHT).uploadtime(UPDATED_UPLOADTIME);

        restEpaperMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEpaper.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEpaper))
            )
            .andExpect(status().isOk());

        // Validate the Epaper in the database
        List<Epaper> epaperList = epaperRepository.findAll();
        assertThat(epaperList).hasSize(databaseSizeBeforeUpdate);
        Epaper testEpaper = epaperList.get(epaperList.size() - 1);
        assertThat(testEpaper.getNewspaperName()).isEqualTo(DEFAULT_NEWSPAPER_NAME);
        assertThat(testEpaper.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testEpaper.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testEpaper.getDpi()).isEqualTo(DEFAULT_DPI);
        assertThat(testEpaper.getUploadtime()).isEqualTo(UPDATED_UPLOADTIME);
        assertThat(testEpaper.getFilename()).isEqualTo(DEFAULT_FILENAME);
    }

    @Test
    @Transactional
    void fullUpdateEpaperWithPatch() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        int databaseSizeBeforeUpdate = epaperRepository.findAll().size();

        // Update the epaper using partial update
        Epaper partialUpdatedEpaper = new Epaper();
        partialUpdatedEpaper.setId(epaper.getId());

        partialUpdatedEpaper
            .newspaperName(UPDATED_NEWSPAPER_NAME)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .dpi(UPDATED_DPI)
            .uploadtime(UPDATED_UPLOADTIME)
            .filename(UPDATED_FILENAME);

        restEpaperMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEpaper.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEpaper))
            )
            .andExpect(status().isOk());

        // Validate the Epaper in the database
        List<Epaper> epaperList = epaperRepository.findAll();
        assertThat(epaperList).hasSize(databaseSizeBeforeUpdate);
        Epaper testEpaper = epaperList.get(epaperList.size() - 1);
        assertThat(testEpaper.getNewspaperName()).isEqualTo(UPDATED_NEWSPAPER_NAME);
        assertThat(testEpaper.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testEpaper.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testEpaper.getDpi()).isEqualTo(UPDATED_DPI);
        assertThat(testEpaper.getUploadtime()).isEqualTo(UPDATED_UPLOADTIME);
        assertThat(testEpaper.getFilename()).isEqualTo(UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void patchNonExistingEpaper() throws Exception {
        int databaseSizeBeforeUpdate = epaperRepository.findAll().size();
        epaper.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEpaperMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, epaper.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(epaper))
            )
            .andExpect(status().isBadRequest());

        // Validate the Epaper in the database
        List<Epaper> epaperList = epaperRepository.findAll();
        assertThat(epaperList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEpaper() throws Exception {
        int databaseSizeBeforeUpdate = epaperRepository.findAll().size();
        epaper.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEpaperMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(epaper))
            )
            .andExpect(status().isBadRequest());

        // Validate the Epaper in the database
        List<Epaper> epaperList = epaperRepository.findAll();
        assertThat(epaperList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEpaper() throws Exception {
        int databaseSizeBeforeUpdate = epaperRepository.findAll().size();
        epaper.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEpaperMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(epaper)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Epaper in the database
        List<Epaper> epaperList = epaperRepository.findAll();
        assertThat(epaperList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEpaper() throws Exception {
        // Initialize the database
        epaperRepository.saveAndFlush(epaper);

        int databaseSizeBeforeDelete = epaperRepository.findAll().size();

        // Delete the epaper
        restEpaperMockMvc
            .perform(delete(ENTITY_API_URL_ID, epaper.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Epaper> epaperList = epaperRepository.findAll();
        assertThat(epaperList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
