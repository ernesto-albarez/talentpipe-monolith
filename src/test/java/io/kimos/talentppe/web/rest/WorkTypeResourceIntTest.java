package io.kimos.talentppe.web.rest;

import io.kimos.talentppe.MonolithApp;
import io.kimos.talentppe.domain.WorkType;
import io.kimos.talentppe.repository.WorkTypeRepository;
import io.kimos.talentppe.repository.search.WorkTypeSearchRepository;
import io.kimos.talentppe.service.WorkTypeService;
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
 * Test class for the WorkTypeResource REST controller.
 *
 * @see WorkTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MonolithApp.class)
public class WorkTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NORMALIZED_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NORMALIZED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_MIN_QUANTITY_HOURS = 0;
    private static final Integer UPDATED_MIN_QUANTITY_HOURS = 1;

    private static final Integer DEFAULT_MAX_QUANTITY_HOURS = 24;
    private static final Integer UPDATED_MAX_QUANTITY_HOURS = 23;

    @Autowired
    private WorkTypeRepository workTypeRepository;

    @Autowired
    private WorkTypeService workTypeService;

    /**
     * This repository is mocked in the io.kimos.talentppe.repository.search test package.
     *
     * @see io.kimos.talentppe.repository.search.WorkTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private WorkTypeSearchRepository mockWorkTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWorkTypeMockMvc;

    private WorkType workType;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkType createEntity(EntityManager em) {
        WorkType workType = new WorkType()
            .name(DEFAULT_NAME)
            .normalizedName(DEFAULT_NORMALIZED_NAME)
            .description(DEFAULT_DESCRIPTION)
            .minQuantityHours(DEFAULT_MIN_QUANTITY_HOURS)
            .maxQuantityHours(DEFAULT_MAX_QUANTITY_HOURS);
        return workType;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WorkTypeResource workTypeResource = new WorkTypeResource(workTypeService);
        this.restWorkTypeMockMvc = MockMvcBuilders.standaloneSetup(workTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        workType = createEntity(em);
    }

    @Test
    @Transactional
    public void createWorkType() throws Exception {
        int databaseSizeBeforeCreate = workTypeRepository.findAll().size();

        // Create the WorkType
        restWorkTypeMockMvc.perform(post("/api/work-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workType)))
            .andExpect(status().isCreated());

        // Validate the WorkType in the database
        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeCreate + 1);
        WorkType testWorkType = workTypeList.get(workTypeList.size() - 1);
        assertThat(testWorkType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWorkType.getNormalizedName()).isEqualTo(DEFAULT_NORMALIZED_NAME);
        assertThat(testWorkType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWorkType.getMinQuantityHours()).isEqualTo(DEFAULT_MIN_QUANTITY_HOURS);
        assertThat(testWorkType.getMaxQuantityHours()).isEqualTo(DEFAULT_MAX_QUANTITY_HOURS);

        // Validate the WorkType in Elasticsearch
        verify(mockWorkTypeSearchRepository, times(1)).save(testWorkType);
    }

    @Test
    @Transactional
    public void createWorkTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = workTypeRepository.findAll().size();

        // Create the WorkType with an existing ID
        workType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkTypeMockMvc.perform(post("/api/work-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workType)))
            .andExpect(status().isBadRequest());

        // Validate the WorkType in the database
        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the WorkType in Elasticsearch
        verify(mockWorkTypeSearchRepository, times(0)).save(workType);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = workTypeRepository.findAll().size();
        // set the field null
        workType.setName(null);

        // Create the WorkType, which fails.

        restWorkTypeMockMvc.perform(post("/api/work-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workType)))
            .andExpect(status().isBadRequest());

        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNormalizedNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = workTypeRepository.findAll().size();
        // set the field null
        workType.setNormalizedName(null);

        // Create the WorkType, which fails.

        restWorkTypeMockMvc.perform(post("/api/work-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workType)))
            .andExpect(status().isBadRequest());

        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMinQuantityHoursIsRequired() throws Exception {
        int databaseSizeBeforeTest = workTypeRepository.findAll().size();
        // set the field null
        workType.setMinQuantityHours(null);

        // Create the WorkType, which fails.

        restWorkTypeMockMvc.perform(post("/api/work-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workType)))
            .andExpect(status().isBadRequest());

        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMaxQuantityHoursIsRequired() throws Exception {
        int databaseSizeBeforeTest = workTypeRepository.findAll().size();
        // set the field null
        workType.setMaxQuantityHours(null);

        // Create the WorkType, which fails.

        restWorkTypeMockMvc.perform(post("/api/work-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workType)))
            .andExpect(status().isBadRequest());

        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWorkTypes() throws Exception {
        // Initialize the database
        workTypeRepository.saveAndFlush(workType);

        // Get all the workTypeList
        restWorkTypeMockMvc.perform(get("/api/work-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].normalizedName").value(hasItem(DEFAULT_NORMALIZED_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].minQuantityHours").value(hasItem(DEFAULT_MIN_QUANTITY_HOURS)))
            .andExpect(jsonPath("$.[*].maxQuantityHours").value(hasItem(DEFAULT_MAX_QUANTITY_HOURS)));
    }

    @Test
    @Transactional
    public void getWorkType() throws Exception {
        // Initialize the database
        workTypeRepository.saveAndFlush(workType);

        // Get the workType
        restWorkTypeMockMvc.perform(get("/api/work-types/{id}", workType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(workType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.normalizedName").value(DEFAULT_NORMALIZED_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.minQuantityHours").value(DEFAULT_MIN_QUANTITY_HOURS))
            .andExpect(jsonPath("$.maxQuantityHours").value(DEFAULT_MAX_QUANTITY_HOURS));
    }

    @Test
    @Transactional
    public void getNonExistingWorkType() throws Exception {
        // Get the workType
        restWorkTypeMockMvc.perform(get("/api/work-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkType() throws Exception {
        // Initialize the database
        workTypeService.save(workType);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockWorkTypeSearchRepository);

        int databaseSizeBeforeUpdate = workTypeRepository.findAll().size();

        // Update the workType
        WorkType updatedWorkType = workTypeRepository.findById(workType.getId()).get();
        // Disconnect from session so that the updates on updatedWorkType are not directly saved in db
        em.detach(updatedWorkType);
        updatedWorkType
            .name(UPDATED_NAME)
            .normalizedName(UPDATED_NORMALIZED_NAME)
            .description(UPDATED_DESCRIPTION)
            .minQuantityHours(UPDATED_MIN_QUANTITY_HOURS)
            .maxQuantityHours(UPDATED_MAX_QUANTITY_HOURS);

        restWorkTypeMockMvc.perform(put("/api/work-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWorkType)))
            .andExpect(status().isOk());

        // Validate the WorkType in the database
        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeUpdate);
        WorkType testWorkType = workTypeList.get(workTypeList.size() - 1);
        assertThat(testWorkType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorkType.getNormalizedName()).isEqualTo(UPDATED_NORMALIZED_NAME);
        assertThat(testWorkType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkType.getMinQuantityHours()).isEqualTo(UPDATED_MIN_QUANTITY_HOURS);
        assertThat(testWorkType.getMaxQuantityHours()).isEqualTo(UPDATED_MAX_QUANTITY_HOURS);

        // Validate the WorkType in Elasticsearch
        verify(mockWorkTypeSearchRepository, times(1)).save(testWorkType);
    }

    @Test
    @Transactional
    public void updateNonExistingWorkType() throws Exception {
        int databaseSizeBeforeUpdate = workTypeRepository.findAll().size();

        // Create the WorkType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkTypeMockMvc.perform(put("/api/work-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workType)))
            .andExpect(status().isBadRequest());

        // Validate the WorkType in the database
        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkType in Elasticsearch
        verify(mockWorkTypeSearchRepository, times(0)).save(workType);
    }

    @Test
    @Transactional
    public void deleteWorkType() throws Exception {
        // Initialize the database
        workTypeService.save(workType);

        int databaseSizeBeforeDelete = workTypeRepository.findAll().size();

        // Get the workType
        restWorkTypeMockMvc.perform(delete("/api/work-types/{id}", workType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the WorkType in Elasticsearch
        verify(mockWorkTypeSearchRepository, times(1)).deleteById(workType.getId());
    }

    @Test
    @Transactional
    public void searchWorkType() throws Exception {
        // Initialize the database
        workTypeService.save(workType);
        when(mockWorkTypeSearchRepository.search(queryStringQuery("id:" + workType.getId())))
            .thenReturn(Collections.singletonList(workType));
        // Search the workType
        restWorkTypeMockMvc.perform(get("/api/_search/work-types?query=id:" + workType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].normalizedName").value(hasItem(DEFAULT_NORMALIZED_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].minQuantityHours").value(hasItem(DEFAULT_MIN_QUANTITY_HOURS)))
            .andExpect(jsonPath("$.[*].maxQuantityHours").value(hasItem(DEFAULT_MAX_QUANTITY_HOURS)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkType.class);
        WorkType workType1 = new WorkType();
        workType1.setId(1L);
        WorkType workType2 = new WorkType();
        workType2.setId(workType1.getId());
        assertThat(workType1).isEqualTo(workType2);
        workType2.setId(2L);
        assertThat(workType1).isNotEqualTo(workType2);
        workType1.setId(null);
        assertThat(workType1).isNotEqualTo(workType2);
    }
}
