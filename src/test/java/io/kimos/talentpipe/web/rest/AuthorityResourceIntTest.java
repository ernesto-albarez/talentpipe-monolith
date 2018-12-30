package io.kimos.talentpipe.web.rest;

import io.kimos.talentpipe.MonolithApp;

import io.kimos.talentpipe.domain.Authority;
import io.kimos.talentpipe.repository.AuthorityRepository;
import io.kimos.talentpipe.repository.search.AuthoritySearchRepository;
import io.kimos.talentpipe.service.AuthorityService;
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
 * Test class for the AuthorityResource REST controller.
 *
 * @see AuthorityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MonolithApp.class)
public class AuthorityResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private AuthorityService authorityService;

    /**
     * This repository is mocked in the io.kimos.talentpipe.repository.search test package.
     *
     * @see io.kimos.talentpipe.repository.search.AuthoritySearchRepositoryMockConfiguration
     */
    @Autowired
    private AuthoritySearchRepository mockAuthoritySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAuthorityMockMvc;

    private Authority authority;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AuthorityResource authorityResource = new AuthorityResource(authorityService);
        this.restAuthorityMockMvc = MockMvcBuilders.standaloneSetup(authorityResource)
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
    public static Authority createEntity(EntityManager em) {
        Authority authority = new Authority()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return authority;
    }

    @Before
    public void initTest() {
        authority = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuthority() throws Exception {
        int databaseSizeBeforeCreate = authorityRepository.findAll().size();

        // Create the Authority
        restAuthorityMockMvc.perform(post("/api/authorities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authority)))
            .andExpect(status().isCreated());

        // Validate the Authority in the database
        List<Authority> authorityList = authorityRepository.findAll();
        assertThat(authorityList).hasSize(databaseSizeBeforeCreate + 1);
        Authority testAuthority = authorityList.get(authorityList.size() - 1);
        assertThat(testAuthority.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAuthority.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Authority in Elasticsearch
        verify(mockAuthoritySearchRepository, times(1)).save(testAuthority);
    }

    @Test
    @Transactional
    public void createAuthorityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = authorityRepository.findAll().size();

        // Create the Authority with an existing ID
        authority.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuthorityMockMvc.perform(post("/api/authorities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authority)))
            .andExpect(status().isBadRequest());

        // Validate the Authority in the database
        List<Authority> authorityList = authorityRepository.findAll();
        assertThat(authorityList).hasSize(databaseSizeBeforeCreate);

        // Validate the Authority in Elasticsearch
        verify(mockAuthoritySearchRepository, times(0)).save(authority);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorityRepository.findAll().size();
        // set the field null
        authority.setName(null);

        // Create the Authority, which fails.

        restAuthorityMockMvc.perform(post("/api/authorities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authority)))
            .andExpect(status().isBadRequest());

        List<Authority> authorityList = authorityRepository.findAll();
        assertThat(authorityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAuthorities() throws Exception {
        // Initialize the database
        authorityRepository.saveAndFlush(authority);

        // Get all the authorityList
        restAuthorityMockMvc.perform(get("/api/authorities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authority.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getAuthority() throws Exception {
        // Initialize the database
        authorityRepository.saveAndFlush(authority);

        // Get the authority
        restAuthorityMockMvc.perform(get("/api/authorities/{id}", authority.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(authority.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAuthority() throws Exception {
        // Get the authority
        restAuthorityMockMvc.perform(get("/api/authorities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuthority() throws Exception {
        // Initialize the database
        authorityService.save(authority);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockAuthoritySearchRepository);

        int databaseSizeBeforeUpdate = authorityRepository.findAll().size();

        // Update the authority
        Authority updatedAuthority = authorityRepository.findById(authority.getId()).get();
        // Disconnect from session so that the updates on updatedAuthority are not directly saved in db
        em.detach(updatedAuthority);
        updatedAuthority
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restAuthorityMockMvc.perform(put("/api/authorities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAuthority)))
            .andExpect(status().isOk());

        // Validate the Authority in the database
        List<Authority> authorityList = authorityRepository.findAll();
        assertThat(authorityList).hasSize(databaseSizeBeforeUpdate);
        Authority testAuthority = authorityList.get(authorityList.size() - 1);
        assertThat(testAuthority.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAuthority.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Authority in Elasticsearch
        verify(mockAuthoritySearchRepository, times(1)).save(testAuthority);
    }

    @Test
    @Transactional
    public void updateNonExistingAuthority() throws Exception {
        int databaseSizeBeforeUpdate = authorityRepository.findAll().size();

        // Create the Authority

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthorityMockMvc.perform(put("/api/authorities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authority)))
            .andExpect(status().isBadRequest());

        // Validate the Authority in the database
        List<Authority> authorityList = authorityRepository.findAll();
        assertThat(authorityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Authority in Elasticsearch
        verify(mockAuthoritySearchRepository, times(0)).save(authority);
    }

    @Test
    @Transactional
    public void deleteAuthority() throws Exception {
        // Initialize the database
        authorityService.save(authority);

        int databaseSizeBeforeDelete = authorityRepository.findAll().size();

        // Get the authority
        restAuthorityMockMvc.perform(delete("/api/authorities/{id}", authority.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Authority> authorityList = authorityRepository.findAll();
        assertThat(authorityList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Authority in Elasticsearch
        verify(mockAuthoritySearchRepository, times(1)).deleteById(authority.getId());
    }

    @Test
    @Transactional
    public void searchAuthority() throws Exception {
        // Initialize the database
        authorityService.save(authority);
        when(mockAuthoritySearchRepository.search(queryStringQuery("id:" + authority.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(authority), PageRequest.of(0, 1), 1));
        // Search the authority
        restAuthorityMockMvc.perform(get("/api/_search/authorities?query=id:" + authority.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authority.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Authority.class);
        Authority authority1 = new Authority();
        authority1.setId(1L);
        Authority authority2 = new Authority();
        authority2.setId(authority1.getId());
        assertThat(authority1).isEqualTo(authority2);
        authority2.setId(2L);
        assertThat(authority1).isNotEqualTo(authority2);
        authority1.setId(null);
        assertThat(authority1).isNotEqualTo(authority2);
    }
}
