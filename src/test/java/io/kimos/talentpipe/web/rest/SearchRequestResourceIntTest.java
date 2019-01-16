package io.kimos.talentpipe.web.rest;

import io.kimos.talentpipe.MonolithApp;
import io.kimos.talentpipe.domain.Company;
import io.kimos.talentpipe.domain.SearchRequest;
import io.kimos.talentpipe.repository.SearchRequestRepository;
import io.kimos.talentpipe.repository.search.SearchRequestSearchRepository;
import io.kimos.talentpipe.service.SearchRequestService;
import io.kimos.talentpipe.service.UserService;
import io.kimos.talentpipe.web.rest.errors.ExceptionTranslator;
import ma.glasnost.orika.MapperFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import java.math.BigDecimal;
import java.util.ArrayList;
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
 * Test class for the SearchRequestResource REST controller.
 *
 * @see SearchRequestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MonolithApp.class)
public class SearchRequestResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_MIN_SALARY = new BigDecimal(0);
    private static final BigDecimal UPDATED_MIN_SALARY = new BigDecimal(1);

    private static final BigDecimal DEFAULT_MAX_SALARY = new BigDecimal(1);
    private static final BigDecimal UPDATED_MAX_SALARY = new BigDecimal(2);

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    @Autowired
    private SearchRequestRepository searchRequestRepository;

    @Mock
    private SearchRequestRepository searchRequestRepositoryMock;

    @Mock
    private SearchRequestService searchRequestServiceMock;

    @Autowired
    private SearchRequestService searchRequestService;

    /**
     * This repository is mocked in the io.kimos.talentppe.repository.search test package.
     *
     * @see io.kimos.talentpipe.repository.search.SearchRequestSearchRepositoryMockConfiguration
     */
    @Autowired
    private SearchRequestSearchRepository mockSearchRequestSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSearchRequestMockMvc;

    private SearchRequest searchRequest;

    private UserService userService;

    private MapperFacade orikaMapper;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchRequest createEntity(EntityManager em) {
        SearchRequest searchRequest = new SearchRequest()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .minSalary(DEFAULT_MIN_SALARY)
            .maxSalary(DEFAULT_MAX_SALARY)
            .position(DEFAULT_POSITION);
        // Add required entity
        Company company = CompanyResourceIntTest.createEntity(em);
        em.persist(company);
        em.flush();
        searchRequest.setCompany(company);
        return searchRequest;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SearchRequestResource searchRequestResource = new SearchRequestResource(searchRequestService, orikaMapper, userService);
        this.restSearchRequestMockMvc = MockMvcBuilders.standaloneSetup(searchRequestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        searchRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createSearchRequest() throws Exception {
        int databaseSizeBeforeCreate = searchRequestRepository.findAll().size();

        // Create the SearchRequest
        restSearchRequestMockMvc.perform(post("/api/search-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchRequest)))
            .andExpect(status().isCreated());

        // Validate the SearchRequest in the database
        List<SearchRequest> searchRequestList = searchRequestRepository.findAll();
        assertThat(searchRequestList).hasSize(databaseSizeBeforeCreate + 1);
        SearchRequest testSearchRequest = searchRequestList.get(searchRequestList.size() - 1);
        assertThat(testSearchRequest.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSearchRequest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSearchRequest.getMinSalary()).isEqualTo(DEFAULT_MIN_SALARY);
        assertThat(testSearchRequest.getMaxSalary()).isEqualTo(DEFAULT_MAX_SALARY);
        assertThat(testSearchRequest.getPosition()).isEqualTo(DEFAULT_POSITION);

        // Validate the SearchRequest in Elasticsearch
        verify(mockSearchRequestSearchRepository, times(1)).save(testSearchRequest);
    }

    @Test
    @Transactional
    public void createSearchRequestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = searchRequestRepository.findAll().size();

        // Create the SearchRequest with an existing ID
        searchRequest.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSearchRequestMockMvc.perform(post("/api/search-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchRequest)))
            .andExpect(status().isBadRequest());

        // Validate the SearchRequest in the database
        List<SearchRequest> searchRequestList = searchRequestRepository.findAll();
        assertThat(searchRequestList).hasSize(databaseSizeBeforeCreate);

        // Validate the SearchRequest in Elasticsearch
        verify(mockSearchRequestSearchRepository, times(0)).save(searchRequest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = searchRequestRepository.findAll().size();
        // set the field null
        searchRequest.setName(null);

        // Create the SearchRequest, which fails.

        restSearchRequestMockMvc.perform(post("/api/search-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchRequest)))
            .andExpect(status().isBadRequest());

        List<SearchRequest> searchRequestList = searchRequestRepository.findAll();
        assertThat(searchRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = searchRequestRepository.findAll().size();
        // set the field null
        searchRequest.setDescription(null);

        // Create the SearchRequest, which fails.

        restSearchRequestMockMvc.perform(post("/api/search-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchRequest)))
            .andExpect(status().isBadRequest());

        List<SearchRequest> searchRequestList = searchRequestRepository.findAll();
        assertThat(searchRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMinSalaryIsRequired() throws Exception {
        int databaseSizeBeforeTest = searchRequestRepository.findAll().size();
        // set the field null
        searchRequest.setMinSalary(null);

        // Create the SearchRequest, which fails.

        restSearchRequestMockMvc.perform(post("/api/search-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchRequest)))
            .andExpect(status().isBadRequest());

        List<SearchRequest> searchRequestList = searchRequestRepository.findAll();
        assertThat(searchRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPositionIsRequired() throws Exception {
        int databaseSizeBeforeTest = searchRequestRepository.findAll().size();
        // set the field null
        searchRequest.setPosition(null);

        // Create the SearchRequest, which fails.

        restSearchRequestMockMvc.perform(post("/api/search-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchRequest)))
            .andExpect(status().isBadRequest());

        List<SearchRequest> searchRequestList = searchRequestRepository.findAll();
        assertThat(searchRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSearchRequests() throws Exception {
        // Initialize the database
        searchRequestRepository.saveAndFlush(searchRequest);

        // Get all the searchRequestList
        restSearchRequestMockMvc.perform(get("/api/search-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].minSalary").value(hasItem(DEFAULT_MIN_SALARY.intValue())))
            .andExpect(jsonPath("$.[*].maxSalary").value(hasItem(DEFAULT_MAX_SALARY.intValue())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION.toString())));
    }

    @SuppressWarnings({"unchecked"})
    public void getAllSearchRequestsWithEagerRelationshipsIsEnabled() throws Exception {
        SearchRequestResource searchRequestResource = new SearchRequestResource(searchRequestServiceMock, orikaMapper, userService);
        when(searchRequestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restSearchRequestMockMvc = MockMvcBuilders.standaloneSetup(searchRequestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restSearchRequestMockMvc.perform(get("/api/search-requests?eagerload=true"))
            .andExpect(status().isOk());

        verify(searchRequestServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllSearchRequestsWithEagerRelationshipsIsNotEnabled() throws Exception {
        SearchRequestResource searchRequestResource = new SearchRequestResource(searchRequestServiceMock, orikaMapper, userService);
        when(searchRequestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
        MockMvc restSearchRequestMockMvc = MockMvcBuilders.standaloneSetup(searchRequestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restSearchRequestMockMvc.perform(get("/api/search-requests?eagerload=true"))
            .andExpect(status().isOk());

        verify(searchRequestServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getSearchRequest() throws Exception {
        // Initialize the database
        searchRequestRepository.saveAndFlush(searchRequest);

        // Get the searchRequest
        restSearchRequestMockMvc.perform(get("/api/search-requests/{id}", searchRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(searchRequest.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.minSalary").value(DEFAULT_MIN_SALARY.intValue()))
            .andExpect(jsonPath("$.maxSalary").value(DEFAULT_MAX_SALARY.intValue()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSearchRequest() throws Exception {
        // Get the searchRequest
        restSearchRequestMockMvc.perform(get("/api/search-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSearchRequest() throws Exception {
        // Initialize the database
        searchRequestService.save(searchRequest);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockSearchRequestSearchRepository);

        int databaseSizeBeforeUpdate = searchRequestRepository.findAll().size();

        // Update the searchRequest
        SearchRequest updatedSearchRequest = searchRequestRepository.findById(searchRequest.getId()).get();
        // Disconnect from session so that the updates on updatedSearchRequest are not directly saved in db
        em.detach(updatedSearchRequest);
        updatedSearchRequest
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .minSalary(UPDATED_MIN_SALARY)
            .maxSalary(UPDATED_MAX_SALARY)
            .position(UPDATED_POSITION);

        restSearchRequestMockMvc.perform(put("/api/search-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSearchRequest)))
            .andExpect(status().isOk());

        // Validate the SearchRequest in the database
        List<SearchRequest> searchRequestList = searchRequestRepository.findAll();
        assertThat(searchRequestList).hasSize(databaseSizeBeforeUpdate);
        SearchRequest testSearchRequest = searchRequestList.get(searchRequestList.size() - 1);
        assertThat(testSearchRequest.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSearchRequest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSearchRequest.getMinSalary()).isEqualTo(UPDATED_MIN_SALARY);
        assertThat(testSearchRequest.getMaxSalary()).isEqualTo(UPDATED_MAX_SALARY);
        assertThat(testSearchRequest.getPosition()).isEqualTo(UPDATED_POSITION);

        // Validate the SearchRequest in Elasticsearch
        verify(mockSearchRequestSearchRepository, times(1)).save(testSearchRequest);
    }

    @Test
    @Transactional
    public void updateNonExistingSearchRequest() throws Exception {
        int databaseSizeBeforeUpdate = searchRequestRepository.findAll().size();

        // Create the SearchRequest

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchRequestMockMvc.perform(put("/api/search-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchRequest)))
            .andExpect(status().isBadRequest());

        // Validate the SearchRequest in the database
        List<SearchRequest> searchRequestList = searchRequestRepository.findAll();
        assertThat(searchRequestList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SearchRequest in Elasticsearch
        verify(mockSearchRequestSearchRepository, times(0)).save(searchRequest);
    }

    @Test
    @Transactional
    public void deleteSearchRequest() throws Exception {
        // Initialize the database
        searchRequestService.save(searchRequest);

        int databaseSizeBeforeDelete = searchRequestRepository.findAll().size();

        // Get the searchRequest
        restSearchRequestMockMvc.perform(delete("/api/search-requests/{id}", searchRequest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SearchRequest> searchRequestList = searchRequestRepository.findAll();
        assertThat(searchRequestList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SearchRequest in Elasticsearch
        verify(mockSearchRequestSearchRepository, times(1)).deleteById(searchRequest.getId());
    }

    @Test
    @Transactional
    public void searchSearchRequest() throws Exception {
        // Initialize the database
        searchRequestService.save(searchRequest);
        when(mockSearchRequestSearchRepository.search(queryStringQuery("id:" + searchRequest.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(searchRequest), PageRequest.of(0, 1), 1));
        // Search the searchRequest
        restSearchRequestMockMvc.perform(get("/api/_search/search-requests?query=id:" + searchRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].minSalary").value(hasItem(DEFAULT_MIN_SALARY.intValue())))
            .andExpect(jsonPath("$.[*].maxSalary").value(hasItem(DEFAULT_MAX_SALARY.intValue())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SearchRequest.class);
        SearchRequest searchRequest1 = new SearchRequest();
        searchRequest1.setId(1L);
        SearchRequest searchRequest2 = new SearchRequest();
        searchRequest2.setId(searchRequest1.getId());
        assertThat(searchRequest1).isEqualTo(searchRequest2);
        searchRequest2.setId(2L);
        assertThat(searchRequest1).isNotEqualTo(searchRequest2);
        searchRequest1.setId(null);
        assertThat(searchRequest1).isNotEqualTo(searchRequest2);
    }
}
