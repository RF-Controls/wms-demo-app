package com.rfcontrols.wms.repository;

import com.rfcontrols.wms.domain.Item;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the Item entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByEpc(String epc);
}
