package com.rfcontrols.wms.service;

import com.rfcontrols.wms.service.dto.ItemLocationDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.rfcontrols.wms.domain.ItemLocation}.
 */
public interface ItemLocationService {

    /**
     * Save a itemLocation.
     *
     * @param itemLocationDTO the entity to save.
     * @return the persisted entity.
     */
    ItemLocationDTO save(ItemLocationDTO itemLocationDTO);

    /**
     * Get all the itemLocations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ItemLocationDTO> findAll(Pageable pageable);


    /**
     * Get the "id" itemLocation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ItemLocationDTO> findOne(Long id);

    /**
     * Find the item location related to the given item EPC
     *
     * @param epc the id of the entity.
     * @return the entity.
     */
    Optional<ItemLocationDTO> findByEpc(String epc);

    /**
     * Delete the "id" itemLocation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the itemLocation corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ItemLocationDTO> search(String query, Pageable pageable);
}
