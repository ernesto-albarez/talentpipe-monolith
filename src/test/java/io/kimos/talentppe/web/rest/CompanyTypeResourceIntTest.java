package io.kimos.talentppe.web.rest;

import io.kimos.talentppe.MonolithApp;
import io.kimos.talentppe.domain.CompanyType;
import io.kimos.talentppe.repository.CompanyTypeRepository;
import io.kimos.talentppe.repository.search.CompanyTypeSearchRepository;
import io.kimos.talentppe.service.CompanyTypeService;
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
 * Test class for the CompanyTypeResource REST controller.
 *
 * @see CompanyTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MonolithApp.class)
public class CompanyTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NORMALIZED_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NORMALIZED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_MIN_EMPLOYEES_QUANTITY = 0;
    private static final Integer UPDATED_MIN_EMPLOYEES_QUANTITY = 1;

    private static final Integer DEFAULT_MAX_EMPLOYEES_QUANTITY = 1;
    private static final Integer UPDATED_MAX_EMPLOYEES_QUANTITY = 2;

    @Autowired
    private CompanyTypeRepository companyTypeRepository;

    @Autowired
    private CompanyTypeService companyTypeService;

    /**
     * This repository is mocked in the io.kimos.talentppe.repository.search test package.
     *
     * @see io.kimos.talentppe.repository.search.CompanyTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CompanyTypeSearchRepository mockCompanyTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCompanyTypeMockMvc;

    private CompanyType companyType;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyType createEntity(EntityManager em) {
        CompanyType companyType = new CompanyType()
            .name(DEFAULT_NAME)
            .normalizedName(DEFAULT_NORMALIZED_NAME)
            .description(DEFAULT_DESCRIPTION)
            .minEmployeesQuantity(DEFAULT_MIN_EMPLOYEES_QUANTITY)
            .maxEmployeesQuantity(DEFAULT_MAX_EMPLOYEES_QUANTITY);
        return companyType;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CompanyTypeResource companyTypeResource = new CompanyTypeResource(companyTypeService);
        this.restCompanyTypeMockMvc = MockMvcBuilders.standaloneSetup(companyTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        companyType = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompanyType() throws Exception {
        int databaseSizeBeforeCreate = companyTypeRepository.findAll().size();

        // Create the CompanyType
        restCompanyTypeMockMvc.perform(post("/api/company-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyType)))
            .andExpect(status().isCreated());

        // Validate the CompanyType in the database
        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CompanyType testCompanyType = companyTypeList.get(companyTypeList.size() - 1);
        assertThat(testCompanyType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCompanyType.getNormalizedName()).isEqualTo(DEFAULT_NORMALIZED_NAME);
        assertThat(testCompanyType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCompanyType.getMinEmployeesQuantity()).isEqualTo(DEFAULT_MIN_EMPLOYEES_QUANTITY);
        assertThat(testCompanyType.getMaxEmployeesQuantity()).isEqualTo(DEFAULT_MAX_EMPLOYEES_QUANTITY);

        // Validate the CompanyType in Elasticsearch
        verify(mockCompanyTypeSearchRepository, times(1)).save(testCompanyType);
    }

    @Test
    @Transactional
    public void createCompanyTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = companyTypeRepository.findAll().size();

        // Create the CompanyType with an existing ID
        companyType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyTypeMockMvc.perform(post("/api/company-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyType)))
            .andExpect(status().isBadRequest());

        // Validate the CompanyType in the database
        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CompanyType in Elasticsearch
        verify(mockCompanyTypeSearchRepository, times(0)).save(companyType);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyTypeRepository.findAll().size();
        // set the field null
        companyType.setName(null);

        // Create the CompanyType, which fails.

        restCompanyTypeMockMvc.perform(post("/api/company-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyType)))
            .andExpect(status().isBadRequest());

        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNormalizedNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyTypeRepository.findAll().size();
        // set the field null
        companyType.setNormalizedName(null);

        // Create the CompanyType, which fails.

        restCompanyTypeMockMvc.perform(post("/api/company-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyType)))
            .andExpect(status().isBadRequest());

        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMinEmployeesQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyTypeRepository.findAll().size();
        // set the field null
        companyType.setMinEmployeesQuantity(null);

        // Create the CompanyType, which fails.

        restCompanyTypeMockMvc.perform(post("/api/company-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyType)))
            .andExpect(status().isBadRequest());

        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompanyTypes() throws Exception {
        // Initialize the database
        companyTypeRepository.saveAndFlush(companyType);

        // Get all the companyTypeList
        restCompanyTypeMockMvc.perform(get("/api/company-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].normalizedName").value(hasItem(DEFAULT_NORMALIZED_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].minEmployeesQuantity").value(hasItem(DEFAULT_MIN_EMPLOYEES_QUANTITY)))
            .andExpect(jsonPath("$.[*].maxEmployeesQuantity").value(hasItem(DEFAULT_MAX_EMPLOYEES_QUANTITY)));
    }

    @Test
    @Transactional
    public void getCompanyType() throws Exception {
        // Initialize the database
        companyTypeRepository.saveAndFlush(companyType);

        // Get the companyType
        restCompanyTypeMockMvc.perform(get("/api/company-types/{id}", companyType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(companyType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.normalizedName").value(DEFAULT_NORMALIZED_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.minEmployeesQuantity").value(DEFAULT_MIN_EMPLOYEES_QUANTITY))
            .andExpect(jsonPath("$.maxEmployeesQuantity").value(DEFAULT_MAX_EMPLOYEES_QUANTITY));
    }

    @Test
    @Transactional
    public void getNonExistingCompanyType() throws Exception {
        // Get the companyType
        restCompanyTypeMockMvc.perform(get("/api/company-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompanyType() throws Exception {
        // Initialize the database
        companyTypeService.save(companyType);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCompanyTypeSearchRepository);

        int databaseSizeBeforeUpdate = companyTypeRepository.findAll().size();

        // Update the companyType
        CompanyType updatedCompanyType = companyTypeRepository.findById(companyType.getId()).get();
        // Disconnect from session so that the updates on updatedCompanyType are not directly saved in db
        em.detach(updatedCompanyType);
        updatedCompanyType
            .name(UPDATED_NAME)
            .normalizedName(UPDATED_NORMALIZED_NAME)
            .description(UPDATED_DESCRIPTION)
            .minEmployeesQuantity(UPDATED_MIN_EMPLOYEES_QUANTITY)
            .maxEmployeesQuantity(UPDATED_MAX_EMPLOYEES_QUANTITY);

        restCompanyTypeMockMvc.perform(put("/api/company-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCompanyType)))
            .andExpect(status().isOk());

        // Validate the CompanyType in the database
        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeUpdate);
        CompanyType testCompanyType = companyTypeList.get(companyTypeList.size() - 1);
        assertThat(testCompanyType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompanyType.getNormalizedName()).isEqualTo(UPDATED_NORMALIZED_NAME);
        assertThat(testCompanyType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCompanyType.getMinEmployeesQuantity()).isEqualTo(UPDATED_MIN_EMPLOYEES_QUANTITY);
        assertThat(testCompanyType.getMaxEmployeesQuantity()).isEqualTo(UPDATED_MAX_EMPLOYEES_QUANTITY);

        // Validate the CompanyType in Elasticsearch
        verify(mockCompanyTypeSearchRepository, times(1)).save(testCompanyType);
    }

    @Test
    @Transactional
    public void updateNonExistingCompanyType() throws Exception {
        int databaseSizeBeforeUpdate = companyTypeRepository.findAll().size();

        // Create the CompanyType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyTypeMockMvc.perform(put("/api/company-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyType)))
            .andExpect(status().isBadRequest());

        // Validate the CompanyType in the database
        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CompanyType in Elasticsearch
        verify(mockCompanyTypeSearchRepository, times(0)).save(companyType);
    }

    @Test
    @Transactional
    public void deleteCompanyType() throws Exception {
        // Initialize the database
        companyTypeService.save(companyType);

        int databaseSizeBeforeDelete = companyTypeRepository.findAll().size();

        // Get the companyType
        restCompanyTypeMockMvc.perform(delete("/api/company-types/{id}", companyType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CompanyType in Elasticsearch
        verify(mockCompanyTypeSearchRepository, times(1)).deleteById(companyType.getId());
    }

    @Test
    @Transactional
    public void searchCompanyType() throws Exception {
        // Initialize the database
        companyTypeService.save(companyType);
        when(mockCompanyTypeSearchRepository.search(queryStringQuery("id:" + companyType.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(companyType), PageRequest.of(0, 1), 1));
        // Search the companyType
        restCompanyTypeMockMvc.perform(get("/api/_search/company-types?query=id:" + companyType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].normalizedName").value(hasItem(DEFAULT_NORMALIZED_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].minEmployeesQuantity").value(hasItem(DEFAULT_MIN_EMPLOYEES_QUANTITY)))
            .andExpect(jsonPath("$.[*].maxEmployeesQuantity").value(hasItem(DEFAULT_MAX_EMPLOYEES_QUANTITY)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyType.class);
        CompanyType companyType1 = new CompanyType();
        companyType1.setId(1L);
        CompanyType companyType2 = new CompanyType();
        companyType2.setId(companyType1.getId());
        assertThat(companyType1).isEqualTo(companyType2);
        companyType2.setId(2L);
        assertThat(companyType1).isNotEqualTo(companyType2);
        companyType1.setId(null);
        assertThat(companyType1).isNotEqualTo(companyType2);
    }
}
