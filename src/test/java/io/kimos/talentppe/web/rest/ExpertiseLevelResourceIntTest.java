package io.kimos.talentppe.web.rest;

import io.kimos.talentppe.MonolithApp;

import io.kimos.talentppe.domain.ExpertiseLevel;
import io.kimos.talentppe.repository.ExpertiseLevelRepository;
import io.kimos.talentppe.repository.search.ExpertiseLevelSearchRepository;
import io.kimos.talentppe.service.ExpertiseLevelService;
import io.kimos.talentppe.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static io.kimos.talentppe.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ExpertiseLevelResource REST controller.
 *
 * @see ExpertiseLevelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MonolithApp.class)
public class ExpertiseLevelResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NORMALIZED_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NORMALIZED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ExpertiseLevelRepository expertiseLevelRepository;

    @Autowired
    private ExpertiseLevelService expertiseLevelService;

    /**
     * This repository is mocked in the io.kimos.talentppe.repository.search test package.
     *
     * @see io.kimos.talentppe.repository.search.ExpertiseLevelSearchRepositoryMockConfiguration
     */
    @Autowired
    private ExpertiseLevelSearchRepository mockExpertiseLevelSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExpertiseLevelMockMvc;

    private ExpertiseLevel expertiseLevel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExpertiseLevelResource expertiseLevelResource = new ExpertiseLevelResource(expertiseLevelService);
        this.restExpertiseLevelMockMvc = MockMvcBuilders.standaloneSetup(expertiseLevelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpertiseLevel createEntity(EntityManager em) {
        ExpertiseLevel expertiseLevel = new ExpertiseLevel()
            .name(DEFAULT_NAME)
            .normalizedName(DEFAULT_NORMALIZED_NAME)
            .description(DEFAULT_DESCRIPTION);
        return expertiseLevel;
    }

    @Before
    public void initTest() {
        expertiseLevel = createEntity(em);
    }

    @Test
    @Transactional
    public void createExpertiseLevel() throws Exception {
        int databaseSizeBeforeCreate = expertiseLevelRepository.findAll().size();

        // Create the ExpertiseLevel
        restExpertiseLevelMockMvc.perform(post("/api/expertise-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expertiseLevel)))
            .andExpect(status().isCreated());

        // Validate the ExpertiseLevel in the database
        List<ExpertiseLevel> expertiseLevelList = expertiseLevelRepository.findAll();
        assertThat(expertiseLevelList).hasSize(databaseSizeBeforeCreate + 1);
        ExpertiseLevel testExpertiseLevel = expertiseLevelList.get(expertiseLevelList.size() - 1);
        assertThat(testExpertiseLevel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExpertiseLevel.getNormalizedName()).isEqualTo(DEFAULT_NORMALIZED_NAME);
        assertThat(testExpertiseLevel.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the ExpertiseLevel in Elasticsearch
        verify(mockExpertiseLevelSearchRepository, times(1)).save(testExpertiseLevel);
    }

    @Test
    @Transactional
    public void createExpertiseLevelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = expertiseLevelRepository.findAll().size();

        // Create the ExpertiseLevel with an existing ID
        expertiseLevel.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpertiseLevelMockMvc.perform(post("/api/expertise-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expertiseLevel)))
            .andExpect(status().isBadRequest());

        // Validate the ExpertiseLevel in the database
        List<ExpertiseLevel> expertiseLevelList = expertiseLevelRepository.findAll();
        assertThat(expertiseLevelList).hasSize(databaseSizeBeforeCreate);

        // Validate the ExpertiseLevel in Elasticsearch
        verify(mockExpertiseLevelSearchRepository, times(0)).save(expertiseLevel);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = expertiseLevelRepository.findAll().size();
        // set the field null
        expertiseLevel.setName(null);

        // Create the ExpertiseLevel, which fails.

        restExpertiseLevelMockMvc.perform(post("/api/expertise-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expertiseLevel)))
            .andExpect(status().isBadRequest());

        List<ExpertiseLevel> expertiseLevelList = expertiseLevelRepository.findAll();
        assertThat(expertiseLevelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNormalizedNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = expertiseLevelRepository.findAll().size();
        // set the field null
        expertiseLevel.setNormalizedName(null);

        // Create the ExpertiseLevel, which fails.

        restExpertiseLevelMockMvc.perform(post("/api/expertise-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expertiseLevel)))
            .andExpect(status().isBadRequest());

        List<ExpertiseLevel> expertiseLevelList = expertiseLevelRepository.findAll();
        assertThat(expertiseLevelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExpertiseLevels() throws Exception {
        // Initialize the database
        expertiseLevelRepository.saveAndFlush(expertiseLevel);

        // Get all the expertiseLevelList
        restExpertiseLevelMockMvc.perform(get("/api/expertise-levels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expertiseLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].normalizedName").value(hasItem(DEFAULT_NORMALIZED_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getExpertiseLevel() throws Exception {
        // Initialize the database
        expertiseLevelRepository.saveAndFlush(expertiseLevel);

        // Get the expertiseLevel
        restExpertiseLevelMockMvc.perform(get("/api/expertise-levels/{id}", expertiseLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(expertiseLevel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.normalizedName").value(DEFAULT_NORMALIZED_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExpertiseLevel() throws Exception {
        // Get the expertiseLevel
        restExpertiseLevelMockMvc.perform(get("/api/expertise-levels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExpertiseLevel() throws Exception {
        // Initialize the database
        expertiseLevelService.save(expertiseLevel);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockExpertiseLevelSearchRepository);

        int databaseSizeBeforeUpdate = expertiseLevelRepository.findAll().size();

        // Update the expertiseLevel
        ExpertiseLevel updatedExpertiseLevel = expertiseLevelRepository.findById(expertiseLevel.getId()).get();
        // Disconnect from session so that the updates on updatedExpertiseLevel are not directly saved in db
        em.detach(updatedExpertiseLevel);
        updatedExpertiseLevel
            .name(UPDATED_NAME)
            .normalizedName(UPDATED_NORMALIZED_NAME)
            .description(UPDATED_DESCRIPTION);

        restExpertiseLevelMockMvc.perform(put("/api/expertise-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExpertiseLevel)))
            .andExpect(status().isOk());

        // Validate the ExpertiseLevel in the database
        List<ExpertiseLevel> expertiseLevelList = expertiseLevelRepository.findAll();
        assertThat(expertiseLevelList).hasSize(databaseSizeBeforeUpdate);
        ExpertiseLevel testExpertiseLevel = expertiseLevelList.get(expertiseLevelList.size() - 1);
        assertThat(testExpertiseLevel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExpertiseLevel.getNormalizedName()).isEqualTo(UPDATED_NORMALIZED_NAME);
        assertThat(testExpertiseLevel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the ExpertiseLevel in Elasticsearch
        verify(mockExpertiseLevelSearchRepository, times(1)).save(testExpertiseLevel);
    }

    @Test
    @Transactional
    public void updateNonExistingExpertiseLevel() throws Exception {
        int databaseSizeBeforeUpdate = expertiseLevelRepository.findAll().size();

        // Create the ExpertiseLevel

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpertiseLevelMockMvc.perform(put("/api/expertise-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expertiseLevel)))
            .andExpect(status().isBadRequest());

        // Validate the ExpertiseLevel in the database
        List<ExpertiseLevel> expertiseLevelList = expertiseLevelRepository.findAll();
        assertThat(expertiseLevelList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExpertiseLevel in Elasticsearch
        verify(mockExpertiseLevelSearchRepository, times(0)).save(expertiseLevel);
    }

    @Test
    @Transactional
    public void deleteExpertiseLevel() throws Exception {
        // Initialize the database
        expertiseLevelService.save(expertiseLevel);

        int databaseSizeBeforeDelete = expertiseLevelRepository.findAll().size();

        // Get the expertiseLevel
        restExpertiseLevelMockMvc.perform(delete("/api/expertise-levels/{id}", expertiseLevel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ExpertiseLevel> expertiseLevelList = expertiseLevelRepository.findAll();
        assertThat(expertiseLevelList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ExpertiseLevel in Elasticsearch
        verify(mockExpertiseLevelSearchRepository, times(1)).deleteById(expertiseLevel.getId());
    }

    @Test
    @Transactional
    public void searchExpertiseLevel() throws Exception {
        // Initialize the database
        expertiseLevelService.save(expertiseLevel);
        when(mockExpertiseLevelSearchRepository.search(queryStringQuery("id:" + expertiseLevel.getId())))
            .thenReturn(Collections.singletonList(expertiseLevel));
        // Search the expertiseLevel
        restExpertiseLevelMockMvc.perform(get("/api/_search/expertise-levels?query=id:" + expertiseLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expertiseLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].normalizedName").value(hasItem(DEFAULT_NORMALIZED_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpertiseLevel.class);
        ExpertiseLevel expertiseLevel1 = new ExpertiseLevel();
        expertiseLevel1.setId(1L);
        ExpertiseLevel expertiseLevel2 = new ExpertiseLevel();
        expertiseLevel2.setId(expertiseLevel1.getId());
        assertThat(expertiseLevel1).isEqualTo(expertiseLevel2);
        expertiseLevel2.setId(2L);
        assertThat(expertiseLevel1).isNotEqualTo(expertiseLevel2);
        expertiseLevel1.setId(null);
        assertThat(expertiseLevel1).isNotEqualTo(expertiseLevel2);
    }
}
