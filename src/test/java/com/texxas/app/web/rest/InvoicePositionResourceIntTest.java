package com.texxas.app.web.rest;

import com.texxas.app.AppApp;

import com.texxas.app.domain.InvoicePosition;
import com.texxas.app.domain.Invoice;
import com.texxas.app.repository.InvoicePositionRepository;
import com.texxas.app.repository.search.InvoicePositionSearchRepository;
import com.texxas.app.web.rest.errors.ExceptionTranslator;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InvoicePositionResource REST controller.
 *
 * @see InvoicePositionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApp.class)
public class InvoicePositionResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TAX = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAX = new BigDecimal(2);

    @Autowired
    private InvoicePositionRepository invoicePositionRepository;

    @Autowired
    private InvoicePositionSearchRepository invoicePositionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInvoicePositionMockMvc;

    private InvoicePosition invoicePosition;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InvoicePositionResource invoicePositionResource = new InvoicePositionResource(invoicePositionRepository, invoicePositionSearchRepository);
        this.restInvoicePositionMockMvc = MockMvcBuilders.standaloneSetup(invoicePositionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoicePosition createEntity(EntityManager em) {
        InvoicePosition invoicePosition = new InvoicePosition()
            .name(DEFAULT_NAME)
            .value(DEFAULT_VALUE)
            .tax(DEFAULT_TAX);
        // Add required entity
        Invoice invoice = InvoiceResourceIntTest.createEntity(em);
        em.persist(invoice);
        em.flush();
        invoicePosition.setInvoice(invoice);
        return invoicePosition;
    }

    @Before
    public void initTest() {
        invoicePositionSearchRepository.deleteAll();
        invoicePosition = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoicePosition() throws Exception {
        int databaseSizeBeforeCreate = invoicePositionRepository.findAll().size();

        // Create the InvoicePosition
        restInvoicePositionMockMvc.perform(post("/api/invoice-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoicePosition)))
            .andExpect(status().isCreated());

        // Validate the InvoicePosition in the database
        List<InvoicePosition> invoicePositionList = invoicePositionRepository.findAll();
        assertThat(invoicePositionList).hasSize(databaseSizeBeforeCreate + 1);
        InvoicePosition testInvoicePosition = invoicePositionList.get(invoicePositionList.size() - 1);
        assertThat(testInvoicePosition.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInvoicePosition.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testInvoicePosition.getTax()).isEqualTo(DEFAULT_TAX);

        // Validate the InvoicePosition in Elasticsearch
        InvoicePosition invoicePositionEs = invoicePositionSearchRepository.findOne(testInvoicePosition.getId());
        assertThat(invoicePositionEs).isEqualToComparingFieldByField(testInvoicePosition);
    }

    @Test
    @Transactional
    public void createInvoicePositionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoicePositionRepository.findAll().size();

        // Create the InvoicePosition with an existing ID
        invoicePosition.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoicePositionMockMvc.perform(post("/api/invoice-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoicePosition)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<InvoicePosition> invoicePositionList = invoicePositionRepository.findAll();
        assertThat(invoicePositionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoicePositionRepository.findAll().size();
        // set the field null
        invoicePosition.setName(null);

        // Create the InvoicePosition, which fails.

        restInvoicePositionMockMvc.perform(post("/api/invoice-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoicePosition)))
            .andExpect(status().isBadRequest());

        List<InvoicePosition> invoicePositionList = invoicePositionRepository.findAll();
        assertThat(invoicePositionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTaxIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoicePositionRepository.findAll().size();
        // set the field null
        invoicePosition.setTax(null);

        // Create the InvoicePosition, which fails.

        restInvoicePositionMockMvc.perform(post("/api/invoice-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoicePosition)))
            .andExpect(status().isBadRequest());

        List<InvoicePosition> invoicePositionList = invoicePositionRepository.findAll();
        assertThat(invoicePositionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvoicePositions() throws Exception {
        // Initialize the database
        invoicePositionRepository.saveAndFlush(invoicePosition);

        // Get all the invoicePositionList
        restInvoicePositionMockMvc.perform(get("/api/invoice-positions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoicePosition.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.intValue())));
    }

    @Test
    @Transactional
    public void getInvoicePosition() throws Exception {
        // Initialize the database
        invoicePositionRepository.saveAndFlush(invoicePosition);

        // Get the invoicePosition
        restInvoicePositionMockMvc.perform(get("/api/invoice-positions/{id}", invoicePosition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoicePosition.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.intValue()))
            .andExpect(jsonPath("$.tax").value(DEFAULT_TAX.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInvoicePosition() throws Exception {
        // Get the invoicePosition
        restInvoicePositionMockMvc.perform(get("/api/invoice-positions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoicePosition() throws Exception {
        // Initialize the database
        invoicePositionRepository.saveAndFlush(invoicePosition);
        invoicePositionSearchRepository.save(invoicePosition);
        int databaseSizeBeforeUpdate = invoicePositionRepository.findAll().size();

        // Update the invoicePosition
        InvoicePosition updatedInvoicePosition = invoicePositionRepository.findOne(invoicePosition.getId());
        updatedInvoicePosition
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .tax(UPDATED_TAX);

        restInvoicePositionMockMvc.perform(put("/api/invoice-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInvoicePosition)))
            .andExpect(status().isOk());

        // Validate the InvoicePosition in the database
        List<InvoicePosition> invoicePositionList = invoicePositionRepository.findAll();
        assertThat(invoicePositionList).hasSize(databaseSizeBeforeUpdate);
        InvoicePosition testInvoicePosition = invoicePositionList.get(invoicePositionList.size() - 1);
        assertThat(testInvoicePosition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInvoicePosition.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testInvoicePosition.getTax()).isEqualTo(UPDATED_TAX);

        // Validate the InvoicePosition in Elasticsearch
        InvoicePosition invoicePositionEs = invoicePositionSearchRepository.findOne(testInvoicePosition.getId());
        assertThat(invoicePositionEs).isEqualToComparingFieldByField(testInvoicePosition);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoicePosition() throws Exception {
        int databaseSizeBeforeUpdate = invoicePositionRepository.findAll().size();

        // Create the InvoicePosition

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInvoicePositionMockMvc.perform(put("/api/invoice-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoicePosition)))
            .andExpect(status().isCreated());

        // Validate the InvoicePosition in the database
        List<InvoicePosition> invoicePositionList = invoicePositionRepository.findAll();
        assertThat(invoicePositionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInvoicePosition() throws Exception {
        // Initialize the database
        invoicePositionRepository.saveAndFlush(invoicePosition);
        invoicePositionSearchRepository.save(invoicePosition);
        int databaseSizeBeforeDelete = invoicePositionRepository.findAll().size();

        // Get the invoicePosition
        restInvoicePositionMockMvc.perform(delete("/api/invoice-positions/{id}", invoicePosition.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean invoicePositionExistsInEs = invoicePositionSearchRepository.exists(invoicePosition.getId());
        assertThat(invoicePositionExistsInEs).isFalse();

        // Validate the database is empty
        List<InvoicePosition> invoicePositionList = invoicePositionRepository.findAll();
        assertThat(invoicePositionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInvoicePosition() throws Exception {
        // Initialize the database
        invoicePositionRepository.saveAndFlush(invoicePosition);
        invoicePositionSearchRepository.save(invoicePosition);

        // Search the invoicePosition
        restInvoicePositionMockMvc.perform(get("/api/_search/invoice-positions?query=id:" + invoicePosition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoicePosition.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoicePosition.class);
        InvoicePosition invoicePosition1 = new InvoicePosition();
        invoicePosition1.setId(1L);
        InvoicePosition invoicePosition2 = new InvoicePosition();
        invoicePosition2.setId(invoicePosition1.getId());
        assertThat(invoicePosition1).isEqualTo(invoicePosition2);
        invoicePosition2.setId(2L);
        assertThat(invoicePosition1).isNotEqualTo(invoicePosition2);
        invoicePosition1.setId(null);
        assertThat(invoicePosition1).isNotEqualTo(invoicePosition2);
    }
}
