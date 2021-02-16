package com.rfcontrols.wms.service.impl;

import com.rfcontrols.wms.service.ZoneAssociationService;
import com.rfcontrols.wms.service.ZoneService;
import com.rfcontrols.wms.domain.Zone;
import com.rfcontrols.wms.repository.ZoneRepository;
import com.rfcontrols.wms.repository.search.ZoneSearchRepository;
import com.rfcontrols.wms.service.dto.ZoneDTO;
import com.rfcontrols.wms.service.mapper.ZoneMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Zone}.
 */
@Service
@Transactional
public class ZoneServiceImpl implements ZoneService {

    private final Logger log = LoggerFactory.getLogger(ZoneServiceImpl.class);

    private final ZoneRepository zoneRepository;

    private final ZoneMapper zoneMapper;

    private final ZoneSearchRepository zoneSearchRepository;

    private final ZoneAssociationService zoneAssociationService;

    public ZoneServiceImpl(ZoneRepository zoneRepository, ZoneMapper zoneMapper, ZoneSearchRepository zoneSearchRepository, ZoneAssociationService zoneAssociationService) {
        this.zoneRepository = zoneRepository;
        this.zoneMapper = zoneMapper;
        this.zoneSearchRepository = zoneSearchRepository;
        this.zoneAssociationService = zoneAssociationService;
    }

    @Override
    public ZoneDTO save(ZoneDTO zoneDTO) {
        log.debug("Request to save Zone : {}", zoneDTO);
        Zone zone = zoneMapper.toEntity(zoneDTO);
        zone = zoneRepository.save(zone);
        ZoneDTO result = zoneMapper.toDto(zone);
        zoneSearchRepository.save(zone);
        zoneAssociationService.loadZones();
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ZoneDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Zones");
        return zoneRepository.findAll(pageable)
            .map(zoneMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ZoneDTO> findOne(Long id) {
        log.debug("Request to get Zone : {}", id);
        return zoneRepository.findById(id)
            .map(zoneMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Zone : {}", id);
        zoneRepository.deleteById(id);
        zoneSearchRepository.deleteById(id);
        zoneAssociationService.loadZones();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ZoneDTO> search(String query, Pageable pageable) {
        String matchingQuery = QueryBuilderUtils.buildWildcardQuery(query);
        log.debug("Request to search for a page of Zones for query {}", matchingQuery);
        return zoneSearchRepository.search(queryStringQuery(matchingQuery), pageable)
            .map(zoneMapper::toDto);
    }
}
