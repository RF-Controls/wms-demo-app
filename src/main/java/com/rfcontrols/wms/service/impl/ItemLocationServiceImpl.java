package com.rfcontrols.wms.service.impl;

import com.rfcontrols.wms.service.ItemLocationService;
import com.rfcontrols.wms.domain.ItemLocation;
import com.rfcontrols.wms.repository.ItemLocationRepository;
import com.rfcontrols.wms.repository.search.ItemLocationSearchRepository;
import com.rfcontrols.wms.service.dto.ItemLocationDTO;
import com.rfcontrols.wms.service.mapper.ItemLocationMapper;
import com.rfcontrols.wms.service.mapper.ItemLocationWithItemDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ItemLocation}.
 */
@Service
@Transactional
public class ItemLocationServiceImpl implements ItemLocationService {

    private final Logger log = LoggerFactory.getLogger(ItemLocationServiceImpl.class);

    private final ItemLocationRepository itemLocationRepository;

    private final ItemLocationMapper itemLocationMapper;

    private final ItemLocationWithItemDetailsMapper itemLocationWithItemDetailsMapper;

    private final ItemLocationSearchRepository itemLocationSearchRepository;

    public ItemLocationServiceImpl(ItemLocationRepository itemLocationRepository, ItemLocationMapper itemLocationMapper, ItemLocationSearchRepository itemLocationSearchRepository, ItemLocationWithItemDetailsMapper itemLocationWithItemDetailsMapper) {
        this.itemLocationRepository = itemLocationRepository;
        this.itemLocationMapper = itemLocationMapper;
        this.itemLocationSearchRepository = itemLocationSearchRepository;
        this.itemLocationWithItemDetailsMapper = itemLocationWithItemDetailsMapper;
    }

    @Override
    public ItemLocationDTO save(ItemLocationDTO itemLocationDTO) {
        log.debug("Request to save ItemLocation : {}", itemLocationDTO);
        ItemLocation itemLocation = itemLocationMapper.toEntity(itemLocationDTO);
        itemLocation = itemLocationRepository.save(itemLocation);
        ItemLocationDTO result = itemLocationMapper.toDto(itemLocation);
        itemLocationSearchRepository.save(itemLocation);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ItemLocationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ItemLocations");
        return itemLocationRepository.findAll(pageable)
            .map(itemLocationMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ItemLocationDTO> findOne(Long id) {
        log.debug("Request to get ItemLocation : {}", id);
        return itemLocationRepository.findById(id)
            .map(itemLocationWithItemDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemLocationDTO> findByEpc(String epc) {
        log.debug("Request to get ItemLocation by EPC : {}", epc);
        return itemLocationRepository.findByItemEpc(epc).map(itemLocationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ItemLocation : {}", id);
        itemLocationRepository.deleteById(id);
        itemLocationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ItemLocationDTO> search(String query, Pageable pageable) {
        String matchingQuery = QueryBuilderUtils.buildWildcardQuery(query);
        log.debug("Request to search for a page of ItemLocations for query {}", matchingQuery);
        return itemLocationSearchRepository.search(queryStringQuery(matchingQuery), pageable)
            .map(itemLocationMapper::toDto);
    }
}
