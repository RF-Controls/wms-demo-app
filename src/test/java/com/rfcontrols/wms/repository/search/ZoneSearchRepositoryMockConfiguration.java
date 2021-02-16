package com.rfcontrols.wms.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ZoneSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ZoneSearchRepositoryMockConfiguration {

    @MockBean
    private ZoneSearchRepository mockZoneSearchRepository;

}
