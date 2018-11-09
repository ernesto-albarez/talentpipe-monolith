package io.kimos.talentpipe.web.rest;

import io.kimos.talentpipe.TalentpipeApp;
import io.kimos.talentpipe.domain.SearchType;
import io.kimos.talentpipe.repository.SearchTypeRepository;
import io.kimos.talentpipe.repository.search.SearchTypeSearchRepository;
import io.kimos.talentpipe.service.SearchTypeService;
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
 * Test class for the SearchTypeResource REST controller.
 *
 * @see SearchTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TalentpipeApp.class)
public class SearchTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NORMALIZED_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NORMALIZED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private SearchTypeRepository searchTypeRepository;

    @Autowired
    private SearchTypeService searchTypeService;

    /**
     * This repository is mocked in the io.kimos.talentpipe.repository.search test package.
     *
     * @see io.kimos.talentpipe.repository.search.SearchTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private SearchTypeSearchRepository mockSearchTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSearchTypeMockMvc;

    private SearchType searchType;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchType createEntity(EntityManager em) {
        SearchType searchType = new SearchType()
            .name(DEFAULT_NAME)
            .normalizedName(DEFAULT_NORMALIZED_NAME)
            .description(DEFAULT_DESCRIPTION);
        return searchType;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SearchTypeResource searchTypeResource = new SearchTypeResource(searchTypeService);
        this.restSearchTypeMockMvc = MockMvcBuilders.standaloneSetup(searchTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        searchType = createEntity(em);
    }

    @Test
    @Transactional
    public void createSearchType() throws Exception {
        int databaseSizeBeforeCreate = searchTypeRepository.findAll().size();

        // Create the SearchType
        restSearchTypeMockMvc.perform(post("/api/search-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchType)))
            .andExpect(status().isCreated());

        // Validate the SearchType in the database
        List<SearchType> searchTypeList = searchTypeRepository.findAll();
        assertThat(searchTypeList).hasSize(databaseSizeBeforeCreate + 1);
        SearchType testSearchType = searchTypeList.get(searchTypeList.size() - 1);
        assertThat(testSearchType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSearchType.getNormalizedName()).isEqualTo(DEFAULT_NORMALIZED_NAME);
        assertThat(testSearchType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the SearchType in Elasticsearch
        verify(mockSearchTypeSearchRepository, times(1)).save(testSearchType);
    }

    @Test
    @Transactional
    public void createSearchTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = searchTypeRepository.findAll().size();

        // Create the SearchType with an existing ID
        searchType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSearchTypeMockMvc.perform(post("/api/search-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchType)))
            .andExpect(status().isBadRequest());

        // Validate the SearchType in the database
        List<SearchType> searchTypeList = searchTypeRepository.findAll();
        assertThat(searchTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the SearchType in Elasticsearch
        verify(mockSearchTypeSearchRepository, times(0)).save(searchType);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = searchTypeRepository.findAll().size();
        // set the field null
        searchType.setName(null);

        // Create the SearchType, which fails.

        restSearchTypeMockMvc.perform(post("/api/search-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchType)))
            .andExpect(status().isBadRequest());

        List<SearchType> searchTypeList = searchTypeRepository.findAll();
        assertThat(searchTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNormalizedNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = searchTypeRepository.findAll().size();
        // set the field null
        searchType.setNormalizedName(null);

        // Create the SearchType, which fails.

        restSearchTypeMockMvc.perform(post("/api/search-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchType)))
            .andExpect(status().isBadRequest());

        List<SearchType> searchTypeList = searchTypeRepository.findAll();
        assertThat(searchTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSearchTypes() throws Exception {
        // Initialize the database
        searchTypeRepository.saveAndFlush(searchType);

        // Get all the searchTypeList
        restSearchTypeMockMvc.perform(get("/api/search-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].normalizedName").value(hasItem(DEFAULT_NORMALIZED_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getSearchType() throws Exception {
        // Initialize the database
        searchTypeRepository.saveAndFlush(searchType);

        // Get the searchType
        restSearchTypeMockMvc.perform(get("/api/search-types/{id}", searchType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(searchType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.normalizedName").value(DEFAULT_NORMALIZED_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSearchType() throws Exception {
        // Get the searchType
        restSearchTypeMockMvc.perform(get("/api/search-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSearchType() throws Exception {
        // Initialize the database
        searchTypeService.save(searchType);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockSearchTypeSearchRepository);

        int databaseSizeBeforeUpdate = searchTypeRepository.findAll().size();

        // Update the searchType
        SearchType updatedSearchType = searchTypeRepository.findById(searchType.getId()).get();
        // Disconnect from session so that the updates on updatedSearchType are not directly saved in db
        em.detach(updatedSearchType);
        updatedSearchType
            .name(UPDATED_NAME)
            .normalizedName(UPDATED_NORMALIZED_NAME)
            .description(UPDATED_DESCRIPTION);

        restSearchTypeMockMvc.perform(put("/api/search-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSearchType)))
            .andExpect(status().isOk());

        // Validate the SearchType in the database
        List<SearchType> searchTypeList = searchTypeRepository.findAll();
        assertThat(searchTypeList).hasSize(databaseSizeBeforeUpdate);
        SearchType testSearchType = searchTypeList.get(searchTypeList.size() - 1);
        assertThat(testSearchType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSearchType.getNormalizedName()).isEqualTo(UPDATED_NORMALIZED_NAME);
        assertThat(testSearchType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the SearchType in Elasticsearch
        verify(mockSearchTypeSearchRepository, times(1)).save(testSearchType);
    }

    @Test
    @Transactional
    public void updateNonExistingSearchType() throws Exception {
        int databaseSizeBeforeUpdate = searchTypeRepository.findAll().size();

        // Create the SearchType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchTypeMockMvc.perform(put("/api/search-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchType)))
            .andExpect(status().isBadRequest());

        // Validate the SearchType in the database
        List<SearchType> searchTypeList = searchTypeRepository.findAll();
        assertThat(searchTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SearchType in Elasticsearch
        verify(mockSearchTypeSearchRepository, times(0)).save(searchType);
    }

    @Test
    @Transactional
    public void deleteSearchType() throws Exception {
        // Initialize the database
        searchTypeService.save(searchType);

        int databaseSizeBeforeDelete = searchTypeRepository.findAll().size();

        // Get the searchType
        restSearchTypeMockMvc.perform(delete("/api/search-types/{id}", searchType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SearchType> searchTypeList = searchTypeRepository.findAll();
        assertThat(searchTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SearchType in Elasticsearch
        verify(mockSearchTypeSearchRepository, times(1)).deleteById(searchType.getId());
    }

    @Test
    @Transactional
    public void searchSearchType() throws Exception {
        // Initialize the database
        searchTypeService.save(searchType);
        when(mockSearchTypeSearchRepository.search(queryStringQuery("id:" + searchType.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(searchType), PageRequest.of(0, 1), 1));
        // Search the searchType
        restSearchTypeMockMvc.perform(get("/api/_search/search-types?query=id:" + searchType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].normalizedName").value(hasItem(DEFAULT_NORMALIZED_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SearchType.class);
        SearchType searchType1 = new SearchType();
        searchType1.setId(1L);
        SearchType searchType2 = new SearchType();
        searchType2.setId(searchType1.getId());
        assertThat(searchType1).isEqualTo(searchType2);
        searchType2.setId(2L);
        assertThat(searchType1).isNotEqualTo(searchType2);
        searchType1.setId(null);
        assertThat(searchType1).isNotEqualTo(searchType2);
    }
}
