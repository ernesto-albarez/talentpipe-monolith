package io.kimos.talentppe.web.rest;

import io.kimos.talentppe.MonolithApp;
import io.kimos.talentppe.domain.SoftSkill;
import io.kimos.talentppe.repository.SoftSkillRepository;
import io.kimos.talentppe.repository.search.SoftSkillSearchRepository;
import io.kimos.talentppe.service.SoftSkillService;
import io.kimos.talentppe.web.rest.errors.ExceptionTranslator;
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

import static io.kimos.talentppe.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SoftSkillResource REST controller.
 *
 * @see SoftSkillResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MonolithApp.class)
public class SoftSkillResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NORMALIZED_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NORMALIZED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private SoftSkillRepository softSkillRepository;

    @Autowired
    private SoftSkillService softSkillService;

    /**
     * This repository is mocked in the io.kimos.talentppe.repository.search test package.
     *
     * @see io.kimos.talentppe.repository.search.SoftSkillSearchRepositoryMockConfiguration
     */
    @Autowired
    private SoftSkillSearchRepository mockSoftSkillSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSoftSkillMockMvc;

    private SoftSkill softSkill;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SoftSkill createEntity(EntityManager em) {
        SoftSkill softSkill = new SoftSkill()
            .name(DEFAULT_NAME)
            .normalizedName(DEFAULT_NORMALIZED_NAME)
            .description(DEFAULT_DESCRIPTION);
        return softSkill;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SoftSkillResource softSkillResource = new SoftSkillResource(softSkillService);
        this.restSoftSkillMockMvc = MockMvcBuilders.standaloneSetup(softSkillResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        softSkill = createEntity(em);
    }

    @Test
    @Transactional
    public void createSoftSkill() throws Exception {
        int databaseSizeBeforeCreate = softSkillRepository.findAll().size();

        // Create the SoftSkill
        restSoftSkillMockMvc.perform(post("/api/soft-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(softSkill)))
            .andExpect(status().isCreated());

        // Validate the SoftSkill in the database
        List<SoftSkill> softSkillList = softSkillRepository.findAll();
        assertThat(softSkillList).hasSize(databaseSizeBeforeCreate + 1);
        SoftSkill testSoftSkill = softSkillList.get(softSkillList.size() - 1);
        assertThat(testSoftSkill.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSoftSkill.getNormalizedName()).isEqualTo(DEFAULT_NORMALIZED_NAME);
        assertThat(testSoftSkill.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the SoftSkill in Elasticsearch
        verify(mockSoftSkillSearchRepository, times(1)).save(testSoftSkill);
    }

    @Test
    @Transactional
    public void createSoftSkillWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = softSkillRepository.findAll().size();

        // Create the SoftSkill with an existing ID
        softSkill.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSoftSkillMockMvc.perform(post("/api/soft-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(softSkill)))
            .andExpect(status().isBadRequest());

        // Validate the SoftSkill in the database
        List<SoftSkill> softSkillList = softSkillRepository.findAll();
        assertThat(softSkillList).hasSize(databaseSizeBeforeCreate);

        // Validate the SoftSkill in Elasticsearch
        verify(mockSoftSkillSearchRepository, times(0)).save(softSkill);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = softSkillRepository.findAll().size();
        // set the field null
        softSkill.setName(null);

        // Create the SoftSkill, which fails.

        restSoftSkillMockMvc.perform(post("/api/soft-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(softSkill)))
            .andExpect(status().isBadRequest());

        List<SoftSkill> softSkillList = softSkillRepository.findAll();
        assertThat(softSkillList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNormalizedNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = softSkillRepository.findAll().size();
        // set the field null
        softSkill.setNormalizedName(null);

        // Create the SoftSkill, which fails.

        restSoftSkillMockMvc.perform(post("/api/soft-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(softSkill)))
            .andExpect(status().isBadRequest());

        List<SoftSkill> softSkillList = softSkillRepository.findAll();
        assertThat(softSkillList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSoftSkills() throws Exception {
        // Initialize the database
        softSkillRepository.saveAndFlush(softSkill);

        // Get all the softSkillList
        restSoftSkillMockMvc.perform(get("/api/soft-skills?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(softSkill.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].normalizedName").value(hasItem(DEFAULT_NORMALIZED_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getSoftSkill() throws Exception {
        // Initialize the database
        softSkillRepository.saveAndFlush(softSkill);

        // Get the softSkill
        restSoftSkillMockMvc.perform(get("/api/soft-skills/{id}", softSkill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(softSkill.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.normalizedName").value(DEFAULT_NORMALIZED_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSoftSkill() throws Exception {
        // Get the softSkill
        restSoftSkillMockMvc.perform(get("/api/soft-skills/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSoftSkill() throws Exception {
        // Initialize the database
        softSkillService.save(softSkill);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockSoftSkillSearchRepository);

        int databaseSizeBeforeUpdate = softSkillRepository.findAll().size();

        // Update the softSkill
        SoftSkill updatedSoftSkill = softSkillRepository.findById(softSkill.getId()).get();
        // Disconnect from session so that the updates on updatedSoftSkill are not directly saved in db
        em.detach(updatedSoftSkill);
        updatedSoftSkill
            .name(UPDATED_NAME)
            .normalizedName(UPDATED_NORMALIZED_NAME)
            .description(UPDATED_DESCRIPTION);

        restSoftSkillMockMvc.perform(put("/api/soft-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSoftSkill)))
            .andExpect(status().isOk());

        // Validate the SoftSkill in the database
        List<SoftSkill> softSkillList = softSkillRepository.findAll();
        assertThat(softSkillList).hasSize(databaseSizeBeforeUpdate);
        SoftSkill testSoftSkill = softSkillList.get(softSkillList.size() - 1);
        assertThat(testSoftSkill.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSoftSkill.getNormalizedName()).isEqualTo(UPDATED_NORMALIZED_NAME);
        assertThat(testSoftSkill.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the SoftSkill in Elasticsearch
        verify(mockSoftSkillSearchRepository, times(1)).save(testSoftSkill);
    }

    @Test
    @Transactional
    public void updateNonExistingSoftSkill() throws Exception {
        int databaseSizeBeforeUpdate = softSkillRepository.findAll().size();

        // Create the SoftSkill

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSoftSkillMockMvc.perform(put("/api/soft-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(softSkill)))
            .andExpect(status().isBadRequest());

        // Validate the SoftSkill in the database
        List<SoftSkill> softSkillList = softSkillRepository.findAll();
        assertThat(softSkillList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SoftSkill in Elasticsearch
        verify(mockSoftSkillSearchRepository, times(0)).save(softSkill);
    }

    @Test
    @Transactional
    public void deleteSoftSkill() throws Exception {
        // Initialize the database
        softSkillService.save(softSkill);

        int databaseSizeBeforeDelete = softSkillRepository.findAll().size();

        // Get the softSkill
        restSoftSkillMockMvc.perform(delete("/api/soft-skills/{id}", softSkill.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SoftSkill> softSkillList = softSkillRepository.findAll();
        assertThat(softSkillList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SoftSkill in Elasticsearch
        verify(mockSoftSkillSearchRepository, times(1)).deleteById(softSkill.getId());
    }

    @Test
    @Transactional
    public void searchSoftSkill() throws Exception {
        // Initialize the database
        softSkillService.save(softSkill);
        when(mockSoftSkillSearchRepository.search(queryStringQuery("id:" + softSkill.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(softSkill), PageRequest.of(0, 1), 1));
        // Search the softSkill
        restSoftSkillMockMvc.perform(get("/api/_search/soft-skills?query=id:" + softSkill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(softSkill.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].normalizedName").value(hasItem(DEFAULT_NORMALIZED_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SoftSkill.class);
        SoftSkill softSkill1 = new SoftSkill();
        softSkill1.setId(1L);
        SoftSkill softSkill2 = new SoftSkill();
        softSkill2.setId(softSkill1.getId());
        assertThat(softSkill1).isEqualTo(softSkill2);
        softSkill2.setId(2L);
        assertThat(softSkill1).isNotEqualTo(softSkill2);
        softSkill1.setId(null);
        assertThat(softSkill1).isNotEqualTo(softSkill2);
    }
}
