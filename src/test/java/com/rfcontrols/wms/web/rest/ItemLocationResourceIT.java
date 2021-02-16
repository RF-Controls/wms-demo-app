package com.rfcontrols.wms.web.rest;

import com.rfcontrols.wms.WmsdemoApp;
import com.rfcontrols.wms.domain.ItemLocation;
import com.rfcontrols.wms.repository.ItemLocationRepository;
import com.rfcontrols.wms.repository.search.ItemLocationSearchRepository;
import com.rfcontrols.wms.service.ItemLocationService;
import com.rfcontrols.wms.service.dto.ItemLocationDTO;
import com.rfcontrols.wms.service.mapper.ItemLocationMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ItemLocationResource} REST controller.
 */
@SpringBootTest(classes = WmsdemoApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ItemLocationResourceIT {

    private static final Instant DEFAULT_LAST_LOCATION_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_LOCATION_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ZONE_ENTER_INSTANT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ZONE_ENTER_INSTANT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_X = 1D;
    private static final Double UPDATED_X = 2D;

    private static final Double DEFAULT_Y = 1D;
    private static final Double UPDATED_Y = 2D;

    private static final Double DEFAULT_Z = 1D;
    private static final Double UPDATED_Z = 2D;

    @Autowired
    private ItemLocationRepository itemLocationRepository;

    @Autowired
    private ItemLocationMapper itemLocationMapper;

    @Autowired
    private ItemLocationService itemLocationService;

    /**
     * This repository is mocked in the com.rfcontrols.wms.repository.search test package.
     *
     * @see com.rfcontrols.wms.repository.search.ItemLocationSearchRepositoryMockConfiguration
     */
    @Autowired
    private ItemLocationSearchRepository mockItemLocationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemLocationMockMvc;

    private ItemLocation itemLocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemLocation createEntity(EntityManager em) {
        ItemLocation itemLocation = new ItemLocation()
            .lastLocationUpdate(DEFAULT_LAST_LOCATION_UPDATE)
            .zoneEnterInstant(DEFAULT_ZONE_ENTER_INSTANT)
            .description(DEFAULT_DESCRIPTION)
            .x(DEFAULT_X)
            .y(DEFAULT_Y)
            .z(DEFAULT_Z);
        return itemLocation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemLocation createUpdatedEntity(EntityManager em) {
        ItemLocation itemLocation = new ItemLocation()
            .lastLocationUpdate(UPDATED_LAST_LOCATION_UPDATE)
            .zoneEnterInstant(UPDATED_ZONE_ENTER_INSTANT)
            .description(UPDATED_DESCRIPTION)
            .x(UPDATED_X)
            .y(UPDATED_Y)
            .z(UPDATED_Z);
        return itemLocation;
    }

    @BeforeEach
    public void initTest() {
        itemLocation = createEntity(em);
    }

    @Test
    @Transactional
    public void createItemLocation() throws Exception {
        int databaseSizeBeforeCreate = itemLocationRepository.findAll().size();
        // Create the ItemLocation
        ItemLocationDTO itemLocationDTO = itemLocationMapper.toDto(itemLocation);
        restItemLocationMockMvc.perform(post("/api/item-locations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemLocationDTO)))
            .andExpect(status().isCreated());

        // Validate the ItemLocation in the database
        List<ItemLocation> itemLocationList = itemLocationRepository.findAll();
        assertThat(itemLocationList).hasSize(databaseSizeBeforeCreate + 1);
        ItemLocation testItemLocation = itemLocationList.get(itemLocationList.size() - 1);
        assertThat(testItemLocation.getLastLocationUpdate()).isEqualTo(DEFAULT_LAST_LOCATION_UPDATE);
        assertThat(testItemLocation.getZoneEnterInstant()).isEqualTo(DEFAULT_ZONE_ENTER_INSTANT);
        assertThat(testItemLocation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testItemLocation.getX()).isEqualTo(DEFAULT_X);
        assertThat(testItemLocation.getY()).isEqualTo(DEFAULT_Y);
        assertThat(testItemLocation.getZ()).isEqualTo(DEFAULT_Z);

        // Validate the ItemLocation in Elasticsearch
        verify(mockItemLocationSearchRepository, times(1)).save(testItemLocation);
    }

    @Test
    @Transactional
    public void createItemLocationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itemLocationRepository.findAll().size();

        // Create the ItemLocation with an existing ID
        itemLocation.setId(1L);
        ItemLocationDTO itemLocationDTO = itemLocationMapper.toDto(itemLocation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemLocationMockMvc.perform(post("/api/item-locations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemLocationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ItemLocation in the database
        List<ItemLocation> itemLocationList = itemLocationRepository.findAll();
        assertThat(itemLocationList).hasSize(databaseSizeBeforeCreate);

        // Validate the ItemLocation in Elasticsearch
        verify(mockItemLocationSearchRepository, times(0)).save(itemLocation);
    }


    @Test
    @Transactional
    public void checkLastLocationUpdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemLocationRepository.findAll().size();
        // set the field null
        itemLocation.setLastLocationUpdate(null);

        // Create the ItemLocation, which fails.
        ItemLocationDTO itemLocationDTO = itemLocationMapper.toDto(itemLocation);


        restItemLocationMockMvc.perform(post("/api/item-locations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemLocationDTO)))
            .andExpect(status().isBadRequest());

        List<ItemLocation> itemLocationList = itemLocationRepository.findAll();
        assertThat(itemLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkXIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemLocationRepository.findAll().size();
        // set the field null
        itemLocation.setX(null);

        // Create the ItemLocation, which fails.
        ItemLocationDTO itemLocationDTO = itemLocationMapper.toDto(itemLocation);


        restItemLocationMockMvc.perform(post("/api/item-locations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemLocationDTO)))
            .andExpect(status().isBadRequest());

        List<ItemLocation> itemLocationList = itemLocationRepository.findAll();
        assertThat(itemLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemLocationRepository.findAll().size();
        // set the field null
        itemLocation.setY(null);

        // Create the ItemLocation, which fails.
        ItemLocationDTO itemLocationDTO = itemLocationMapper.toDto(itemLocation);


        restItemLocationMockMvc.perform(post("/api/item-locations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemLocationDTO)))
            .andExpect(status().isBadRequest());

        List<ItemLocation> itemLocationList = itemLocationRepository.findAll();
        assertThat(itemLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkZIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemLocationRepository.findAll().size();
        // set the field null
        itemLocation.setZ(null);

        // Create the ItemLocation, which fails.
        ItemLocationDTO itemLocationDTO = itemLocationMapper.toDto(itemLocation);


        restItemLocationMockMvc.perform(post("/api/item-locations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemLocationDTO)))
            .andExpect(status().isBadRequest());

        List<ItemLocation> itemLocationList = itemLocationRepository.findAll();
        assertThat(itemLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItemLocations() throws Exception {
        // Initialize the database
        itemLocationRepository.saveAndFlush(itemLocation);

        // Get all the itemLocationList
        restItemLocationMockMvc.perform(get("/api/item-locations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].lastLocationUpdate").value(hasItem(DEFAULT_LAST_LOCATION_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].zoneEnterInstant").value(hasItem(DEFAULT_ZONE_ENTER_INSTANT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X.doubleValue())))
            .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y.doubleValue())))
            .andExpect(jsonPath("$.[*].z").value(hasItem(DEFAULT_Z.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getItemLocation() throws Exception {
        // Initialize the database
        itemLocationRepository.saveAndFlush(itemLocation);

        // Get the itemLocation
        restItemLocationMockMvc.perform(get("/api/item-locations/{id}", itemLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemLocation.getId().intValue()))
            .andExpect(jsonPath("$.lastLocationUpdate").value(DEFAULT_LAST_LOCATION_UPDATE.toString()))
            .andExpect(jsonPath("$.zoneEnterInstant").value(DEFAULT_ZONE_ENTER_INSTANT.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.x").value(DEFAULT_X.doubleValue()))
            .andExpect(jsonPath("$.y").value(DEFAULT_Y.doubleValue()))
            .andExpect(jsonPath("$.z").value(DEFAULT_Z.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingItemLocation() throws Exception {
        // Get the itemLocation
        restItemLocationMockMvc.perform(get("/api/item-locations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemLocation() throws Exception {
        // Initialize the database
        itemLocationRepository.saveAndFlush(itemLocation);

        int databaseSizeBeforeUpdate = itemLocationRepository.findAll().size();

        // Update the itemLocation
        ItemLocation updatedItemLocation = itemLocationRepository.findById(itemLocation.getId()).get();
        // Disconnect from session so that the updates on updatedItemLocation are not directly saved in db
        em.detach(updatedItemLocation);
        updatedItemLocation
            .lastLocationUpdate(UPDATED_LAST_LOCATION_UPDATE)
            .zoneEnterInstant(UPDATED_ZONE_ENTER_INSTANT)
            .description(UPDATED_DESCRIPTION)
            .x(UPDATED_X)
            .y(UPDATED_Y)
            .z(UPDATED_Z);
        ItemLocationDTO itemLocationDTO = itemLocationMapper.toDto(updatedItemLocation);

        restItemLocationMockMvc.perform(put("/api/item-locations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemLocationDTO)))
            .andExpect(status().isOk());

        // Validate the ItemLocation in the database
        List<ItemLocation> itemLocationList = itemLocationRepository.findAll();
        assertThat(itemLocationList).hasSize(databaseSizeBeforeUpdate);
        ItemLocation testItemLocation = itemLocationList.get(itemLocationList.size() - 1);
        assertThat(testItemLocation.getLastLocationUpdate()).isEqualTo(UPDATED_LAST_LOCATION_UPDATE);
        assertThat(testItemLocation.getZoneEnterInstant()).isEqualTo(UPDATED_ZONE_ENTER_INSTANT);
        assertThat(testItemLocation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testItemLocation.getX()).isEqualTo(UPDATED_X);
        assertThat(testItemLocation.getY()).isEqualTo(UPDATED_Y);
        assertThat(testItemLocation.getZ()).isEqualTo(UPDATED_Z);

        // Validate the ItemLocation in Elasticsearch
        verify(mockItemLocationSearchRepository, times(1)).save(testItemLocation);
    }

    @Test
    @Transactional
    public void updateNonExistingItemLocation() throws Exception {
        int databaseSizeBeforeUpdate = itemLocationRepository.findAll().size();

        // Create the ItemLocation
        ItemLocationDTO itemLocationDTO = itemLocationMapper.toDto(itemLocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemLocationMockMvc.perform(put("/api/item-locations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(itemLocationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ItemLocation in the database
        List<ItemLocation> itemLocationList = itemLocationRepository.findAll();
        assertThat(itemLocationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ItemLocation in Elasticsearch
        verify(mockItemLocationSearchRepository, times(0)).save(itemLocation);
    }

    @Test
    @Transactional
    public void deleteItemLocation() throws Exception {
        // Initialize the database
        itemLocationRepository.saveAndFlush(itemLocation);

        int databaseSizeBeforeDelete = itemLocationRepository.findAll().size();

        // Delete the itemLocation
        restItemLocationMockMvc.perform(delete("/api/item-locations/{id}", itemLocation.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemLocation> itemLocationList = itemLocationRepository.findAll();
        assertThat(itemLocationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ItemLocation in Elasticsearch
        verify(mockItemLocationSearchRepository, times(1)).deleteById(itemLocation.getId());
    }

    @Test
    @Transactional
    public void searchItemLocation() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        itemLocationRepository.saveAndFlush(itemLocation);
        when(mockItemLocationSearchRepository.search(queryStringQuery("id:" + itemLocation.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(itemLocation), PageRequest.of(0, 1), 1));

        // Search the itemLocation
        restItemLocationMockMvc.perform(get("/api/_search/item-locations?query=id:" + itemLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].lastLocationUpdate").value(hasItem(DEFAULT_LAST_LOCATION_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].zoneEnterInstant").value(hasItem(DEFAULT_ZONE_ENTER_INSTANT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X.doubleValue())))
            .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y.doubleValue())))
            .andExpect(jsonPath("$.[*].z").value(hasItem(DEFAULT_Z.doubleValue())));
    }
}
