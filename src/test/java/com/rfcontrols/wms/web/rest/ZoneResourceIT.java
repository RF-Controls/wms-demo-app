package com.rfcontrols.wms.web.rest;

import com.rfcontrols.wms.WmsdemoApp;
import com.rfcontrols.wms.domain.Zone;
import com.rfcontrols.wms.repository.ZoneRepository;
import com.rfcontrols.wms.repository.search.ZoneSearchRepository;
import com.rfcontrols.wms.service.ZoneService;
import com.rfcontrols.wms.service.dto.ZoneDTO;
import com.rfcontrols.wms.service.mapper.ZoneMapper;

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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ZoneResource} REST controller.
 */
@SpringBootTest(classes = WmsdemoApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ZoneResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_MAX_ITEMS = 1;
    private static final Integer UPDATED_MAX_ITEMS = 2;

    private static final Double DEFAULT_X_1 = 1D;
    private static final Double UPDATED_X_1 = 2D;

    private static final Double DEFAULT_Y_1 = 1D;
    private static final Double UPDATED_Y_1 = 2D;

    private static final Double DEFAULT_Z_1 = 1D;
    private static final Double UPDATED_Z_1 = 2D;

    private static final Double DEFAULT_X_2 = 1D;
    private static final Double UPDATED_X_2 = 2D;

    private static final Double DEFAULT_Y_2 = 1D;
    private static final Double UPDATED_Y_2 = 2D;

    private static final Double DEFAULT_Z_2 = 1D;
    private static final Double UPDATED_Z_2 = 2D;

    private static final Boolean DEFAULT_ASSOCIATION_ZONE = false;
    private static final Boolean UPDATED_ASSOCIATION_ZONE = true;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private ZoneMapper zoneMapper;

    @Autowired
    private ZoneService zoneService;

    /**
     * This repository is mocked in the com.rfcontrols.wms.repository.search test package.
     *
     * @see com.rfcontrols.wms.repository.search.ZoneSearchRepositoryMockConfiguration
     */
    @Autowired
    private ZoneSearchRepository mockZoneSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restZoneMockMvc;

    private Zone zone;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Zone createEntity(EntityManager em) {
        Zone zone = new Zone()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .maxItems(DEFAULT_MAX_ITEMS)
            .x1(DEFAULT_X_1)
            .y1(DEFAULT_Y_1)
            .z1(DEFAULT_Z_1)
            .x2(DEFAULT_X_2)
            .y2(DEFAULT_Y_2)
            .z2(DEFAULT_Z_2)
            .associationZone(DEFAULT_ASSOCIATION_ZONE);
        return zone;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Zone createUpdatedEntity(EntityManager em) {
        Zone zone = new Zone()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .maxItems(UPDATED_MAX_ITEMS)
            .x1(UPDATED_X_1)
            .y1(UPDATED_Y_1)
            .z1(UPDATED_Z_1)
            .x2(UPDATED_X_2)
            .y2(UPDATED_Y_2)
            .z2(UPDATED_Z_2)
            .associationZone(UPDATED_ASSOCIATION_ZONE);
        return zone;
    }

    @BeforeEach
    public void initTest() {
        zone = createEntity(em);
    }

    @Test
    @Transactional
    public void createZone() throws Exception {
        int databaseSizeBeforeCreate = zoneRepository.findAll().size();
        // Create the Zone
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);
        restZoneMockMvc.perform(post("/api/zones")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(zoneDTO)))
            .andExpect(status().isCreated());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeCreate + 1);
        Zone testZone = zoneList.get(zoneList.size() - 1);
        assertThat(testZone.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testZone.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testZone.getMaxItems()).isEqualTo(DEFAULT_MAX_ITEMS);
        assertThat(testZone.getx1()).isEqualTo(DEFAULT_X_1);
        assertThat(testZone.gety1()).isEqualTo(DEFAULT_Y_1);
        assertThat(testZone.getz1()).isEqualTo(DEFAULT_Z_1);
        assertThat(testZone.getx2()).isEqualTo(DEFAULT_X_2);
        assertThat(testZone.gety2()).isEqualTo(DEFAULT_Y_2);
        assertThat(testZone.getz2()).isEqualTo(DEFAULT_Z_2);
        assertThat(testZone.isAssociationZone()).isEqualTo(DEFAULT_ASSOCIATION_ZONE);

        // Validate the Zone in Elasticsearch
        verify(mockZoneSearchRepository, times(1)).save(testZone);
    }

    @Test
    @Transactional
    public void createZoneWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = zoneRepository.findAll().size();

        // Create the Zone with an existing ID
        zone.setId(1L);
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);

        // An entity with an existing ID cannot be created, so this API call must fail
        restZoneMockMvc.perform(post("/api/zones")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(zoneDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeCreate);

        // Validate the Zone in Elasticsearch
        verify(mockZoneSearchRepository, times(0)).save(zone);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = zoneRepository.findAll().size();
        // set the field null
        zone.setName(null);

        // Create the Zone, which fails.
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);


        restZoneMockMvc.perform(post("/api/zones")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(zoneDTO)))
            .andExpect(status().isBadRequest());

        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkx1IsRequired() throws Exception {
        int databaseSizeBeforeTest = zoneRepository.findAll().size();
        // set the field null
        zone.setx1(null);

        // Create the Zone, which fails.
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);


        restZoneMockMvc.perform(post("/api/zones")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(zoneDTO)))
            .andExpect(status().isBadRequest());

        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checky1IsRequired() throws Exception {
        int databaseSizeBeforeTest = zoneRepository.findAll().size();
        // set the field null
        zone.sety1(null);

        // Create the Zone, which fails.
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);


        restZoneMockMvc.perform(post("/api/zones")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(zoneDTO)))
            .andExpect(status().isBadRequest());

        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkz1IsRequired() throws Exception {
        int databaseSizeBeforeTest = zoneRepository.findAll().size();
        // set the field null
        zone.setz1(null);

        // Create the Zone, which fails.
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);


        restZoneMockMvc.perform(post("/api/zones")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(zoneDTO)))
            .andExpect(status().isBadRequest());

        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkx2IsRequired() throws Exception {
        int databaseSizeBeforeTest = zoneRepository.findAll().size();
        // set the field null
        zone.setx2(null);

        // Create the Zone, which fails.
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);


        restZoneMockMvc.perform(post("/api/zones")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(zoneDTO)))
            .andExpect(status().isBadRequest());

        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checky2IsRequired() throws Exception {
        int databaseSizeBeforeTest = zoneRepository.findAll().size();
        // set the field null
        zone.sety2(null);

        // Create the Zone, which fails.
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);


        restZoneMockMvc.perform(post("/api/zones")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(zoneDTO)))
            .andExpect(status().isBadRequest());

        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkz2IsRequired() throws Exception {
        int databaseSizeBeforeTest = zoneRepository.findAll().size();
        // set the field null
        zone.setz2(null);

        // Create the Zone, which fails.
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);


        restZoneMockMvc.perform(post("/api/zones")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(zoneDTO)))
            .andExpect(status().isBadRequest());

        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAssociationZoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = zoneRepository.findAll().size();
        // set the field null
        zone.setAssociationZone(null);

        // Create the Zone, which fails.
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);


        restZoneMockMvc.perform(post("/api/zones")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(zoneDTO)))
            .andExpect(status().isBadRequest());

        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllZones() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList
        restZoneMockMvc.perform(get("/api/zones?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zone.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].maxItems").value(hasItem(DEFAULT_MAX_ITEMS)))
            .andExpect(jsonPath("$.[*].x1").value(hasItem(DEFAULT_X_1.doubleValue())))
            .andExpect(jsonPath("$.[*].y1").value(hasItem(DEFAULT_Y_1.doubleValue())))
            .andExpect(jsonPath("$.[*].z1").value(hasItem(DEFAULT_Z_1.doubleValue())))
            .andExpect(jsonPath("$.[*].x2").value(hasItem(DEFAULT_X_2.doubleValue())))
            .andExpect(jsonPath("$.[*].y2").value(hasItem(DEFAULT_Y_2.doubleValue())))
            .andExpect(jsonPath("$.[*].z2").value(hasItem(DEFAULT_Z_2.doubleValue())))
            .andExpect(jsonPath("$.[*].associationZone").value(hasItem(DEFAULT_ASSOCIATION_ZONE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getZone() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get the zone
        restZoneMockMvc.perform(get("/api/zones/{id}", zone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(zone.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.maxItems").value(DEFAULT_MAX_ITEMS))
            .andExpect(jsonPath("$.x1").value(DEFAULT_X_1.doubleValue()))
            .andExpect(jsonPath("$.y1").value(DEFAULT_Y_1.doubleValue()))
            .andExpect(jsonPath("$.z1").value(DEFAULT_Z_1.doubleValue()))
            .andExpect(jsonPath("$.x2").value(DEFAULT_X_2.doubleValue()))
            .andExpect(jsonPath("$.y2").value(DEFAULT_Y_2.doubleValue()))
            .andExpect(jsonPath("$.z2").value(DEFAULT_Z_2.doubleValue()))
            .andExpect(jsonPath("$.associationZone").value(DEFAULT_ASSOCIATION_ZONE.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingZone() throws Exception {
        // Get the zone
        restZoneMockMvc.perform(get("/api/zones/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateZone() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        int databaseSizeBeforeUpdate = zoneRepository.findAll().size();

        // Update the zone
        Zone updatedZone = zoneRepository.findById(zone.getId()).get();
        // Disconnect from session so that the updates on updatedZone are not directly saved in db
        em.detach(updatedZone);
        updatedZone
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .maxItems(UPDATED_MAX_ITEMS)
            .x1(UPDATED_X_1)
            .y1(UPDATED_Y_1)
            .z1(UPDATED_Z_1)
            .x2(UPDATED_X_2)
            .y2(UPDATED_Y_2)
            .z2(UPDATED_Z_2)
            .associationZone(UPDATED_ASSOCIATION_ZONE);
        ZoneDTO zoneDTO = zoneMapper.toDto(updatedZone);

        restZoneMockMvc.perform(put("/api/zones")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(zoneDTO)))
            .andExpect(status().isOk());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate);
        Zone testZone = zoneList.get(zoneList.size() - 1);
        assertThat(testZone.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testZone.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testZone.getMaxItems()).isEqualTo(UPDATED_MAX_ITEMS);
        assertThat(testZone.getx1()).isEqualTo(UPDATED_X_1);
        assertThat(testZone.gety1()).isEqualTo(UPDATED_Y_1);
        assertThat(testZone.getz1()).isEqualTo(UPDATED_Z_1);
        assertThat(testZone.getx2()).isEqualTo(UPDATED_X_2);
        assertThat(testZone.gety2()).isEqualTo(UPDATED_Y_2);
        assertThat(testZone.getz2()).isEqualTo(UPDATED_Z_2);
        assertThat(testZone.isAssociationZone()).isEqualTo(UPDATED_ASSOCIATION_ZONE);

        // Validate the Zone in Elasticsearch
        verify(mockZoneSearchRepository, times(1)).save(testZone);
    }

    @Test
    @Transactional
    public void updateNonExistingZone() throws Exception {
        int databaseSizeBeforeUpdate = zoneRepository.findAll().size();

        // Create the Zone
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZoneMockMvc.perform(put("/api/zones")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(zoneDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Zone in Elasticsearch
        verify(mockZoneSearchRepository, times(0)).save(zone);
    }

    @Test
    @Transactional
    public void deleteZone() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        int databaseSizeBeforeDelete = zoneRepository.findAll().size();

        // Delete the zone
        restZoneMockMvc.perform(delete("/api/zones/{id}", zone.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Zone in Elasticsearch
        verify(mockZoneSearchRepository, times(1)).deleteById(zone.getId());
    }

    @Test
    @Transactional
    public void searchZone() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        zoneRepository.saveAndFlush(zone);
        when(mockZoneSearchRepository.search(queryStringQuery("id:" + zone.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(zone), PageRequest.of(0, 1), 1));

        // Search the zone
        restZoneMockMvc.perform(get("/api/_search/zones?query=id:" + zone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zone.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].maxItems").value(hasItem(DEFAULT_MAX_ITEMS)))
            .andExpect(jsonPath("$.[*].x1").value(hasItem(DEFAULT_X_1.doubleValue())))
            .andExpect(jsonPath("$.[*].y1").value(hasItem(DEFAULT_Y_1.doubleValue())))
            .andExpect(jsonPath("$.[*].z1").value(hasItem(DEFAULT_Z_1.doubleValue())))
            .andExpect(jsonPath("$.[*].x2").value(hasItem(DEFAULT_X_2.doubleValue())))
            .andExpect(jsonPath("$.[*].y2").value(hasItem(DEFAULT_Y_2.doubleValue())))
            .andExpect(jsonPath("$.[*].z2").value(hasItem(DEFAULT_Z_2.doubleValue())))
            .andExpect(jsonPath("$.[*].associationZone").value(hasItem(DEFAULT_ASSOCIATION_ZONE.booleanValue())));
    }
}
