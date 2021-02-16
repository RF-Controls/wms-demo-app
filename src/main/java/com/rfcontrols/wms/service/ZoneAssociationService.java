package com.rfcontrols.wms.service;


import com.rfcontrols.wms.service.dto.TagBlinkLite;

import java.util.List;

/**
 * Service to determine the best zone to put a tag within
 */
public interface ZoneAssociationService {
    void updateLocations(List<TagBlinkLite> lites);

    void loadZones();
}
