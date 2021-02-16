package com.rfcontrols.wms.web.rest;

import com.rfcontrols.wms.service.ItemLocationService;
import com.rfcontrols.wms.web.rest.errors.BadRequestAlertException;
import com.rfcontrols.wms.service.dto.ItemLocationDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.rfcontrols.wms.domain.ItemLocation}.
 */
@RestController
@RequestMapping("/api")
public class ItemLocationResource {

    private final Logger log = LoggerFactory.getLogger(ItemLocationResource.class);

    private static final String ENTITY_NAME = "itemLocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemLocationService itemLocationService;

    public ItemLocationResource(ItemLocationService itemLocationService) {
        this.itemLocationService = itemLocationService;
    }

    /**
     * {@code POST  /item-locations} : Create a new itemLocation.
     *
     * @param itemLocationDTO the itemLocationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemLocationDTO, or with status {@code 400 (Bad Request)} if the itemLocation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-locations")
    public ResponseEntity<ItemLocationDTO> createItemLocation(@Valid @RequestBody ItemLocationDTO itemLocationDTO) throws URISyntaxException {
        log.debug("REST request to save ItemLocation : {}", itemLocationDTO);
        if (itemLocationDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemLocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemLocationDTO result = itemLocationService.save(itemLocationDTO);
        return ResponseEntity.created(new URI("/api/item-locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-locations} : Updates an existing itemLocation.
     *
     * @param itemLocationDTO the itemLocationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemLocationDTO,
     * or with status {@code 400 (Bad Request)} if the itemLocationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemLocationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-locations")
    public ResponseEntity<ItemLocationDTO> updateItemLocation(@Valid @RequestBody ItemLocationDTO itemLocationDTO) throws URISyntaxException {
        log.debug("REST request to update ItemLocation : {}", itemLocationDTO);
        if (itemLocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ItemLocationDTO result = itemLocationService.save(itemLocationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemLocationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /item-locations} : get all the itemLocations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemLocations in body.
     */
    @GetMapping("/item-locations")
    public ResponseEntity<List<ItemLocationDTO>> getAllItemLocations(Pageable pageable) {
        log.debug("REST request to get a page of ItemLocations");
        Page<ItemLocationDTO> page = itemLocationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /item-locations/:id} : get the "id" itemLocation.
     *
     * @param id the id of the itemLocationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemLocationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-locations/{id}")
    public ResponseEntity<ItemLocationDTO> getItemLocation(@PathVariable Long id) {
        log.debug("REST request to get ItemLocation : {}", id);
        Optional<ItemLocationDTO> itemLocationDTO = itemLocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemLocationDTO);
    }

    /**
     * {@code DELETE  /item-locations/:id} : delete the "id" itemLocation.
     *
     * @param id the id of the itemLocationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-locations/{id}")
    public ResponseEntity<Void> deleteItemLocation(@PathVariable Long id) {
        log.debug("REST request to delete ItemLocation : {}", id);
        itemLocationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/item-locations?query=:query} : search for the itemLocation corresponding
     * to the query.
     *
     * @param query the query of the itemLocation search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/item-locations")
    public ResponseEntity<List<ItemLocationDTO>> searchItemLocations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ItemLocations for query {}", query);
        Page<ItemLocationDTO> page = itemLocationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
