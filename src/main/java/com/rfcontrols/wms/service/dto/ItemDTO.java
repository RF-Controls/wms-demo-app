package com.rfcontrols.wms.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.rfcontrols.wms.domain.Item} entity.
 */
public class ItemDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String epc;

    @NotNull
    private String description;

    private String description2;

    private Double weight;

    @Lob
    private byte[] thumbnail;

    private String thumbnailContentType;
    @Lob
    private byte[] detailImage;

    private String detailImageContentType;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnailContentType() {
        return thumbnailContentType;
    }

    public void setThumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
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
        if (!(o instanceof ItemDTO)) {
            return false;
        }

        return id != null && id.equals(((ItemDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", epc='" + getEpc() + "'" +
            ", description='" + getDescription() + "'" +
            ", description2='" + getDescription2() + "'" +
            ", weight=" + getWeight() +
            ", thumbnail='" + getThumbnail() + "'" +
            ", detailImage='" + getDetailImage() + "'" +
            "}";
    }
}
