package com.rfcontrols.wms.repository.search;

import com.rfcontrols.wms.domain.ItemLocation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link ItemLocation} entity.
 */
public interface ItemLocationSearchRepository extends ElasticsearchRepository<ItemLocation, Long> {
}
