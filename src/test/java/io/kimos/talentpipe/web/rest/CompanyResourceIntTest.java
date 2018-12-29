package io.kimos.talentpipe.web.rest;

import io.kimos.talentpipe.domain.Company;
import io.kimos.talentpipe.domain.User;
import io.kimos.talentpipe.domain.Sector;
import io.kimos.talentpipe.domain.City;
import io.kimos.talentpipe.domain.CompanyType;
import io.kimos.talentpipe.repository.CompanyRepository;
import io.kimos.talentpipe.repository.search.CompanySearchRepository;
import io.kimos.talentpipe.service.CompanyService;
import io.kimos.talentpipe.web.rest.errors.ExceptionTranslator;
import io.kimos.talentpipe.service.CompanyQueryService;

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
import javax.validation.ConstraintViolationException;
import java.time.Instant;
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
 * Test class for the CompanyResource REST controller.
 *
 * @see CompanyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MonolithApp.class)
public class CompanyResourceIntTest {

    private static final String DEFAULT_TAX_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TAX_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TAX_ID = "AAAAAAAAAA";
    private static final String UPDATED_TAX_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    private static final Integer DEFAULT_FLOOR = 1;
    private static final Integer UPDATED_FLOOR = 2;

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    private static final String DEFAULT_APARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_APARTMENT = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private CompanyService companyService;

    /**
     * This repository is mocked in the io.kimos.talentppe.repository.search test package.
     *
     * @see io.kimos.talentpipe.repository.search.CompanySearchRepositoryMockConfiguration
     */
    @Autowired
    private CompanySearchRepository mockCompanySearchRepository;

    @Autowired
    private CompanyQueryService companyQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private MapperFacade orikaMapper;

    private MockMvc restCompanyMockMvc;

    private Company company;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CompanyResource companyResource = new CompanyResource(companyService, companyQueryService, orikaMapper);
        this.restCompanyMockMvc = MockMvcBuilders.standaloneSetup(companyResource)
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
    public static Company createEntity(EntityManager em) {
        Company company = new Company()
            .taxName(DEFAULT_TAX_NAME)
            .taxId(DEFAULT_TAX_ID)
            .email(DEFAULT_EMAIL)
            .name(DEFAULT_NAME)
            .street(DEFAULT_STREET)
            .floor(DEFAULT_FLOOR)
            .number(DEFAULT_NUMBER)
            .apartment(DEFAULT_APARTMENT)
            .postalCode(DEFAULT_POSTAL_CODE)
            .phone(DEFAULT_PHONE)
            .contactName(DEFAULT_CONTACT_NAME);
        company.setContactLastName("Test");
        company.setLastUpdateDate(Instant.now());
        company.setCreationDate(Instant.now());
        company.setPhonePrefix("011");
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        company.setMainUser(user);
        // Add required entity
        Sector sector = SectorResourceIntTest.createEntity(em);
        em.persist(sector);
        em.flush();
        company.setSector(sector);
        // Add required entity
        City city = CityResourceIntTest.createEntity(em);
        em.persist(city);
        em.flush();
        company.setCity(city);
        // Add required entity
        CompanyType companyType = CompanyTypeResourceIntTest.createEntity(em);
        em.persist(companyType);
        em.flush();
        company.setCompanyType(companyType);
        return company;
    }

    @Before
    public void initTest() {
        company = createEntity(em);
    }

