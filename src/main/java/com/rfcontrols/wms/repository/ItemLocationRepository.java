package com.rfcontrols.wms.repository;

import com.rfcontrols.wms.domain.ItemLocation;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the ItemLocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemLocationRepository extends JpaRepository<ItemLocation, Long> {

    Optional<ItemLocation> findByItemEpc(String epc);
}
