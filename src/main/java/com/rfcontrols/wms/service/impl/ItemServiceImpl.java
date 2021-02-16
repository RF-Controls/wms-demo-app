package com.rfcontrols.wms.service.impl;

import com.rfcontrols.wms.service.ItemService;
import com.rfcontrols.wms.domain.Item;
import com.rfcontrols.wms.repository.ItemRepository;
import com.rfcontrols.wms.repository.search.ItemSearchRepository;
import com.rfcontrols.wms.service.dto.ItemDTO;
import com.rfcontrols.wms.service.mapper.ItemMapper;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Item}.
 */
@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    private final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    private final ItemSearchRepository itemSearchRepository;

    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper, ItemSearchRepository itemSearchRepository) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.itemSearchRepository = itemSearchRepository;
    }

    @Override
    public ItemDTO save(ItemDTO itemDTO) {
        log.debug("Request to save Item : {}", itemDTO);
        Item item = itemMapper.toEntity(itemDTO);

        try {
            ByteArrayOutputStream detailOut = new ByteArrayOutputStream();
            Thumbnails.of(new ByteArrayInputStream(itemDTO.getDetailImage())).size(600, 600).toOutputStream(detailOut);
            item.setDetailImage(detailOut.toByteArray());
        }catch (Throwable throwable){
            log.error("Error resizing the detailed image", throwable);
        }

        try {
            ByteArrayOutputStream thumbOut = new ByteArrayOutputStream();
            Thumbnails.of(new ByteArrayInputStream(itemDTO.getDetailImage())).size(100, 100).toOutputStream(thumbOut);
            item.setThumbnail(thumbOut.toByteArray());
            item.setThumbnailContentType(item.getDetailImageContentType());
        }catch (Throwable throwable){
            log.error("Error resizing the thumbnail image", throwable);
        }

        item = itemRepository.save(item);
        ItemDTO result = itemMapper.toDto(item);
        itemSearchRepository.save(item);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Items");
        return itemRepository.findAll(pageable)
            .map(itemMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ItemDTO> findOne(Long id) {
        log.debug("Request to get Item : {}", id);
        Optional<Item> itemOptional = itemRepository.findById(id);
        Optional<ItemDTO> itemDTO = itemOptional.map(itemMapper::toDto);

        if(itemOptional.isPresent()){
            itemDTO.get().setDetailImage(itemOptional.get().getDetailImage());
            itemDTO.get().setDetailImageContentType(itemOptional.get().getDetailImageContentType());
        }

        return itemDTO;
    }

    @Override
    public Optional<ItemDTO> findByEpc(String epc) {
        log.debug("Request to get Item by epc: {}", epc);
        Optional<ItemDTO> itemDTO = itemRepository.findByEpc(epc)
            .map(itemMapper::toDto);

        if(itemDTO.isPresent()){
            itemDTO.get().setDetailImage(null);
            itemDTO.get().setThumbnail(null);
        }
        return itemDTO;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Item : {}", id);
        itemRepository.deleteById(id);
        itemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ItemDTO> search(String query, Pageable pageable) {
        String matchingQuery = QueryBuilderUtils.buildWildcardQuery(query);
        log.debug("Request to search for a page of Items for query {}", matchingQuery);
        return itemSearchRepository.search(queryStringQuery(matchingQuery), pageable)
            .map(itemMapper::toDto);
    }
}
