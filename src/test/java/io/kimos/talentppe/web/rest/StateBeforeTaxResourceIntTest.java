package io.kimos.talentppe.web.rest;

import io.kimos.talentppe.MonolithApp;
import io.kimos.talentppe.domain.StateBeforeTax;
import io.kimos.talentppe.repository.StateBeforeTaxRepository;
import io.kimos.talentppe.repository.search.StateBeforeTaxSearchRepository;
import io.kimos.talentppe.service.StateBeforeTaxService;
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
 * Test class for the StateBeforeTaxResource REST controller.
 *
 * @see StateBeforeTaxResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MonolithApp.class)
public class StateBeforeTaxResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NORMALIZED_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NORMALIZED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private StateBeforeTaxRepository stateBeforeTaxRepository;

    @Autowired
    private StateBeforeTaxService stateBeforeTaxService;

    /**
     * This repository is mocked in the io.kimos.talentppe.repository.search test package.
     *
     * @see io.kimos.talentppe.repository.search.StateBeforeTaxSearchRepositoryMockConfiguration
     */
    @Autowired
    private StateBeforeTaxSearchRepository mockStateBeforeTaxSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStateBeforeTaxMockMvc;

    private StateBeforeTax stateBeforeTax;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StateBeforeTax createEntity(EntityManager em) {
        StateBeforeTax stateBeforeTax = new StateBeforeTax()
            .name(DEFAULT_NAME)
            .normalizedName(DEFAULT_NORMALIZED_NAME)
            .description(DEFAULT_DESCRIPTION);
        return stateBeforeTax;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StateBeforeTaxResource stateBeforeTaxResource = new StateBeforeTaxResource(stateBeforeTaxService);
        this.restStateBeforeTaxMockMvc = MockMvcBuilders.standaloneSetup(stateBeforeTaxResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        stateBeforeTax = createEntity(em);
    }

    @Test
    @Transactional
    public void createStateBeforeTax() throws Exception {
        int databaseSizeBeforeCreate = stateBeforeTaxRepository.findAll().size();

        // Create the StateBeforeTax
        restStateBeforeTaxMockMvc.perform(post("/api/state-before-taxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stateBeforeTax)))
            .andExpect(status().isCreated());

        // Validate the StateBeforeTax in the database
        List<StateBeforeTax> stateBeforeTaxList = stateBeforeTaxRepository.findAll();
        assertThat(stateBeforeTaxList).hasSize(databaseSizeBeforeCreate + 1);
        StateBeforeTax testStateBeforeTax = stateBeforeTaxList.get(stateBeforeTaxList.size() - 1);
        assertThat(testStateBeforeTax.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStateBeforeTax.getNormalizedName()).isEqualTo(DEFAULT_NORMALIZED_NAME);
        assertThat(testStateBeforeTax.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the StateBeforeTax in Elasticsearch
        verify(mockStateBeforeTaxSearchRepository, times(1)).save(testStateBeforeTax);
    }

    @Test
    @Transactional
    public void createStateBeforeTaxWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stateBeforeTaxRepository.findAll().size();

        // Create the StateBeforeTax with an existing ID
        stateBeforeTax.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStateBeforeTaxMockMvc.perform(post("/api/state-before-taxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stateBeforeTax)))
            .andExpect(status().isBadRequest());

        // Validate the StateBeforeTax in the database
        List<StateBeforeTax> stateBeforeTaxList = stateBeforeTaxRepository.findAll();
        assertThat(stateBeforeTaxList).hasSize(databaseSizeBeforeCreate);

        // Validate the StateBeforeTax in Elasticsearch
        verify(mockStateBeforeTaxSearchRepository, times(0)).save(stateBeforeTax);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = stateBeforeTaxRepository.findAll().size();
        // set the field null
        stateBeforeTax.setName(null);

        // Create the StateBeforeTax, which fails.

        restStateBeforeTaxMockMvc.perform(post("/api/state-before-taxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stateBeforeTax)))
            .andExpect(status().isBadRequest());

        List<StateBeforeTax> stateBeforeTaxList = stateBeforeTaxRepository.findAll();
        assertThat(stateBeforeTaxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNormalizedNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = stateBeforeTaxRepository.findAll().size();
        // set the field null
        stateBeforeTax.setNormalizedName(null);

        // Create the StateBeforeTax, which fails.

        restStateBeforeTaxMockMvc.perform(post("/api/state-before-taxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stateBeforeTax)))
            .andExpect(status().isBadRequest());

        List<StateBeforeTax> stateBeforeTaxList = stateBeforeTaxRepository.findAll();
        assertThat(stateBeforeTaxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStateBeforeTaxes() throws Exception {
        // Initialize the database
        stateBeforeTaxRepository.saveAndFlush(stateBeforeTax);

        // Get all the stateBeforeTaxList
        restStateBeforeTaxMockMvc.perform(get("/api/state-before-taxes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stateBeforeTax.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].normalizedName").value(hasItem(DEFAULT_NORMALIZED_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getStateBeforeTax() throws Exception {
        // Initialize the database
        stateBeforeTaxRepository.saveAndFlush(stateBeforeTax);

        // Get the stateBeforeTax
        restStateBeforeTaxMockMvc.perform(get("/api/state-before-taxes/{id}", stateBeforeTax.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stateBeforeTax.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.normalizedName").value(DEFAULT_NORMALIZED_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStateBeforeTax() throws Exception {
        // Get the stateBeforeTax
        restStateBeforeTaxMockMvc.perform(get("/api/state-before-taxes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStateBeforeTax() throws Exception {
        // Initialize the database
        stateBeforeTaxService.save(stateBeforeTax);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockStateBeforeTaxSearchRepository);

        int databaseSizeBeforeUpdate = stateBeforeTaxRepository.findAll().size();

        // Update the stateBeforeTax
        StateBeforeTax updatedStateBeforeTax = stateBeforeTaxRepository.findById(stateBeforeTax.getId()).get();
        // Disconnect from session so that the updates on updatedStateBeforeTax are not directly saved in db
        em.detach(updatedStateBeforeTax);
        updatedStateBeforeTax
            .name(UPDATED_NAME)
            .normalizedName(UPDATED_NORMALIZED_NAME)
            .description(UPDATED_DESCRIPTION);

        restStateBeforeTaxMockMvc.perform(put("/api/state-before-taxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStateBeforeTax)))
            .andExpect(status().isOk());

        // Validate the StateBeforeTax in the database
        List<StateBeforeTax> stateBeforeTaxList = stateBeforeTaxRepository.findAll();
        assertThat(stateBeforeTaxList).hasSize(databaseSizeBeforeUpdate);
        StateBeforeTax testStateBeforeTax = stateBeforeTaxList.get(stateBeforeTaxList.size() - 1);
        assertThat(testStateBeforeTax.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStateBeforeTax.getNormalizedName()).isEqualTo(UPDATED_NORMALIZED_NAME);
        assertThat(testStateBeforeTax.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the StateBeforeTax in Elasticsearch
        verify(mockStateBeforeTaxSearchRepository, times(1)).save(testStateBeforeTax);
    }

    @Test
    @Transactional
    public void updateNonExistingStateBeforeTax() throws Exception {
        int databaseSizeBeforeUpdate = stateBeforeTaxRepository.findAll().size();

        // Create the StateBeforeTax

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStateBeforeTaxMockMvc.perform(put("/api/state-before-taxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stateBeforeTax)))
            .andExpect(status().isBadRequest());

        // Validate the StateBeforeTax in the database
        List<StateBeforeTax> stateBeforeTaxList = stateBeforeTaxRepository.findAll();
        assertThat(stateBeforeTaxList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StateBeforeTax in Elasticsearch
        verify(mockStateBeforeTaxSearchRepository, times(0)).save(stateBeforeTax);
    }

    @Test
    @Transactional
    public void deleteStateBeforeTax() throws Exception {
        // Initialize the database
        stateBeforeTaxService.save(stateBeforeTax);

        int databaseSizeBeforeDelete = stateBeforeTaxRepository.findAll().size();

        // Get the stateBeforeTax
        restStateBeforeTaxMockMvc.perform(delete("/api/state-before-taxes/{id}", stateBeforeTax.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StateBeforeTax> stateBeforeTaxList = stateBeforeTaxRepository.findAll();
        assertThat(stateBeforeTaxList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the StateBeforeTax in Elasticsearch
        verify(mockStateBeforeTaxSearchRepository, times(1)).deleteById(stateBeforeTax.getId());
    }

    @Test
    @Transactional
    public void searchStateBeforeTax() throws Exception {
        // Initialize the database
        stateBeforeTaxService.save(stateBeforeTax);
        when(mockStateBeforeTaxSearchRepository.search(queryStringQuery("id:" + stateBeforeTax.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(stateBeforeTax), PageRequest.of(0, 1), 1));
        // Search the stateBeforeTax
        restStateBeforeTaxMockMvc.perform(get("/api/_search/state-before-taxes?query=id:" + stateBeforeTax.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stateBeforeTax.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].normalizedName").value(hasItem(DEFAULT_NORMALIZED_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StateBeforeTax.class);
        StateBeforeTax stateBeforeTax1 = new StateBeforeTax();
        stateBeforeTax1.setId(1L);
        StateBeforeTax stateBeforeTax2 = new StateBeforeTax();
        stateBeforeTax2.setId(stateBeforeTax1.getId());
        assertThat(stateBeforeTax1).isEqualTo(stateBeforeTax2);
        stateBeforeTax2.setId(2L);
        assertThat(stateBeforeTax1).isNotEqualTo(stateBeforeTax2);
        stateBeforeTax1.setId(null);
        assertThat(stateBeforeTax1).isNotEqualTo(stateBeforeTax2);
    }
}
