package com.hackernight.lwt.web.rest;

import com.hackernight.lwt.LwtApp;

import com.hackernight.lwt.domain.Episode;
import com.hackernight.lwt.repository.EpisodeRepository;
import com.hackernight.lwt.web.rest.errors.ExceptionTranslator;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


import static com.hackernight.lwt.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EpisodeResource REST controller.
 *
 * @see EpisodeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LwtApp.class)
public class EpisodeResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_AIR_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_AIR_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_EPISODE_NUMBER = 1;
    private static final Integer UPDATED_EPISODE_NUMBER = 2;

    private static final Integer DEFAULT_EPISODE_SEASON = 1;
    private static final Integer UPDATED_EPISODE_SEASON = 2;

    private static final String DEFAULT_THUMBNAIL_URL = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL_URL = "BBBBBBBBBB";

    private static final String DEFAULT_YOUTUBE_URL = "AAAAAAAAAA";
    private static final String UPDATED_YOUTUBE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CALL_TO_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_CALL_TO_ACTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CALL_TO_ACTION_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CALL_TO_ACTION_DUE_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private EpisodeRepository episodeRepository;

    @Mock
    private EpisodeRepository episodeRepositoryMock;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEpisodeMockMvc;

    private Episode episode;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EpisodeResource episodeResource = new EpisodeResource(episodeRepository);
        this.restEpisodeMockMvc = MockMvcBuilders.standaloneSetup(episodeResource)
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
    public static Episode createEntity(EntityManager em) {
        Episode episode = new Episode()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .airDate(DEFAULT_AIR_DATE)
            .episodeNumber(DEFAULT_EPISODE_NUMBER)
            .episodeSeason(DEFAULT_EPISODE_SEASON)
            .thumbnailUrl(DEFAULT_THUMBNAIL_URL)
            .youtubeUrl(DEFAULT_YOUTUBE_URL)
            .callToAction(DEFAULT_CALL_TO_ACTION)
            .callToActionDueDate(DEFAULT_CALL_TO_ACTION_DUE_DATE);
        return episode;
    }

    @Before
    public void initTest() {
        episode = createEntity(em);
    }

    @Test
    @Transactional
    public void createEpisode() throws Exception {
        int databaseSizeBeforeCreate = episodeRepository.findAll().size();

        // Create the Episode
        restEpisodeMockMvc.perform(post("/api/episodes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(episode)))
            .andExpect(status().isCreated());

        // Validate the Episode in the database
        List<Episode> episodeList = episodeRepository.findAll();
        assertThat(episodeList).hasSize(databaseSizeBeforeCreate + 1);
        Episode testEpisode = episodeList.get(episodeList.size() - 1);
        assertThat(testEpisode.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEpisode.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEpisode.getAirDate()).isEqualTo(DEFAULT_AIR_DATE);
        assertThat(testEpisode.getEpisodeNumber()).isEqualTo(DEFAULT_EPISODE_NUMBER);
        assertThat(testEpisode.getEpisodeSeason()).isEqualTo(DEFAULT_EPISODE_SEASON);
        assertThat(testEpisode.getThumbnailUrl()).isEqualTo(DEFAULT_THUMBNAIL_URL);
        assertThat(testEpisode.getYoutubeUrl()).isEqualTo(DEFAULT_YOUTUBE_URL);
        assertThat(testEpisode.getCallToAction()).isEqualTo(DEFAULT_CALL_TO_ACTION);
        assertThat(testEpisode.getCallToActionDueDate()).isEqualTo(DEFAULT_CALL_TO_ACTION_DUE_DATE);
    }

    @Test
    @Transactional
    public void createEpisodeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = episodeRepository.findAll().size();

        // Create the Episode with an existing ID
        episode.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEpisodeMockMvc.perform(post("/api/episodes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(episode)))
            .andExpect(status().isBadRequest());

        // Validate the Episode in the database
        List<Episode> episodeList = episodeRepository.findAll();
        assertThat(episodeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = episodeRepository.findAll().size();
        // set the field null
        episode.setTitle(null);

        // Create the Episode, which fails.

        restEpisodeMockMvc.perform(post("/api/episodes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(episode)))
            .andExpect(status().isBadRequest());

        List<Episode> episodeList = episodeRepository.findAll();
        assertThat(episodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = episodeRepository.findAll().size();
        // set the field null
        episode.setDescription(null);

        // Create the Episode, which fails.

        restEpisodeMockMvc.perform(post("/api/episodes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(episode)))
            .andExpect(status().isBadRequest());

        List<Episode> episodeList = episodeRepository.findAll();
        assertThat(episodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAirDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = episodeRepository.findAll().size();
        // set the field null
        episode.setAirDate(null);

        // Create the Episode, which fails.

        restEpisodeMockMvc.perform(post("/api/episodes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(episode)))
            .andExpect(status().isBadRequest());

        List<Episode> episodeList = episodeRepository.findAll();
        assertThat(episodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEpisodeNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = episodeRepository.findAll().size();
        // set the field null
        episode.setEpisodeNumber(null);

        // Create the Episode, which fails.

        restEpisodeMockMvc.perform(post("/api/episodes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(episode)))
            .andExpect(status().isBadRequest());

        List<Episode> episodeList = episodeRepository.findAll();
        assertThat(episodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEpisodeSeasonIsRequired() throws Exception {
        int databaseSizeBeforeTest = episodeRepository.findAll().size();
        // set the field null
        episode.setEpisodeSeason(null);

        // Create the Episode, which fails.

        restEpisodeMockMvc.perform(post("/api/episodes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(episode)))
            .andExpect(status().isBadRequest());

        List<Episode> episodeList = episodeRepository.findAll();
        assertThat(episodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEpisodes() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodeList
        restEpisodeMockMvc.perform(get("/api/episodes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(episode.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].airDate").value(hasItem(DEFAULT_AIR_DATE.toString())))
            .andExpect(jsonPath("$.[*].episodeNumber").value(hasItem(DEFAULT_EPISODE_NUMBER)))
            .andExpect(jsonPath("$.[*].episodeSeason").value(hasItem(DEFAULT_EPISODE_SEASON)))
            .andExpect(jsonPath("$.[*].thumbnailUrl").value(hasItem(DEFAULT_THUMBNAIL_URL.toString())))
            .andExpect(jsonPath("$.[*].youtubeUrl").value(hasItem(DEFAULT_YOUTUBE_URL.toString())))
            .andExpect(jsonPath("$.[*].callToAction").value(hasItem(DEFAULT_CALL_TO_ACTION.toString())))
            .andExpect(jsonPath("$.[*].callToActionDueDate").value(hasItem(DEFAULT_CALL_TO_ACTION_DUE_DATE.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllEpisodesWithEagerRelationshipsIsEnabled() throws Exception {
        EpisodeResource episodeResource = new EpisodeResource(episodeRepositoryMock);
        when(episodeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restEpisodeMockMvc = MockMvcBuilders.standaloneSetup(episodeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restEpisodeMockMvc.perform(get("/api/episodes?eagerload=true"))
        .andExpect(status().isOk());

        verify(episodeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllEpisodesWithEagerRelationshipsIsNotEnabled() throws Exception {
        EpisodeResource episodeResource = new EpisodeResource(episodeRepositoryMock);
            when(episodeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restEpisodeMockMvc = MockMvcBuilders.standaloneSetup(episodeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restEpisodeMockMvc.perform(get("/api/episodes?eagerload=true"))
        .andExpect(status().isOk());

            verify(episodeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getEpisode() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get the episode
        restEpisodeMockMvc.perform(get("/api/episodes/{id}", episode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(episode.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.airDate").value(DEFAULT_AIR_DATE.toString()))
            .andExpect(jsonPath("$.episodeNumber").value(DEFAULT_EPISODE_NUMBER))
            .andExpect(jsonPath("$.episodeSeason").value(DEFAULT_EPISODE_SEASON))
            .andExpect(jsonPath("$.thumbnailUrl").value(DEFAULT_THUMBNAIL_URL.toString()))
            .andExpect(jsonPath("$.youtubeUrl").value(DEFAULT_YOUTUBE_URL.toString()))
            .andExpect(jsonPath("$.callToAction").value(DEFAULT_CALL_TO_ACTION.toString()))
            .andExpect(jsonPath("$.callToActionDueDate").value(DEFAULT_CALL_TO_ACTION_DUE_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEpisode() throws Exception {
        // Get the episode
        restEpisodeMockMvc.perform(get("/api/episodes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEpisode() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        int databaseSizeBeforeUpdate = episodeRepository.findAll().size();

        // Update the episode
        Episode updatedEpisode = episodeRepository.findById(episode.getId()).get();
        // Disconnect from session so that the updates on updatedEpisode are not directly saved in db
        em.detach(updatedEpisode);
        updatedEpisode
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .airDate(UPDATED_AIR_DATE)
            .episodeNumber(UPDATED_EPISODE_NUMBER)
            .episodeSeason(UPDATED_EPISODE_SEASON)
            .thumbnailUrl(UPDATED_THUMBNAIL_URL)
            .youtubeUrl(UPDATED_YOUTUBE_URL)
            .callToAction(UPDATED_CALL_TO_ACTION)
            .callToActionDueDate(UPDATED_CALL_TO_ACTION_DUE_DATE);

        restEpisodeMockMvc.perform(put("/api/episodes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEpisode)))
            .andExpect(status().isOk());

        // Validate the Episode in the database
        List<Episode> episodeList = episodeRepository.findAll();
        assertThat(episodeList).hasSize(databaseSizeBeforeUpdate);
        Episode testEpisode = episodeList.get(episodeList.size() - 1);
        assertThat(testEpisode.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEpisode.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEpisode.getAirDate()).isEqualTo(UPDATED_AIR_DATE);
        assertThat(testEpisode.getEpisodeNumber()).isEqualTo(UPDATED_EPISODE_NUMBER);
        assertThat(testEpisode.getEpisodeSeason()).isEqualTo(UPDATED_EPISODE_SEASON);
        assertThat(testEpisode.getThumbnailUrl()).isEqualTo(UPDATED_THUMBNAIL_URL);
        assertThat(testEpisode.getYoutubeUrl()).isEqualTo(UPDATED_YOUTUBE_URL);
        assertThat(testEpisode.getCallToAction()).isEqualTo(UPDATED_CALL_TO_ACTION);
        assertThat(testEpisode.getCallToActionDueDate()).isEqualTo(UPDATED_CALL_TO_ACTION_DUE_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingEpisode() throws Exception {
        int databaseSizeBeforeUpdate = episodeRepository.findAll().size();

        // Create the Episode

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEpisodeMockMvc.perform(put("/api/episodes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(episode)))
            .andExpect(status().isBadRequest());

        // Validate the Episode in the database
        List<Episode> episodeList = episodeRepository.findAll();
        assertThat(episodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEpisode() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        int databaseSizeBeforeDelete = episodeRepository.findAll().size();

        // Get the episode
        restEpisodeMockMvc.perform(delete("/api/episodes/{id}", episode.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Episode> episodeList = episodeRepository.findAll();
        assertThat(episodeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Episode.class);
        Episode episode1 = new Episode();
        episode1.setId(1L);
        Episode episode2 = new Episode();
        episode2.setId(episode1.getId());
        assertThat(episode1).isEqualTo(episode2);
        episode2.setId(2L);
        assertThat(episode1).isNotEqualTo(episode2);
        episode1.setId(null);
        assertThat(episode1).isNotEqualTo(episode2);
    }
}
