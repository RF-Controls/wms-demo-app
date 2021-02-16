package com.rfcontrols.wms.service.dto;

import java.time.Instant;
import javax.persistence.Lob;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.rfcontrols.wms.domain.ItemLocation} entity.
 */
public class ItemLocationDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant lastLocationUpdate;

    private Instant zoneEnterInstant;

    private String description;

    @NotNull
    private Double x;

    @NotNull
    private Double y;

    @NotNull
    private Double z;


    private Long itemId;
    private String itemName;

    private Long zoneId;
    private String zoneName;
    private String epc;
    @Lob
    private byte[] detailImage;

    private String detailImageContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getLastLocationUpdate() {
        return lastLocationUpdate;
    }

    public void setLastLocationUpdate(Instant lastLocationUpdate) {
        this.lastLocationUpdate = lastLocationUpdate;
    }

    public Instant getZoneEnterInstant() {
        return zoneEnterInstant;
    }

    public void setZoneEnterInstant(Instant zoneEnterInstant) {
        this.zoneEnterInstant = zoneEnterInstant;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    public byte[] getDetailImage() {
        return detailImage;
    }

    public void setDetailImage(byte[] detailImage) {
        this.detailImage = detailImage;
    }

    public String getDetailImageContentType() {
        return detailImageContentType;
    }

    public void setDetailImageContentType(String detailImageContentType) {
        this.detailImageContentType = detailImageContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemLocationDTO)) {
            return false;
        }

        return id != null && id.equals(((ItemLocationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemLocationDTO{" +
            "id=" + getId() +
            ", lastLocationUpdate='" + getLastLocationUpdate() + "'" +
            ", zoneEnterInstant='" + getZoneEnterInstant() + "'" +
            ", description='" + getDescription() + "'" +
            ", x=" + getX() +
            ", y=" + getY() +
            ", z=" + getZ() +
            ", itemId=" + getItemId() +
            ", zoneId=" + getZoneId() +
            "}";
    }
}
