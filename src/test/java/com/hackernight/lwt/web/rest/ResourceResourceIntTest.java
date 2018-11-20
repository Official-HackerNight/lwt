package com.hackernight.lwt.web.rest;

import com.hackernight.lwt.LwtApp;

import com.hackernight.lwt.domain.Resource;
import com.hackernight.lwt.repository.ResourceRepository;
import com.hackernight.lwt.web.rest.errors.ExceptionTranslator;

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
import java.util.List;


import static com.hackernight.lwt.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ResourceResource REST controller.
 *
 * @see ResourceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LwtApp.class)
public class ResourceResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restResourceMockMvc;

    private Resource resource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ResourceResource resourceResource = new ResourceResource(resourceRepository);
        this.restResourceMockMvc = MockMvcBuilders.standaloneSetup(resourceResource)
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
    public static Resource createEntity(EntityManager em) {
        Resource resource = new Resource()
            .name(DEFAULT_NAME)
            .url(DEFAULT_URL)
            .description(DEFAULT_DESCRIPTION);
        return resource;
    }

    @Before
    public void initTest() {
        resource = createEntity(em);
    }

    @Test
    @Transactional
    public void createResource() throws Exception {
        int databaseSizeBeforeCreate = resourceRepository.findAll().size();

        // Create the Resource
        restResourceMockMvc.perform(post("/api/resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resource)))
            .andExpect(status().isCreated());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeCreate + 1);
        Resource testResource = resourceList.get(resourceList.size() - 1);
        assertThat(testResource.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testResource.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testResource.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createResourceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = resourceRepository.findAll().size();

        // Create the Resource with an existing ID
        resource.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResourceMockMvc.perform(post("/api/resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resource)))
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourceRepository.findAll().size();
        // set the field null
        resource.setName(null);

        // Create the Resource, which fails.

        restResourceMockMvc.perform(post("/api/resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resource)))
            .andExpect(status().isBadRequest());

        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourceRepository.findAll().size();
        // set the field null
        resource.setUrl(null);

        // Create the Resource, which fails.

        restResourceMockMvc.perform(post("/api/resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resource)))
            .andExpect(status().isBadRequest());

        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourceRepository.findAll().size();
        // set the field null
        resource.setDescription(null);

        // Create the Resource, which fails.

        restResourceMockMvc.perform(post("/api/resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resource)))
            .andExpect(status().isBadRequest());

        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllResources() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList
        restResourceMockMvc.perform(get("/api/resources?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resource.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getResource() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get the resource
        restResourceMockMvc.perform(get("/api/resources/{id}", resource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(resource.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingResource() throws Exception {
        // Get the resource
        restResourceMockMvc.perform(get("/api/resources/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResource() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();

        // Update the resource
        Resource updatedResource = resourceRepository.findById(resource.getId()).get();
        // Disconnect from session so that the updates on updatedResource are not directly saved in db
        em.detach(updatedResource);
        updatedResource
            .name(UPDATED_NAME)
            .url(UPDATED_URL)
            .description(UPDATED_DESCRIPTION);

        restResourceMockMvc.perform(put("/api/resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedResource)))
            .andExpect(status().isOk());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
        Resource testResource = resourceList.get(resourceList.size() - 1);
        assertThat(testResource.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testResource.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testResource.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingResource() throws Exception {
        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();

        // Create the Resource

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceMockMvc.perform(put("/api/resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resource)))
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteResource() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        int databaseSizeBeforeDelete = resourceRepository.findAll().size();

        // Get the resource
        restResourceMockMvc.perform(delete("/api/resources/{id}", resource.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Resource.class);
        Resource resource1 = new Resource();
        resource1.setId(1L);
        Resource resource2 = new Resource();
        resource2.setId(resource1.getId());
        assertThat(resource1).isEqualTo(resource2);
        resource2.setId(2L);
        assertThat(resource1).isNotEqualTo(resource2);
        resource1.setId(null);
        assertThat(resource1).isNotEqualTo(resource2);
    }
}
