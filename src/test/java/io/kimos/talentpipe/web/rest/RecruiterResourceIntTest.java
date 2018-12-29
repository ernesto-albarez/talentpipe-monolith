package io.kimos.talentpipe.web.rest;

import io.kimos.talentpipe.domain.Recruiter;
import io.kimos.talentpipe.domain.City;
import io.kimos.talentpipe.domain.Sector;
import io.kimos.talentpipe.domain.User;
import io.kimos.talentpipe.repository.RecruiterRepository;
import io.kimos.talentpipe.repository.search.RecruiterSearchRepository;
import io.kimos.talentpipe.service.RecruiterService;
import io.kimos.talentpipe.web.rest.errors.ExceptionTranslator;
import io.kimos.talentpipe.service.RecruiterQueryService;

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


import static io.kimos.talentpipe.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RecruiterResource REST controller.
 *
 * @see RecruiterResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MonolithApp.class)
public class RecruiterResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TAX_ID = "AAAAAAAAAA";
    private static final String UPDATED_TAX_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    private static final Integer DEFAULT_FLOOR = 0;
    private static final Integer UPDATED_FLOOR = 1;

    private static final String DEFAULT_APARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_APARTMENT = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_PREFIX = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_PREFIX = "AAAAAAAAAA";

    @Autowired
    private RecruiterRepository recruiterRepository;
    
    @Autowired
    private RecruiterService recruiterService;

    @Autowired
    private MapperFacade orikaMapper;

    /**
     * This repository is mocked in the io.kimos.talentppe.repository.search test package.
     *
     * @see io.kimos.talentpipe.repository.search.RecruiterSearchRepositoryMockConfiguration
     */
    @Autowired
    private RecruiterSearchRepository mockRecruiterSearchRepository;

    @Autowired
    private RecruiterQueryService recruiterQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRecruiterMockMvc;

    private Recruiter recruiter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RecruiterResource recruiterResource = new RecruiterResource(recruiterService, recruiterQueryService, orikaMapper);
        this.restRecruiterMockMvc = MockMvcBuilders.standaloneSetup(recruiterResource)
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
    public static Recruiter createEntity(EntityManager em) {
        Recruiter recruiter = new Recruiter()
            .name(DEFAULT_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .taxId(DEFAULT_TAX_ID)
            .phone(DEFAULT_PHONE)
            .street(DEFAULT_STREET)
            .number(DEFAULT_NUMBER)
            .floor(DEFAULT_FLOOR)
            .phonePrefix(DEFAULT_PHONE_PREFIX)
            .apartment(DEFAULT_APARTMENT);
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        recruiter.setUser(user);
        // Add required entity
        City city = CityResourceIntTest.createEntity(em);
        em.persist(city);
        em.flush();
        recruiter.setCity(city);
        return recruiter;
    }

    @Before
    public void initTest() {
        recruiter = createEntity(em);
    }

    @Test
    @Transactional
    public void createRecruiter() throws Exception {
        int databaseSizeBeforeCreate = recruiterRepository.findAll().size();

        // Create the Recruiter
        restRecruiterMockMvc.perform(post("/api/recruiters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recruiter)))
            .andExpect(status().isCreated());

        // Validate the Recruiter in the database
        List<Recruiter> recruiterList = recruiterRepository.findAll();
        assertThat(recruiterList).hasSize(databaseSizeBeforeCreate + 1);
        Recruiter testRecruiter = recruiterList.get(recruiterList.size() - 1);
        assertThat(testRecruiter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRecruiter.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testRecruiter.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testRecruiter.getTaxId()).isEqualTo(DEFAULT_TAX_ID);
        assertThat(testRecruiter.getPhoneNumber()).isEqualTo(DEFAULT_PHONE);
        assertThat(testRecruiter.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testRecruiter.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testRecruiter.getFloor()).isEqualTo(DEFAULT_FLOOR);
        assertThat(testRecruiter.getApartment()).isEqualTo(DEFAULT_APARTMENT);

        // Validate the Recruiter in Elasticsearch
        verify(mockRecruiterSearchRepository, times(1)).save(testRecruiter);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = recruiterRepository.findAll().size();
        // set the field null
        recruiter.setName(null);

        // Create the Recruiter, which fails.

        restRecruiterMockMvc.perform(post("/api/recruiters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recruiter)))
            .andExpect(status().isBadRequest());

        List<Recruiter> recruiterList = recruiterRepository.findAll();
        assertThat(recruiterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = recruiterRepository.findAll().size();
        // set the field null
        recruiter.setLastName(null);

        // Create the Recruiter, which fails.

        restRecruiterMockMvc.perform(post("/api/recruiters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recruiter)))
            .andExpect(status().isBadRequest());

        List<Recruiter> recruiterList = recruiterRepository.findAll();
        assertThat(recruiterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = recruiterRepository.findAll().size();
        // set the field null
        recruiter.setEmail(null);

        // Create the Recruiter, which fails.

        restRecruiterMockMvc.perform(post("/api/recruiters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recruiter)))
            .andExpect(status().isBadRequest());

        List<Recruiter> recruiterList = recruiterRepository.findAll();
        assertThat(recruiterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTaxIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = recruiterRepository.findAll().size();
        // set the field null
        recruiter.setTaxId(null);

        // Create the Recruiter, which fails.

        restRecruiterMockMvc.perform(post("/api/recruiters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recruiter)))
            .andExpect(status().isBadRequest());

        List<Recruiter> recruiterList = recruiterRepository.findAll();
        assertThat(recruiterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = recruiterRepository.findAll().size();
        // set the field null
        recruiter.setPhoneNumber(null);

        // Create the Recruiter, which fails.

        restRecruiterMockMvc.perform(post("/api/recruiters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recruiter)))
            .andExpect(status().isBadRequest());

        List<Recruiter> recruiterList = recruiterRepository.findAll();
        assertThat(recruiterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRecruiters() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList
        restRecruiterMockMvc.perform(get("/api/recruiters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recruiter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].taxId").value(hasItem(DEFAULT_TAX_ID.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].floor").value(hasItem(DEFAULT_FLOOR)))
            .andExpect(jsonPath("$.[*].apartment").value(hasItem(DEFAULT_APARTMENT.toString())));
    }
    
    @Test
    @Transactional
    public void getRecruiter() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get the recruiter
        restRecruiterMockMvc.perform(get("/api/recruiters/{id}", recruiter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(recruiter.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.taxId").value(DEFAULT_TAX_ID.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.floor").value(DEFAULT_FLOOR))
            .andExpect(jsonPath("$.apartment").value(DEFAULT_APARTMENT.toString()));
    }

    @Test
    @Transactional
    public void getAllRecruitersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where name equals to DEFAULT_NAME
        defaultRecruiterShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the recruiterList where name equals to UPDATED_NAME
        defaultRecruiterShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRecruitersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRecruiterShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the recruiterList where name equals to UPDATED_NAME
        defaultRecruiterShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRecruitersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where name is not null
        defaultRecruiterShouldBeFound("name.specified=true");

        // Get all the recruiterList where name is null
        defaultRecruiterShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecruitersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where lastName equals to DEFAULT_LAST_NAME
        defaultRecruiterShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the recruiterList where lastName equals to UPDATED_LAST_NAME
        defaultRecruiterShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllRecruitersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultRecruiterShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the recruiterList where lastName equals to UPDATED_LAST_NAME
        defaultRecruiterShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllRecruitersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where lastName is not null
        defaultRecruiterShouldBeFound("lastName.specified=true");

        // Get all the recruiterList where lastName is null
        defaultRecruiterShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecruitersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where email equals to DEFAULT_EMAIL
        defaultRecruiterShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the recruiterList where email equals to UPDATED_EMAIL
        defaultRecruiterShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllRecruitersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultRecruiterShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the recruiterList where email equals to UPDATED_EMAIL
        defaultRecruiterShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllRecruitersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where email is not null
        defaultRecruiterShouldBeFound("email.specified=true");

        // Get all the recruiterList where email is null
        defaultRecruiterShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecruitersByTaxIdIsEqualToSomething() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where taxId equals to DEFAULT_TAX_ID
        defaultRecruiterShouldBeFound("taxId.equals=" + DEFAULT_TAX_ID);

        // Get all the recruiterList where taxId equals to UPDATED_TAX_ID
        defaultRecruiterShouldNotBeFound("taxId.equals=" + UPDATED_TAX_ID);
    }

    @Test
    @Transactional
    public void getAllRecruitersByTaxIdIsInShouldWork() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where taxId in DEFAULT_TAX_ID or UPDATED_TAX_ID
        defaultRecruiterShouldBeFound("taxId.in=" + DEFAULT_TAX_ID + "," + UPDATED_TAX_ID);

        // Get all the recruiterList where taxId equals to UPDATED_TAX_ID
        defaultRecruiterShouldNotBeFound("taxId.in=" + UPDATED_TAX_ID);
    }

    @Test
    @Transactional
    public void getAllRecruitersByTaxIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where taxId is not null
        defaultRecruiterShouldBeFound("taxId.specified=true");

        // Get all the recruiterList where taxId is null
        defaultRecruiterShouldNotBeFound("taxId.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecruitersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where phone equals to DEFAULT_PHONE
        defaultRecruiterShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the recruiterList where phone equals to UPDATED_PHONE
        defaultRecruiterShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllRecruitersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultRecruiterShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the recruiterList where phone equals to UPDATED_PHONE
        defaultRecruiterShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllRecruitersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where phone is not null
        defaultRecruiterShouldBeFound("phone.specified=true");

        // Get all the recruiterList where phone is null
        defaultRecruiterShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecruitersByStreetIsEqualToSomething() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where street equals to DEFAULT_STREET
        defaultRecruiterShouldBeFound("street.equals=" + DEFAULT_STREET);

        // Get all the recruiterList where street equals to UPDATED_STREET
        defaultRecruiterShouldNotBeFound("street.equals=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllRecruitersByStreetIsInShouldWork() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where street in DEFAULT_STREET or UPDATED_STREET
        defaultRecruiterShouldBeFound("street.in=" + DEFAULT_STREET + "," + UPDATED_STREET);

        // Get all the recruiterList where street equals to UPDATED_STREET
        defaultRecruiterShouldNotBeFound("street.in=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllRecruitersByStreetIsNullOrNotNull() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where street is not null
        defaultRecruiterShouldBeFound("street.specified=true");

        // Get all the recruiterList where street is null
        defaultRecruiterShouldNotBeFound("street.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecruitersByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where number equals to DEFAULT_NUMBER
        defaultRecruiterShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the recruiterList where number equals to UPDATED_NUMBER
        defaultRecruiterShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllRecruitersByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultRecruiterShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the recruiterList where number equals to UPDATED_NUMBER
        defaultRecruiterShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllRecruitersByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where number is not null
        defaultRecruiterShouldBeFound("number.specified=true");

        // Get all the recruiterList where number is null
        defaultRecruiterShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecruitersByNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where number greater than or equals to DEFAULT_NUMBER
        defaultRecruiterShouldBeFound("number.greaterOrEqualThan=" + DEFAULT_NUMBER);

        // Get all the recruiterList where number greater than or equals to UPDATED_NUMBER
        defaultRecruiterShouldNotBeFound("number.greaterOrEqualThan=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllRecruitersByNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where number less than or equals to DEFAULT_NUMBER
        defaultRecruiterShouldNotBeFound("number.lessThan=" + DEFAULT_NUMBER);

        // Get all the recruiterList where number less than or equals to UPDATED_NUMBER
        defaultRecruiterShouldBeFound("number.lessThan=" + UPDATED_NUMBER);
    }


    @Test
    @Transactional
    public void getAllRecruitersByFloorIsEqualToSomething() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where floor equals to DEFAULT_FLOOR
        defaultRecruiterShouldBeFound("floor.equals=" + DEFAULT_FLOOR);

        // Get all the recruiterList where floor equals to UPDATED_FLOOR
        defaultRecruiterShouldNotBeFound("floor.equals=" + UPDATED_FLOOR);
    }

    @Test
    @Transactional
    public void getAllRecruitersByFloorIsInShouldWork() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where floor in DEFAULT_FLOOR or UPDATED_FLOOR
        defaultRecruiterShouldBeFound("floor.in=" + DEFAULT_FLOOR + "," + UPDATED_FLOOR);

        // Get all the recruiterList where floor equals to UPDATED_FLOOR
        defaultRecruiterShouldNotBeFound("floor.in=" + UPDATED_FLOOR);
    }

    @Test
    @Transactional
    public void getAllRecruitersByFloorIsNullOrNotNull() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where floor is not null
        defaultRecruiterShouldBeFound("floor.specified=true");

        // Get all the recruiterList where floor is null
        defaultRecruiterShouldNotBeFound("floor.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecruitersByFloorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where floor greater than or equals to DEFAULT_FLOOR
        defaultRecruiterShouldBeFound("floor.greaterOrEqualThan=" + DEFAULT_FLOOR);

        // Get all the recruiterList where floor greater than or equals to (DEFAULT_FLOOR + 1)
        defaultRecruiterShouldNotBeFound("floor.greaterOrEqualThan=" + (DEFAULT_FLOOR + 1));
    }

    @Test
    @Transactional
    public void getAllRecruitersByFloorIsLessThanSomething() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where floor less than or equals to DEFAULT_FLOOR
        defaultRecruiterShouldNotBeFound("floor.lessThan=" + DEFAULT_FLOOR);

        // Get all the recruiterList where floor less than or equals to (DEFAULT_FLOOR + 1)
        defaultRecruiterShouldBeFound("floor.lessThan=" + (DEFAULT_FLOOR + 1));
    }


    @Test
    @Transactional
    public void getAllRecruitersByApartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where apartment equals to DEFAULT_APARTMENT
        defaultRecruiterShouldBeFound("apartment.equals=" + DEFAULT_APARTMENT);

        // Get all the recruiterList where apartment equals to UPDATED_APARTMENT
        defaultRecruiterShouldNotBeFound("apartment.equals=" + UPDATED_APARTMENT);
    }

    @Test
    @Transactional
    public void getAllRecruitersByApartmentIsInShouldWork() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where apartment in DEFAULT_APARTMENT or UPDATED_APARTMENT
        defaultRecruiterShouldBeFound("apartment.in=" + DEFAULT_APARTMENT + "," + UPDATED_APARTMENT);

        // Get all the recruiterList where apartment equals to UPDATED_APARTMENT
        defaultRecruiterShouldNotBeFound("apartment.in=" + UPDATED_APARTMENT);
    }

    @Test
    @Transactional
    public void getAllRecruitersByApartmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList where apartment is not null
        defaultRecruiterShouldBeFound("apartment.specified=true");

        // Get all the recruiterList where apartment is null
        defaultRecruiterShouldNotBeFound("apartment.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecruitersByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        City city = CityResourceIntTest.createEntity(em);
        em.persist(city);
        em.flush();
        recruiter.setCity(city);
        recruiterRepository.saveAndFlush(recruiter);
        Long cityId = city.getId();

        // Get all the recruiterList where city equals to cityId
        defaultRecruiterShouldBeFound("cityId.equals=" + cityId);

        // Get all the recruiterList where city equals to cityId + 1
        defaultRecruiterShouldNotBeFound("cityId.equals=" + (cityId + 1));
    }


    @Test
    @Transactional
    public void getAllRecruitersBySectorIsEqualToSomething() throws Exception {
        // Initialize the database
        Sector sector = SectorResourceIntTest.createEntity(em);
        em.persist(sector);
        em.flush();
        recruiter.setSector(sector);
        recruiterRepository.saveAndFlush(recruiter);
        Long sectorId = sector.getId();

        // Get all the recruiterList where sector equals to sectorId
        defaultRecruiterShouldBeFound("sectorId.equals=" + sectorId);

        // Get all the recruiterList where sector equals to sectorId + 1
        defaultRecruiterShouldNotBeFound("sectorId.equals=" + (sectorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultRecruiterShouldBeFound(String filter) throws Exception {
        restRecruiterMockMvc.perform(get("/api/recruiters?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recruiter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].taxId").value(hasItem(DEFAULT_TAX_ID.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].floor").value(hasItem(DEFAULT_FLOOR)))
            .andExpect(jsonPath("$.[*].apartment").value(hasItem(DEFAULT_APARTMENT.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultRecruiterShouldNotBeFound(String filter) throws Exception {
        restRecruiterMockMvc.perform(get("/api/recruiters?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingRecruiter() throws Exception {
        // Get the recruiter
        restRecruiterMockMvc.perform(get("/api/recruiters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecruiter() throws Exception {
        // Initialize the database
        recruiterService.save(recruiter);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockRecruiterSearchRepository);

        int databaseSizeBeforeUpdate = recruiterRepository.findAll().size();

        // Update the recruiter
        Recruiter updatedRecruiter = recruiterRepository.findById(recruiter.getId()).get();
        // Disconnect from session so that the updates on updatedRecruiter are not directly saved in db
        em.detach(updatedRecruiter);
        updatedRecruiter
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .taxId(UPDATED_TAX_ID)
            .phone(UPDATED_PHONE)
            .street(UPDATED_STREET)
            .number(UPDATED_NUMBER)
            .floor(UPDATED_FLOOR)
            .apartment(UPDATED_APARTMENT);

        restRecruiterMockMvc.perform(put("/api/recruiters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRecruiter)))
            .andExpect(status().isOk());

        // Validate the Recruiter in the database
        List<Recruiter> recruiterList = recruiterRepository.findAll();
        assertThat(recruiterList).hasSize(databaseSizeBeforeUpdate);
        Recruiter testRecruiter = recruiterList.get(recruiterList.size() - 1);
        assertThat(testRecruiter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRecruiter.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testRecruiter.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testRecruiter.getTaxId()).isEqualTo(UPDATED_TAX_ID);
        assertThat(testRecruiter.getPhoneNumber()).isEqualTo(UPDATED_PHONE);
        assertThat(testRecruiter.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testRecruiter.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testRecruiter.getFloor()).isEqualTo(UPDATED_FLOOR);
        assertThat(testRecruiter.getApartment()).isEqualTo(UPDATED_APARTMENT);

        // Validate the Recruiter in Elasticsearch
        verify(mockRecruiterSearchRepository, times(1)).save(testRecruiter);
    }

    @Test
    @Transactional
    public void updateNonExistingRecruiter() throws Exception {
        int databaseSizeBeforeUpdate = recruiterRepository.findAll().size();

        // Create the Recruiter

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecruiterMockMvc.perform(put("/api/recruiters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recruiter)))
            .andExpect(status().isBadRequest());

        // Validate the Recruiter in the database
        List<Recruiter> recruiterList = recruiterRepository.findAll();
        assertThat(recruiterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Recruiter in Elasticsearch
        verify(mockRecruiterSearchRepository, times(0)).save(recruiter);
    }

    @Test
    @Transactional
    public void deleteRecruiter() throws Exception {
        // Initialize the database
        recruiterService.save(recruiter);

        int databaseSizeBeforeDelete = recruiterRepository.findAll().size();

        // Get the recruiter
        restRecruiterMockMvc.perform(delete("/api/recruiters/{id}", recruiter.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Recruiter> recruiterList = recruiterRepository.findAll();
        assertThat(recruiterList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Recruiter in Elasticsearch
        verify(mockRecruiterSearchRepository, times(1)).deleteById(recruiter.getId());
    }

    @Test
    @Transactional
    public void searchRecruiter() throws Exception {
        // Initialize the database
        recruiterService.save(recruiter);
        when(mockRecruiterSearchRepository.search(queryStringQuery("id:" + recruiter.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(recruiter), PageRequest.of(0, 1), 1));
        // Search the recruiter
        restRecruiterMockMvc.perform(get("/api/_search/recruiters?query=id:" + recruiter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recruiter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].taxId").value(hasItem(DEFAULT_TAX_ID.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].floor").value(hasItem(DEFAULT_FLOOR)))
            .andExpect(jsonPath("$.[*].apartment").value(hasItem(DEFAULT_APARTMENT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Recruiter.class);
        Recruiter recruiter1 = new Recruiter();
        recruiter1.setId(1L);
        Recruiter recruiter2 = new Recruiter();
        recruiter2.setId(recruiter1.getId());
        assertThat(recruiter1).isEqualTo(recruiter2);
        recruiter2.setId(2L);
        assertThat(recruiter1).isNotEqualTo(recruiter2);
        recruiter1.setId(null);
        assertThat(recruiter1).isNotEqualTo(recruiter2);
    }
}
