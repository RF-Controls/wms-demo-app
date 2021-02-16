package com.rfcontrols.wms.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.rfcontrols.wms.bo.TagWindowBO;
import com.rfcontrols.wms.bo.ZoneBO;
import com.rfcontrols.wms.domain.Item;
import com.rfcontrols.wms.domain.Zone;
import com.rfcontrols.wms.repository.ItemRepository;
import com.rfcontrols.wms.repository.ZoneRepository;
import com.rfcontrols.wms.service.ItemLocationService;
import com.rfcontrols.wms.service.ZoneAssociationService;
import com.rfcontrols.wms.service.dto.AssociationZoneEntryEvent;
import com.rfcontrols.wms.service.dto.ItemLocationDTO;
import com.rfcontrols.wms.service.dto.ItemLocationTrackerEvent;
import com.rfcontrols.wms.service.dto.TagBlinkLite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.vecmath.Point3d;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ZoneAssociationServiceImpl implements ZoneAssociationService, ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ZoneAssociationServiceImpl.class);
    private static final double METERS_TO_FEET = 3.28084;

    private final ZoneRepository zoneRepository;
    private final ItemLocationService itemLocationService;
    private final ItemRepository itemRepository;
    private final Executor executor;
    private final SimpMessagingTemplate template;

    private List<Zone> zones;
    private List<ZoneBO> boundingBoxes;

    /**
     * Zones used by the user to find EPC codes for the creation of new items
     */
    private List<ZoneBO> associationZones;
    private final Cache<String, TagWindowBO> tagWindowBOCache;

    public ZoneAssociationServiceImpl(ZoneRepository zoneRepository, ItemLocationService itemLocationService, ItemRepository itemRepository, SimpMessagingTemplate template,
                                      @Qualifier("taskExecutor") Executor taskExecutor, @Value("${wms.zone-service.tag-window-ttl-days}") Long tagWindowTTL) {
        this.zoneRepository = zoneRepository;
        this.itemLocationService = itemLocationService;
        this.itemRepository = itemRepository;
        this.template = template;
        this.executor = taskExecutor;
        this.tagWindowBOCache = CacheBuilder.newBuilder().expireAfterAccess(tagWindowTTL, TimeUnit.DAYS).build();
    }

    @Override
    public void updateLocations(List<TagBlinkLite> lites) {
        lites.stream().forEach(lite -> {
            try {
                convertToFeet(lite);
                TagWindowBO tagWindow = tagWindowBOCache.get(lite.getTagID(), () -> new TagWindowBO(lite.getTagID()));
                tagWindow.updateLocation(lite);
                Optional<Point3d> locationOptional = tagWindow.getBestLocation();
                if(!locationOptional.isPresent()) return;

                //See if we are inside any zones
                handleItemLocationUpdate(lite, locationOptional.get());

                //See if we are in any association zones
                processAssociationZones(lite, locationOptional.get());

            } catch (ExecutionException e) {
                logger.error("Error in the zone association service", e);
            }
        });
    }

    private void processAssociationZones(TagBlinkLite lite, Point3d point3d) {
        executor.execute(() -> {
            try{
                logger.debug("Searching for zone association for tag {} at point {},{},{}", lite.getTagID(), point3d.getX(), point3d.getY(), point3d.getZ());
                Optional<ZoneBO> foundAssociationZone = associationZones.stream().filter(zoneBO -> zoneBO.intersectsZone(point3d)).findFirst();
                if(foundAssociationZone.isPresent()){
                    logger.debug("found the tag {} in association zone {}", lite.getTagID(), foundAssociationZone.get().getZone().getName());
                    this.template.convertAndSend("/topic/associationZoneEntry", new AssociationZoneEntryEvent(lite.getTagID()));
                }
            }catch (Throwable throwable){
                logger.error("Error in processAssociationZones", throwable);
            }
        });
    }


    private void handleItemLocationUpdate(TagBlinkLite lite, Point3d point3d) {
        executor.execute(() -> {
            try{
                Optional<Item> item = itemRepository.findByEpc(lite.getTagID());
                if(!item.isPresent()){
                    logger.trace("Item not present for EPC {}", lite.getTagID());
                    return;
                }
                Optional<ItemLocationDTO> itemLocationOptional = itemLocationService.findByEpc(lite.getTagID());

                ItemLocationDTO itemLocation = itemLocationOptional.isPresent() ? itemLocationOptional.get() : new ItemLocationDTO();
                itemLocation.setItemId(item.get().getId());
                itemLocation.setLastLocationUpdate(Instant.now());

                updateZone(lite.getTagID(), itemLocation, point3d);

                itemLocation.setX(point3d.getX());
                itemLocation.setY(point3d.getY());
                itemLocation.setZ(point3d.getZ());

                itemLocationService.save(itemLocation);

                ItemLocationTrackerEvent trackerEvent = new ItemLocationTrackerEvent();
                trackerEvent.setEpc(lite.getTagID());
                trackerEvent.setX(itemLocation.getX());
                trackerEvent.setY(itemLocation.getY());
                trackerEvent.setZ(itemLocation.getZ());
                trackerEvent.setUpdateTime(lite.getLocateTime().toInstant());
                trackerEvent.setZoneName(itemLocation.getZoneName());
                this.template.convertAndSend("/topic/itemLocationUpdate." + lite.getTagID(), trackerEvent);
            }catch (Throwable throwable){
                logger.error("Error handling a single zone intersection", throwable);
            }
        });
    }

    private void convertToFeet(TagBlinkLite lite) {
        lite.setX(lite.getX() * METERS_TO_FEET);
        lite.setY(lite.getY() * METERS_TO_FEET);
        lite.setZ(lite.getZ() * METERS_TO_FEET);
    }

    private void updateZone(String tagID, ItemLocationDTO itemLocation, Point3d point3d) {
        List<ZoneBO> intersectingZones = boundingBoxes.stream().filter(zoneBO -> zoneBO.intersectsZone(point3d)).collect(Collectors.toList());

        if(intersectingZones.size() == 0){
            itemLocation.setZoneId(null);
        }else {
            Long zoneId = intersectingZones.get(0).getZone().getId();
            if(intersectingZones.size() == 1){
                logger.debug("Tag {} was found to be in zone: {}", tagID, zoneId);
                itemLocation.setZoneId(zoneId);
            }else if(intersectingZones.size() > 1){
                //TODO add some logic here around picking the closest zone or something
                logger.debug("Tag {} was found to be in zones: {}", tagID, zoneId);
                itemLocation.setZoneId(zoneId);
            }
        }
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        loadZones();
    }

    public void loadZones() {
        zones = zoneRepository.findAll();
        this.boundingBoxes = zones.stream().map(zone -> new ZoneBO(zone)).collect(Collectors.toList());
        this.associationZones = zones.stream().filter(zone -> zone.isAssociationZone()).map(zone -> new ZoneBO(zone)).collect(Collectors.toList());
    }
}
