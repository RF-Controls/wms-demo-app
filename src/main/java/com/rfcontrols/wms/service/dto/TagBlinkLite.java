package com.rfcontrols.wms.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DTO object used to consume tag blink lite data from RFC OS.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagBlinkLite {

    private String regionId = "";
    private List<String> antennaIds = new ArrayList<>();
    private List<String> antennaNames = new ArrayList<>();
    private String tagID = "";
    private Double x = 0d;
    private Double y = 0d;
    private Double z = 0d;
    private Date locateTime = new Date();
    private Double speed = 0d;
    private Double rssi = 0d;
    private String zoneName = "";
    private String zoneUUID = "";
    private String locationMethod = "";
    private String polarity = "";
    private Double confidenceInterval = 0d;
    private Boolean tracking = false;

    public String getTagID() {
        return tagID;
    }

    public void setTagID(String tagID) {
        this.tagID = tagID;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getZ() {
        return z;
    }

    public void setZ(Double z) {
        this.z = z;
    }

    public Date getLocateTime() {
        return locateTime;
    }

    public void setLocateTime(Date locateTime) {
        this.locateTime = locateTime;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getZoneUUID() {
        return zoneUUID;
    }

    public void setZoneUUID(String zoneUUID) {
        this.zoneUUID = zoneUUID;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public List<String> getAntennaIds() {
        return antennaIds;
    }

    public void setAntennaIds(List<String> antennaIds) {
        this.antennaIds = antennaIds;
    }

    public List<String> getAntennaNames() {
        return antennaNames;
    }

    public void setAntennaNames(List<String> antennaNames) {
        this.antennaNames = antennaNames;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getRssi() {
        return rssi;
    }

    public void setRssi(Double rssi) {
        this.rssi = rssi;
    }

    public String getLocationMethod() {
        return locationMethod;
    }

    public void setLocationMethod(String locationMethod) {
        this.locationMethod = locationMethod;
    }

    public String getPolarity() {
        return polarity;
    }

    public void setPolarity(String polarity) {
        this.polarity = polarity;
    }

    public Double getConfidenceInterval() {
        return confidenceInterval;
    }

    public void setConfidenceInterval(Double confidenceInterval) {
        this.confidenceInterval = confidenceInterval;
    }

    public Boolean getTracking() {
        return tracking;
    }

    public void setTracking(Boolean tracking) {
        this.tracking = tracking;
    }
}

