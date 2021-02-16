package com.rfcontrols.wms.service.mapper;


import com.rfcontrols.wms.domain.ItemLocation;
import com.rfcontrols.wms.service.dto.ItemLocationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link ItemLocation} and its DTO {@link ItemLocationDTO}.
 *
 * This brings over, among other things, the detail image contents from the item entity.
 */
@Mapper(componentModel = "spring", uses = {ItemMapper.class, ZoneMapper.class})
public interface ItemLocationWithItemDetailsMapper extends EntityMapper<ItemLocationDTO, ItemLocation> {

    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "item.name", target = "itemName")
    @Mapping(source = "item.epc", target = "epc")
    @Mapping(source = "zone.id", target = "zoneId")
    @Mapping(source = "zone.name", target = "zoneName")
    @Mapping(source = "item.detailImage", target = "detailImage")
    @Mapping(source = "item.detailImageContentType", target = "detailImageContentType")
    ItemLocationDTO toDto(ItemLocation itemLocation);

    @Mapping(source = "itemId", target = "item")
    @Mapping(source = "zoneId", target = "zone")
    ItemLocation toEntity(ItemLocationDTO itemLocationDTO);

    default ItemLocation fromId(Long id) {
        if (id == null) {
            return null;
        }
        ItemLocation itemLocation = new ItemLocation();
        itemLocation.setId(id);
        return itemLocation;
    }
}
