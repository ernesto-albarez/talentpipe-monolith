package io.kimos.talentpipe.web.rest;

import io.kimos.talentpipe.MonolithApp;
import io.kimos.talentpipe.domain.SearchStatus;
import io.kimos.talentpipe.repository.SearchStatusRepository;
import io.kimos.talentpipe.repository.search.SearchStatusSearchRepository;
import io.kimos.talentpipe.service.SearchStatusService;
import io.kimos.talentpipe.service.UserService;
import io.kimos.talentpipe.web.rest.errors.ExceptionTranslator;
import ma.glasnost.orika.MapperFacade;
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

import static io.kimos.talentpipe.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SearchStatusResource REST controller.
 *
 * @see SearchStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MonolithApp.class)
public class SearchStatusResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NORMALIZED_NAME = "aaaaaaaaaa";
    private static final String UPDATED_NORMALIZED_NAME = "bbbbbbbbbb";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private SearchStatusRepository searchStatusRepository;

    @Autowired
    private SearchStatusService searchStatusService;

    @Autowired
    private MapperFacade orikaMapper;

    @Autowired
    private UserService userService;

    /**
     * This repository is mocked in the io.kimos.talentpipe.repository.search test package.
     *
     * @see io.kimos.talentpipe.repository.search.SearchStatusSearchRepositoryMockConfiguration
     */
    @Autowired
    private SearchStatusSearchRepository mockSearchStatusSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSearchStatusMockMvc;

    private SearchStatus searchStatus;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchStatus createEntity(EntityManager em) {
        SearchStatus searchStatus = new SearchStatus()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return searchStatus;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SearchStatusResource searchStatusResource = new SearchStatusResource(searchStatusService, orikaMapper, userService);
        this.restSearchStatusMockMvc = MockMvcBuilders.standaloneSetup(searchStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        searchStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createSearchStatus() throws Exception {
        int databaseSizeBeforeCreate = searchStatusRepository.findAll().size();

        // Create the SearchStatus
        restSearchStatusMockMvc.perform(post("/api/search-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchStatus)))
            .andExpect(status().isCreated());

        // Validate the SearchStatus in the database
        List<SearchStatus> searchStatusList = searchStatusRepository.findAll();
        assertThat(searchStatusList).hasSize(databaseSizeBeforeCreate + 1);
        SearchStatus testSearchStatus = searchStatusList.get(searchStatusList.size() - 1);
        assertThat(testSearchStatus.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSearchStatus.getNormalizedName()).isEqualTo(DEFAULT_NORMALIZED_NAME);
        assertThat(testSearchStatus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the SearchStatus in Elasticsearch
        verify(mockSearchStatusSearchRepository, times(1)).save(testSearchStatus);
    }

    @Test
    @Transactional
    public void createSearchStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = searchStatusRepository.findAll().size();

        // Create the SearchStatus with an existing ID
        searchStatus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSearchStatusMockMvc.perform(post("/api/search-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchStatus)))
            .andExpect(status().isBadRequest());

        // Validate the SearchStatus in the database
        List<SearchStatus> searchStatusList = searchStatusRepository.findAll();
        assertThat(searchStatusList).hasSize(databaseSizeBeforeCreate);

        // Validate the SearchStatus in Elasticsearch
        verify(mockSearchStatusSearchRepository, times(0)).save(searchStatus);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = searchStatusRepository.findAll().size();
        // set the field null
        searchStatus.setName(null);

        // Create the SearchStatus, which fails.

        restSearchStatusMockMvc.perform(post("/api/search-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchStatus)))
            .andExpect(status().isBadRequest());

        List<SearchStatus> searchStatusList = searchStatusRepository.findAll();
        assertThat(searchStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSearchStatuses() throws Exception {
        // Initialize the database
        searchStatusRepository.saveAndFlush(searchStatus);

        // Get all the searchStatusList
        restSearchStatusMockMvc.perform(get("/api/search-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].normalizedName").value(hasItem(DEFAULT_NORMALIZED_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getSearchStatus() throws Exception {
        // Initialize the database
        searchStatusRepository.saveAndFlush(searchStatus);

        // Get the searchStatus
        restSearchStatusMockMvc.perform(get("/api/search-statuses/{id}", searchStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(searchStatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.normalizedName").value(DEFAULT_NORMALIZED_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSearchStatus() throws Exception {
        // Get the searchStatus
        restSearchStatusMockMvc.perform(get("/api/search-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSearchStatus() throws Exception {
        // Initialize the database
        searchStatusService.save(searchStatus);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockSearchStatusSearchRepository);

        int databaseSizeBeforeUpdate = searchStatusRepository.findAll().size();

        // Update the searchStatus
        SearchStatus updatedSearchStatus = searchStatusRepository.findById(searchStatus.getId()).get();
        // Disconnect from session so that the updates on updatedSearchStatus are not directly saved in db
        em.detach(updatedSearchStatus);
        updatedSearchStatus
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restSearchStatusMockMvc.perform(put("/api/search-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSearchStatus)))
            .andExpect(status().isOk());

        // Validate the SearchStatus in the database
        List<SearchStatus> searchStatusList = searchStatusRepository.findAll();
        assertThat(searchStatusList).hasSize(databaseSizeBeforeUpdate);
        SearchStatus testSearchStatus = searchStatusList.get(searchStatusList.size() - 1);
        assertThat(testSearchStatus.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSearchStatus.getNormalizedName()).isEqualTo(UPDATED_NORMALIZED_NAME);
        assertThat(testSearchStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the SearchStatus in Elasticsearch
        verify(mockSearchStatusSearchRepository, times(1)).save(testSearchStatus);
    }

    @Test
    @Transactional
    public void updateNonExistingSearchStatus() throws Exception {
        int databaseSizeBeforeUpdate = searchStatusRepository.findAll().size();

        // Create the SearchStatus

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchStatusMockMvc.perform(put("/api/search-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchStatus)))
            .andExpect(status().isBadRequest());

        // Validate the SearchStatus in the database
        List<SearchStatus> searchStatusList = searchStatusRepository.findAll();
        assertThat(searchStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SearchStatus in Elasticsearch
        verify(mockSearchStatusSearchRepository, times(0)).save(searchStatus);
    }

    @Test
    @Transactional
    public void deleteSearchStatus() throws Exception {
        // Initialize the database
        searchStatusService.save(searchStatus);

        int databaseSizeBeforeDelete = searchStatusRepository.findAll().size();

        // Get the searchStatus
        restSearchStatusMockMvc.perform(delete("/api/search-statuses/{id}", searchStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SearchStatus> searchStatusList = searchStatusRepository.findAll();
        assertThat(searchStatusList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SearchStatus in Elasticsearch
        verify(mockSearchStatusSearchRepository, times(1)).deleteById(searchStatus.getId());
    }

    @Test
    @Transactional
    public void searchSearchStatus() throws Exception {
        // Initialize the database
        searchStatusService.save(searchStatus);
        when(mockSearchStatusSearchRepository.search(queryStringQuery("id:" + searchStatus.getId())))
            .thenReturn(Collections.singletonList(searchStatus));
        // Search the searchStatus
        restSearchStatusMockMvc.perform(get("/api/_search/search-statuses?query=id:" + searchStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].normalizedName").value(hasItem(DEFAULT_NORMALIZED_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SearchStatus.class);
        SearchStatus searchStatus1 = new SearchStatus();
        searchStatus1.setId(1L);
        SearchStatus searchStatus2 = new SearchStatus();
        searchStatus2.setId(searchStatus1.getId());
        assertThat(searchStatus1).isEqualTo(searchStatus2);
        searchStatus2.setId(2L);
        assertThat(searchStatus1).isNotEqualTo(searchStatus2);
        searchStatus1.setId(null);
        assertThat(searchStatus1).isNotEqualTo(searchStatus2);
    }
}
