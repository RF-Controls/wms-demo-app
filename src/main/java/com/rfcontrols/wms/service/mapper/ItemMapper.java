package com.rfcontrols.wms.service.mapper;


import com.rfcontrols.wms.domain.*;
import com.rfcontrols.wms.service.dto.ItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Item} and its DTO {@link ItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ItemMapper extends EntityMapper<ItemDTO, Item> {


    @Mapping(target = "detailImage", ignore = true)
    ItemDTO toDto(Item item);

    default Item fromId(Long id) {
        if (id == null) {
            return null;
        }
        Item item = new Item();
        item.setId(id);
        return item;
    }
}
