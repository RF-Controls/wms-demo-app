package com.rfcontrols.wms.service.mapper;


import com.rfcontrols.wms.domain.*;
import com.rfcontrols.wms.service.dto.ZoneDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Zone} and its DTO {@link ZoneDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ZoneMapper extends EntityMapper<ZoneDTO, Zone> {


    @Mapping(target = "epcs", ignore = true)
    @Mapping(target = "removeEpc", ignore = true)
    Zone toEntity(ZoneDTO zoneDTO);

    default Zone fromId(Long id) {
        if (id == null) {
            return null;
        }
        Zone zone = new Zone();
        zone.setId(id);
        return zone;
    }
}
