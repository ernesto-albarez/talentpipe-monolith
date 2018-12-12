package io.kimos.talentppe.web.rest;

import io.kimos.talentppe.MonolithApp;
import io.kimos.talentppe.domain.Benefit;
import io.kimos.talentppe.repository.BenefitRepository;
import io.kimos.talentppe.repository.search.BenefitSearchRepository;
import io.kimos.talentppe.service.BenefitService;
import io.kimos.talentppe.web.rest.errors.ExceptionTranslator;
import ma.glasnost.orika.MapperFacade;
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
 * Test class for the BenefitResource REST controller.
 *
 * @see BenefitResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MonolithApp.class)
public class BenefitResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NORMALIZED_NAME = "aaaaaaaaaa";
    private static final String UPDATED_NORMALIZED_NAME = "bbbbbbbbbb";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private BenefitRepository benefitRepository;

    @Autowired
    private BenefitService benefitService;

    @Autowired
    private MapperFacade orikaMapper;

    /**
     * This repository is mocked in the io.kimos.talentppe.repository.search test package.
     *
     * @see io.kimos.talentppe.repository.search.BenefitSearchRepositoryMockConfiguration
     */
    @Autowired
    private BenefitSearchRepository mockBenefitSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBenefitMockMvc;

    private Benefit benefit;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Benefit createEntity(EntityManager em) {
        Benefit benefit = new Benefit()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return benefit;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BenefitResource benefitResource = new BenefitResource(benefitService, orikaMapper);
        this.restBenefitMockMvc = MockMvcBuilders.standaloneSetup(benefitResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        benefit = createEntity(em);
    }

    @Test
    @Transactional
    public void createBenefit() throws Exception {
        int databaseSizeBeforeCreate = benefitRepository.findAll().size();

        // Create the Benefit
        restBenefitMockMvc.perform(post("/api/benefits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(benefit)))
            .andExpect(status().isCreated());

        // Validate the Benefit in the database
        List<Benefit> benefitList = benefitRepository.findAll();
        assertThat(benefitList).hasSize(databaseSizeBeforeCreate + 1);
        Benefit testBenefit = benefitList.get(benefitList.size() - 1);
        assertThat(testBenefit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBenefit.getNormalizedName()).isEqualTo(DEFAULT_NORMALIZED_NAME);
        assertThat(testBenefit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Benefit in Elasticsearch
        verify(mockBenefitSearchRepository, times(1)).save(testBenefit);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = benefitRepository.findAll().size();
        // set the field null
        benefit.setName(null);

        // Create the Benefit, which fails.

        restBenefitMockMvc.perform(post("/api/benefits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(benefit)))
            .andExpect(status().isBadRequest());

        List<Benefit> benefitList = benefitRepository.findAll();
        assertThat(benefitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBenefits() throws Exception {
        // Initialize the database
        benefitRepository.saveAndFlush(benefit);

        // Get all the benefitList
        restBenefitMockMvc.perform(get("/api/benefits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(benefit.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].normalizedName").value(hasItem(DEFAULT_NORMALIZED_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getBenefit() throws Exception {
        // Initialize the database
        benefitRepository.saveAndFlush(benefit);

        // Get the benefit
        restBenefitMockMvc.perform(get("/api/benefits/{id}", benefit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(benefit.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.normalizedName").value(DEFAULT_NORMALIZED_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBenefit() throws Exception {
        // Get the benefit
        restBenefitMockMvc.perform(get("/api/benefits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBenefit() throws Exception {
        // Initialize the database
        benefitService.save(benefit);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockBenefitSearchRepository);

        int databaseSizeBeforeUpdate = benefitRepository.findAll().size();

        // Update the benefit
        Benefit updatedBenefit = benefitRepository.findById(benefit.getId()).get();
        // Disconnect from session so that the updates on updatedBenefit are not directly saved in db
        em.detach(updatedBenefit);
        updatedBenefit
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restBenefitMockMvc.perform(put("/api/benefits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBenefit)))
            .andExpect(status().isOk());

        // Validate the Benefit in the database
        List<Benefit> benefitList = benefitRepository.findAll();
        assertThat(benefitList).hasSize(databaseSizeBeforeUpdate);
        Benefit testBenefit = benefitList.get(benefitList.size() - 1);
        assertThat(testBenefit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBenefit.getNormalizedName()).isEqualTo(UPDATED_NORMALIZED_NAME);
        assertThat(testBenefit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Benefit in Elasticsearch
        verify(mockBenefitSearchRepository, times(1)).save(testBenefit);
    }

    @Test
    @Transactional
    public void updateNonExistingBenefit() throws Exception {
        int databaseSizeBeforeUpdate = benefitRepository.findAll().size();

        // Create the Benefit

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBenefitMockMvc.perform(put("/api/benefits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(benefit)))
            .andExpect(status().isBadRequest());

        // Validate the Benefit in the database
        List<Benefit> benefitList = benefitRepository.findAll();
        assertThat(benefitList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Benefit in Elasticsearch
        verify(mockBenefitSearchRepository, times(0)).save(benefit);
    }

    @Test
    @Transactional
    public void deleteBenefit() throws Exception {
        // Initialize the database
        benefitService.save(benefit);

        int databaseSizeBeforeDelete = benefitRepository.findAll().size();

        // Get the benefit
        restBenefitMockMvc.perform(delete("/api/benefits/{id}", benefit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Benefit> benefitList = benefitRepository.findAll();
        assertThat(benefitList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Benefit in Elasticsearch
        verify(mockBenefitSearchRepository, times(1)).deleteById(benefit.getId());
    }

    @Test
    @Transactional
    public void searchBenefit() throws Exception {
        // Initialize the database
        benefitService.save(benefit);
        when(mockBenefitSearchRepository.search(queryStringQuery("id:" + benefit.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(benefit), PageRequest.of(0, 1), 1));
        // Search the benefit
        restBenefitMockMvc.perform(get("/api/_search/benefits?query=id:" + benefit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(benefit.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].normalizedName").value(hasItem(DEFAULT_NORMALIZED_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Benefit.class);
        Benefit benefit1 = new Benefit();
        benefit1.setId(1L);
        Benefit benefit2 = new Benefit();
        benefit2.setId(benefit1.getId());
        assertThat(benefit1).isEqualTo(benefit2);
        benefit2.setId(2L);
        assertThat(benefit1).isNotEqualTo(benefit2);
        benefit1.setId(null);
        assertThat(benefit1).isNotEqualTo(benefit2);
    }
}