 /*   @Test
    @Transactional
    public void createCompany() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        CreateCompanyRequest request = new CreateCompanyRequest();
        request.setCompanyName(DEFAULT_NAME);
        request.setTaxId(DEFAULT_TAX_ID);
        request.setTaxName(DEFAULT_TAX_NAME);
        request.setEmail(DEFAULT_EMAIL);
        request.setPassword(DEFAULT_PASSWORD);
        request.setPhonePrefix(DEFAULT_PHONE);
        request.setPhoneNumber(DEFAULT_PHONE);
        request.setContactLastName(DEFAULT_CONTACT_NAME);
        request.setContactName(DEFAULT_CONTACT_NAME);
        request.setWantNewsLetter(true);
        request.setAcceptTermsOfService(true);
        // Create the Company
        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isCreated());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate + 1);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getTaxName()).isEqualTo(DEFAULT_TAX_NAME);
        assertThat(testCompany.getTaxId()).isEqualTo(DEFAULT_TAX_ID);
        assertThat(testCompany.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCompany.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCompany.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testCompany.getFloor()).isEqualTo(DEFAULT_FLOOR);
        assertThat(testCompany.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testCompany.getApartment()).isEqualTo(DEFAULT_APARTMENT);
        assertThat(testCompany.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testCompany.getPhoneNumber()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCompany.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);

        // Validate the Company in Elasticsearch
        verify(mockCompanySearchRepository, times(1)).save(testCompany);
    }
*/
    @Test(expected = ConstraintViolationException.class)
    @Transactional
    public void checkTaxNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setTaxName(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test(expected = ConstraintViolationException.class)
    @Transactional
    public void checkTaxIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setTaxId(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test(expected = ConstraintViolationException.class)
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setEmail(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStreetIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setStreet(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setNumber(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPostalCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setPostalCode(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test(expected = ConstraintViolationException.class)
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setPhoneNumber(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test(expected = ConstraintViolationException.class)
    @Transactional
    public void checkContactNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setContactName(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompanies() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList
        restCompanyMockMvc.perform(get("/api/companies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].taxName").value(hasItem(DEFAULT_TAX_NAME.toString())))
            .andExpect(jsonPath("$.[*].taxId").value(hasItem(DEFAULT_TAX_ID.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
            .andExpect(jsonPath("$.[*].floor").value(hasItem(DEFAULT_FLOOR)))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].apartment").value(hasItem(DEFAULT_APARTMENT.toString())))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get the company
        restCompanyMockMvc.perform(get("/api/companies/{id}", company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(company.getId().intValue()))
            .andExpect(jsonPath("$.taxName").value(DEFAULT_TAX_NAME.toString()))
            .andExpect(jsonPath("$.taxId").value(DEFAULT_TAX_ID.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET.toString()))
            .andExpect(jsonPath("$.floor").value(DEFAULT_FLOOR))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.apartment").value(DEFAULT_APARTMENT.toString()))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.contactName").value(DEFAULT_CONTACT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllCompaniesByTaxNameIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where taxName equals to DEFAULT_TAX_NAME
        defaultCompanyShouldBeFound("taxName.equals=" + DEFAULT_TAX_NAME);

        // Get all the companyList where taxName equals to UPDATED_TAX_NAME
        defaultCompanyShouldNotBeFound("taxName.equals=" + UPDATED_TAX_NAME);
    }

    @Test
    @Transactional
    public void getAllCompaniesByTaxNameIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where taxName in DEFAULT_TAX_NAME or UPDATED_TAX_NAME
        defaultCompanyShouldBeFound("taxName.in=" + DEFAULT_TAX_NAME + "," + UPDATED_TAX_NAME);

        // Get all the companyList where taxName equals to UPDATED_TAX_NAME
        defaultCompanyShouldNotBeFound("taxName.in=" + UPDATED_TAX_NAME);
    }

    @Test
    @Transactional
    public void getAllCompaniesByTaxNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where taxName is not null
        defaultCompanyShouldBeFound("taxName.specified=true");

        // Get all the companyList where taxName is null
        defaultCompanyShouldNotBeFound("taxName.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByTaxIdIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where taxId equals to DEFAULT_TAX_ID
        defaultCompanyShouldBeFound("taxId.equals=" + DEFAULT_TAX_ID);

        // Get all the companyList where taxId equals to UPDATED_TAX_ID
        defaultCompanyShouldNotBeFound("taxId.equals=" + UPDATED_TAX_ID);
    }

    @Test
    @Transactional
    public void getAllCompaniesByTaxIdIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where taxId in DEFAULT_TAX_ID or UPDATED_TAX_ID
        defaultCompanyShouldBeFound("taxId.in=" + DEFAULT_TAX_ID + "," + UPDATED_TAX_ID);

        // Get all the companyList where taxId equals to UPDATED_TAX_ID
        defaultCompanyShouldNotBeFound("taxId.in=" + UPDATED_TAX_ID);
    }

    @Test
    @Transactional
    public void getAllCompaniesByTaxIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where taxId is not null
        defaultCompanyShouldBeFound("taxId.specified=true");

        // Get all the companyList where taxId is null
        defaultCompanyShouldNotBeFound("taxId.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where email equals to DEFAULT_EMAIL
        defaultCompanyShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the companyList where email equals to UPDATED_EMAIL
        defaultCompanyShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCompaniesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultCompanyShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the companyList where email equals to UPDATED_EMAIL
        defaultCompanyShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCompaniesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where email is not null
        defaultCompanyShouldBeFound("email.specified=true");

        // Get all the companyList where email is null
        defaultCompanyShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where name equals to DEFAULT_NAME
        defaultCompanyShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the companyList where name equals to UPDATED_NAME
        defaultCompanyShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCompaniesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCompanyShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the companyList where name equals to UPDATED_NAME
        defaultCompanyShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCompaniesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where name is not null
        defaultCompanyShouldBeFound("name.specified=true");

        // Get all the companyList where name is null
        defaultCompanyShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByStreetIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where street equals to DEFAULT_STREET
        defaultCompanyShouldBeFound("street.equals=" + DEFAULT_STREET);

        // Get all the companyList where street equals to UPDATED_STREET
        defaultCompanyShouldNotBeFound("street.equals=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllCompaniesByStreetIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where street in DEFAULT_STREET or UPDATED_STREET
        defaultCompanyShouldBeFound("street.in=" + DEFAULT_STREET + "," + UPDATED_STREET);

        // Get all the companyList where street equals to UPDATED_STREET
        defaultCompanyShouldNotBeFound("street.in=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllCompaniesByStreetIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where street is not null
        defaultCompanyShouldBeFound("street.specified=true");

        // Get all the companyList where street is null
        defaultCompanyShouldNotBeFound("street.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByFloorIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where floor equals to DEFAULT_FLOOR
        defaultCompanyShouldBeFound("floor.equals=" + DEFAULT_FLOOR);

        // Get all the companyList where floor equals to UPDATED_FLOOR
        defaultCompanyShouldNotBeFound("floor.equals=" + UPDATED_FLOOR);
    }

    @Test
    @Transactional
    public void getAllCompaniesByFloorIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where floor in DEFAULT_FLOOR or UPDATED_FLOOR
        defaultCompanyShouldBeFound("floor.in=" + DEFAULT_FLOOR + "," + UPDATED_FLOOR);

        // Get all the companyList where floor equals to UPDATED_FLOOR
        defaultCompanyShouldNotBeFound("floor.in=" + UPDATED_FLOOR);
    }

    @Test
    @Transactional
    public void getAllCompaniesByFloorIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where floor is not null
        defaultCompanyShouldBeFound("floor.specified=true");

        // Get all the companyList where floor is null
        defaultCompanyShouldNotBeFound("floor.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByFloorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where floor greater than or equals to DEFAULT_FLOOR
        defaultCompanyShouldBeFound("floor.greaterOrEqualThan=" + DEFAULT_FLOOR);

        // Get all the companyList where floor greater than or equals to UPDATED_FLOOR
        defaultCompanyShouldNotBeFound("floor.greaterOrEqualThan=" + UPDATED_FLOOR);
    }

    @Test
    @Transactional
    public void getAllCompaniesByFloorIsLessThanSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where floor less than or equals to DEFAULT_FLOOR
        defaultCompanyShouldNotBeFound("floor.lessThan=" + DEFAULT_FLOOR);

        // Get all the companyList where floor less than or equals to UPDATED_FLOOR
        defaultCompanyShouldBeFound("floor.lessThan=" + UPDATED_FLOOR);
    }


    @Test
    @Transactional
    public void getAllCompaniesByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where number equals to DEFAULT_NUMBER
        defaultCompanyShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the companyList where number equals to UPDATED_NUMBER
        defaultCompanyShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCompaniesByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultCompanyShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the companyList where number equals to UPDATED_NUMBER
        defaultCompanyShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCompaniesByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where number is not null
        defaultCompanyShouldBeFound("number.specified=true");

        // Get all the companyList where number is null
        defaultCompanyShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where number greater than or equals to DEFAULT_NUMBER
        defaultCompanyShouldBeFound("number.greaterOrEqualThan=" + DEFAULT_NUMBER);

        // Get all the companyList where number greater than or equals to UPDATED_NUMBER
        defaultCompanyShouldNotBeFound("number.greaterOrEqualThan=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCompaniesByNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where number less than or equals to DEFAULT_NUMBER
        defaultCompanyShouldNotBeFound("number.lessThan=" + DEFAULT_NUMBER);

        // Get all the companyList where number less than or equals to UPDATED_NUMBER
        defaultCompanyShouldBeFound("number.lessThan=" + UPDATED_NUMBER);
    }


    @Test
    @Transactional
    public void getAllCompaniesByApartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where apartment equals to DEFAULT_APARTMENT
        defaultCompanyShouldBeFound("apartment.equals=" + DEFAULT_APARTMENT);

        // Get all the companyList where apartment equals to UPDATED_APARTMENT
        defaultCompanyShouldNotBeFound("apartment.equals=" + UPDATED_APARTMENT);
    }

    @Test
    @Transactional
    public void getAllCompaniesByApartmentIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where apartment in DEFAULT_APARTMENT or UPDATED_APARTMENT
        defaultCompanyShouldBeFound("apartment.in=" + DEFAULT_APARTMENT + "," + UPDATED_APARTMENT);

        // Get all the companyList where apartment equals to UPDATED_APARTMENT
        defaultCompanyShouldNotBeFound("apartment.in=" + UPDATED_APARTMENT);
    }

    @Test
    @Transactional
    public void getAllCompaniesByApartmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where apartment is not null
        defaultCompanyShouldBeFound("apartment.specified=true");

        // Get all the companyList where apartment is null
        defaultCompanyShouldNotBeFound("apartment.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where postalCode equals to DEFAULT_POSTAL_CODE
        defaultCompanyShouldBeFound("postalCode.equals=" + DEFAULT_POSTAL_CODE);

        // Get all the companyList where postalCode equals to UPDATED_POSTAL_CODE
        defaultCompanyShouldNotBeFound("postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where postalCode in DEFAULT_POSTAL_CODE or UPDATED_POSTAL_CODE
        defaultCompanyShouldBeFound("postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE);

        // Get all the companyList where postalCode equals to UPDATED_POSTAL_CODE
        defaultCompanyShouldNotBeFound("postalCode.in=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where postalCode is not null
        defaultCompanyShouldBeFound("postalCode.specified=true");

        // Get all the companyList where postalCode is null
        defaultCompanyShouldNotBeFound("postalCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where phone equals to DEFAULT_PHONE
        defaultCompanyShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the companyList where phone equals to UPDATED_PHONE
        defaultCompanyShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultCompanyShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the companyList where phone equals to UPDATED_PHONE
        defaultCompanyShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where phone is not null
        defaultCompanyShouldBeFound("phone.specified=true");

        // Get all the companyList where phone is null
        defaultCompanyShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByContactNameIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where contactName equals to DEFAULT_CONTACT_NAME
        defaultCompanyShouldBeFound("contactName.equals=" + DEFAULT_CONTACT_NAME);

        // Get all the companyList where contactName equals to UPDATED_CONTACT_NAME
        defaultCompanyShouldNotBeFound("contactName.equals=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    public void getAllCompaniesByContactNameIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where contactName in DEFAULT_CONTACT_NAME or UPDATED_CONTACT_NAME
        defaultCompanyShouldBeFound("contactName.in=" + DEFAULT_CONTACT_NAME + "," + UPDATED_CONTACT_NAME);

        // Get all the companyList where contactName equals to UPDATED_CONTACT_NAME
        defaultCompanyShouldNotBeFound("contactName.in=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    public void getAllCompaniesByContactNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where contactName is not null
        defaultCompanyShouldBeFound("contactName.specified=true");

        // Get all the companyList where contactName is null
        defaultCompanyShouldNotBeFound("contactName.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByMainUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User mainUser = UserResourceIntTest.createEntity(em);
        em.persist(mainUser);
        em.flush();
        company.setMainUser(mainUser);
        companyRepository.saveAndFlush(company);
        Long mainUserId = mainUser.getId();

        // Get all the companyList where mainUser equals to mainUserId
        defaultCompanyShouldBeFound("mainUserId.equals=" + mainUserId);

        // Get all the companyList where mainUser equals to mainUserId + 1
        defaultCompanyShouldNotBeFound("mainUserId.equals=" + (mainUserId + 1));
    }


    @Test
    @Transactional
    public void getAllCompaniesBySectorIsEqualToSomething() throws Exception {
        // Initialize the database
        Sector sector = SectorResourceIntTest.createEntity(em);
        em.persist(sector);
        em.flush();
        company.setSector(sector);
        companyRepository.saveAndFlush(company);
        Long sectorId = sector.getId();

        // Get all the companyList where sector equals to sectorId
        defaultCompanyShouldBeFound("sectorId.equals=" + sectorId);

        // Get all the companyList where sector equals to sectorId + 1
        defaultCompanyShouldNotBeFound("sectorId.equals=" + (sectorId + 1));
    }


    @Test
    @Transactional
    public void getAllCompaniesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        City city = CityResourceIntTest.createEntity(em);
        em.persist(city);
        em.flush();
        company.setCity(city);
        companyRepository.saveAndFlush(company);
        Long cityId = city.getId();

        // Get all the companyList where city equals to cityId
        defaultCompanyShouldBeFound("cityId.equals=" + cityId);

        // Get all the companyList where city equals to cityId + 1
        defaultCompanyShouldNotBeFound("cityId.equals=" + (cityId + 1));
    }


    @Test
    @Transactional
    public void getAllCompaniesByCompanyTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        CompanyType companyType = CompanyTypeResourceIntTest.createEntity(em);
        em.persist(companyType);
        em.flush();
        company.setCompanyType(companyType);
        companyRepository.saveAndFlush(company);
        Long companyTypeId = companyType.getId();

        // Get all the companyList where companyType equals to companyTypeId
        defaultCompanyShouldBeFound("companyTypeId.equals=" + companyTypeId);

        // Get all the companyList where companyType equals to companyTypeId + 1
        defaultCompanyShouldNotBeFound("companyTypeId.equals=" + (companyTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCompanyShouldBeFound(String filter) throws Exception {
        restCompanyMockMvc.perform(get("/api/companies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].taxName").value(hasItem(DEFAULT_TAX_NAME.toString())))
            .andExpect(jsonPath("$.[*].taxId").value(hasItem(DEFAULT_TAX_ID.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
            .andExpect(jsonPath("$.[*].floor").value(hasItem(DEFAULT_FLOOR)))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].apartment").value(hasItem(DEFAULT_APARTMENT.toString())))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCompanyShouldNotBeFound(String filter) throws Exception {
        restCompanyMockMvc.perform(get("/api/companies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingCompany() throws Exception {
        // Get the company
        restCompanyMockMvc.perform(get("/api/companies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

  /*  @Test
    @Transactional
    public void updateCompany() throws Exception {
        // Initialize the database
        companyService.save(company);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCompanySearchRepository);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company
        Company updatedCompany = companyRepository.findById(company.getId()).get();
        // Disconnect from session so that the updates on updatedCompany are not directly saved in db
        em.detach(updatedCompany);
        updatedCompany
            .taxName(UPDATED_TAX_NAME)
            .taxId(UPDATED_TAX_ID)
            .email(UPDATED_EMAIL)
            .name(UPDATED_NAME)
            .street(UPDATED_STREET)
            .floor(UPDATED_FLOOR)
            .number(UPDATED_NUMBER)
            .apartment(UPDATED_APARTMENT)
            .postalCode(UPDATED_POSTAL_CODE)
            .phone(UPDATED_PHONE)
            .contactName(UPDATED_CONTACT_NAME);

        restCompanyMockMvc.perform(put("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCompany)))
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getTaxName()).isEqualTo(UPDATED_TAX_NAME);
        assertThat(testCompany.getTaxId()).isEqualTo(UPDATED_TAX_ID);
        assertThat(testCompany.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCompany.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompany.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testCompany.getFloor()).isEqualTo(UPDATED_FLOOR);
        assertThat(testCompany.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testCompany.getApartment()).isEqualTo(UPDATED_APARTMENT);
        assertThat(testCompany.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testCompany.getPhoneNumber()).isEqualTo(UPDATED_PHONE);
        assertThat(testCompany.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);

        // Validate the Company in Elasticsearch
        verify(mockCompanySearchRepository, times(1)).save(testCompany);
    }*/

    @Test
    @Transactional
    public void updateNonExistingCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Create the Company

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc.perform(put("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Company in Elasticsearch
        verify(mockCompanySearchRepository, times(0)).save(company);
    }

    @Test
    @Transactional
    public void deleteCompany() throws Exception {
        // Initialize the database
        companyService.save(company);

        int databaseSizeBeforeDelete = companyRepository.findAll().size();

        // Get the company
        restCompanyMockMvc.perform(delete("/api/companies/{id}", company.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Company in Elasticsearch
        verify(mockCompanySearchRepository, times(1)).deleteById(company.getId());
    }

    @Test
    @Transactional
    public void searchCompany() throws Exception {
        // Initialize the database
        companyService.save(company);
        when(mockCompanySearchRepository.search(queryStringQuery("id:" + company.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(company), PageRequest.of(0, 1), 1));
        // Search the company
        restCompanyMockMvc.perform(get("/api/_search/companies?query=id:" + company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].taxName").value(hasItem(DEFAULT_TAX_NAME.toString())))
            .andExpect(jsonPath("$.[*].taxId").value(hasItem(DEFAULT_TAX_ID.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
            .andExpect(jsonPath("$.[*].floor").value(hasItem(DEFAULT_FLOOR)))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].apartment").value(hasItem(DEFAULT_APARTMENT.toString())))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Company.class);
        Company company1 = new Company();
        company1.setId(1L);
        Company company2 = new Company();
        company2.setId(company1.getId());
        assertThat(company1).isEqualTo(company2);
        company2.setId(2L);
        assertThat(company1).isNotEqualTo(company2);
        company1.setId(null);
        assertThat(company1).isNotEqualTo(company2);
    }
}
