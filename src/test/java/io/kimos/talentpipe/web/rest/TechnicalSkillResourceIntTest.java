package io.kimos.talentpipe.web.rest;

import io.kimos.talentpipe.TalentpipeApp;
import io.kimos.talentpipe.domain.TechnicalSkill;
import io.kimos.talentpipe.repository.TechnicalSkillRepository;
import io.kimos.talentpipe.repository.search.TechnicalSkillSearchRepository;
import io.kimos.talentpipe.service.TechnicalSkillService;
import io.kimos.talentpipe.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

import static io.kimos.talentpipe.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TechnicalSkillResource REST controller.
 *
 * @see TechnicalSkillResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TalentpipeApp.class)
public class TechnicalSkillResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NORMALIZED_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NORMALIZED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private TechnicalSkillRepository technicalSkillRepository;

    @Autowired
    private TechnicalSkillService technicalSkillService;

    /**
     * This repository is mocked in the io.kimos.talentpipe.repository.search test package.
     *
     * @see io.kimos.talentpipe.repository.search.TechnicalSkillSearchRepositoryMockConfiguration
     */
    @Autowired
    private TechnicalSkillSearchRepository mockTechnicalSkillSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTechnicalSkillMockMvc;

    private TechnicalSkill technicalSkill;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TechnicalSkill createEntity(EntityManager em) {
        TechnicalSkill technicalSkill = new TechnicalSkill()
            .name(DEFAULT_NAME)
            .normalizedName(DEFAULT_NORMALIZED_NAME)
            .description(DEFAULT_DESCRIPTION);
        return technicalSkill;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TechnicalSkillResource technicalSkillResource = new TechnicalSkillResource(technicalSkillService);
        this.restTechnicalSkillMockMvc = MockMvcBuilders.standaloneSetup(technicalSkillResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        technicalSkill = createEntity(em);
    }

    @Test
    @Transactional
    public void createTechnicalSkill() throws Exception {
        int databaseSizeBeforeCreate = technicalSkillRepository.findAll().size();

        // Create the TechnicalSkill
        restTechnicalSkillMockMvc.perform(post("/api/technical-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(technicalSkill)))
            .andExpect(status().isCreated());

        // Validate the TechnicalSkill in the database
        List<TechnicalSkill> technicalSkillList = technicalSkillRepository.findAll();
        assertThat(technicalSkillList).hasSize(databaseSizeBeforeCreate + 1);
        TechnicalSkill testTechnicalSkill = technicalSkillList.get(technicalSkillList.size() - 1);
        assertThat(testTechnicalSkill.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTechnicalSkill.getNormalizedName()).isEqualTo(DEFAULT_NORMALIZED_NAME);
        assertThat(testTechnicalSkill.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the TechnicalSkill in Elasticsearch
        verify(mockTechnicalSkillSearchRepository, times(1)).save(testTechnicalSkill);
    }

    @Test
    @Transactional
    public void createTechnicalSkillWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = technicalSkillRepository.findAll().size();

        // Create the TechnicalSkill with an existing ID
        technicalSkill.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTechnicalSkillMockMvc.perform(post("/api/technical-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(technicalSkill)))
            .andExpect(status().isBadRequest());

        // Validate the TechnicalSkill in the database
        List<TechnicalSkill> technicalSkillList = technicalSkillRepository.findAll();
        assertThat(technicalSkillList).hasSize(databaseSizeBeforeCreate);

        // Validate the TechnicalSkill in Elasticsearch
        verify(mockTechnicalSkillSearchRepository, times(0)).save(technicalSkill);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = technicalSkillRepository.findAll().size();
        // set the field null
        technicalSkill.setName(null);

        // Create the TechnicalSkill, which fails.

        restTechnicalSkillMockMvc.perform(post("/api/technical-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(technicalSkill)))
            .andExpect(status().isBadRequest());

        List<TechnicalSkill> technicalSkillList = technicalSkillRepository.findAll();
        assertThat(technicalSkillList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNormalizedNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = technicalSkillRepository.findAll().size();
        // set the field null
        technicalSkill.setNormalizedName(null);

        // Create the TechnicalSkill, which fails.

        restTechnicalSkillMockMvc.perform(post("/api/technical-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(technicalSkill)))
            .andExpect(status().isBadRequest());

        List<TechnicalSkill> technicalSkillList = technicalSkillRepository.findAll();
        assertThat(technicalSkillList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTechnicalSkills() throws Exception {
        // Initialize the database
        technicalSkillRepository.saveAndFlush(technicalSkill);

        // Get all the technicalSkillList
        restTechnicalSkillMockMvc.perform(get("/api/technical-skills?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(technicalSkill.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].normalizedName").value(hasItem(DEFAULT_NORMALIZED_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getTechnicalSkill() throws Exception {
        // Initialize the database
        technicalSkillRepository.saveAndFlush(technicalSkill);

        // Get the technicalSkill
        restTechnicalSkillMockMvc.perform(get("/api/technical-skills/{id}", technicalSkill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(technicalSkill.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.normalizedName").value(DEFAULT_NORMALIZED_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTechnicalSkill() throws Exception {
        // Get the technicalSkill
        restTechnicalSkillMockMvc.perform(get("/api/technical-skills/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTechnicalSkill() throws Exception {
        // Initialize the database
        technicalSkillService.save(technicalSkill);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockTechnicalSkillSearchRepository);

        int databaseSizeBeforeUpdate = technicalSkillRepository.findAll().size();

        // Update the technicalSkill
        TechnicalSkill updatedTechnicalSkill = technicalSkillRepository.findById(technicalSkill.getId()).get();
        // Disconnect from session so that the updates on updatedTechnicalSkill are not directly saved in db
        em.detach(updatedTechnicalSkill);
        updatedTechnicalSkill
            .name(UPDATED_NAME)
            .normalizedName(UPDATED_NORMALIZED_NAME)
            .description(UPDATED_DESCRIPTION);

        restTechnicalSkillMockMvc.perform(put("/api/technical-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTechnicalSkill)))
            .andExpect(status().isOk());

        // Validate the TechnicalSkill in the database
        List<TechnicalSkill> technicalSkillList = technicalSkillRepository.findAll();
        assertThat(technicalSkillList).hasSize(databaseSizeBeforeUpdate);
        TechnicalSkill testTechnicalSkill = technicalSkillList.get(technicalSkillList.size() - 1);
        assertThat(testTechnicalSkill.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTechnicalSkill.getNormalizedName()).isEqualTo(UPDATED_NORMALIZED_NAME);
        assertThat(testTechnicalSkill.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the TechnicalSkill in Elasticsearch
        verify(mockTechnicalSkillSearchRepository, times(1)).save(testTechnicalSkill);
    }

    @Test
    @Transactional
    public void updateNonExistingTechnicalSkill() throws Exception {
        int databaseSizeBeforeUpdate = technicalSkillRepository.findAll().size();

        // Create the TechnicalSkill

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechnicalSkillMockMvc.perform(put("/api/technical-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(technicalSkill)))
            .andExpect(status().isBadRequest());

        // Validate the TechnicalSkill in the database
        List<TechnicalSkill> technicalSkillList = technicalSkillRepository.findAll();
        assertThat(technicalSkillList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TechnicalSkill in Elasticsearch
        verify(mockTechnicalSkillSearchRepository, times(0)).save(technicalSkill);
    }

    @Test
    @Transactional
    public void deleteTechnicalSkill() throws Exception {
        // Initialize the database
        technicalSkillService.save(technicalSkill);

        int databaseSizeBeforeDelete = technicalSkillRepository.findAll().size();

        // Get the technicalSkill
        restTechnicalSkillMockMvc.perform(delete("/api/technical-skills/{id}", technicalSkill.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TechnicalSkill> technicalSkillList = technicalSkillRepository.findAll();
        assertThat(technicalSkillList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TechnicalSkill in Elasticsearch
        verify(mockTechnicalSkillSearchRepository, times(1)).deleteById(technicalSkill.getId());
    }

    @Test
    @Transactional
    public void searchTechnicalSkill() throws Exception {
        // Initialize the database
        technicalSkillService.save(technicalSkill);
        when(mockTechnicalSkillSearchRepository.search(queryStringQuery("id:" + technicalSkill.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(technicalSkill), PageRequest.of(0, 1), 1));
        // Search the technicalSkill
        restTechnicalSkillMockMvc.perform(get("/api/_search/technical-skills?query=id:" + technicalSkill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(technicalSkill.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].normalizedName").value(hasItem(DEFAULT_NORMALIZED_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TechnicalSkill.class);
        TechnicalSkill technicalSkill1 = new TechnicalSkill();
        technicalSkill1.setId(1L);
        TechnicalSkill technicalSkill2 = new TechnicalSkill();
        technicalSkill2.setId(technicalSkill1.getId());
        assertThat(technicalSkill1).isEqualTo(technicalSkill2);
        technicalSkill2.setId(2L);
        assertThat(technicalSkill1).isNotEqualTo(technicalSkill2);
        technicalSkill1.setId(null);
        assertThat(technicalSkill1).isNotEqualTo(technicalSkill2);
    }
}
