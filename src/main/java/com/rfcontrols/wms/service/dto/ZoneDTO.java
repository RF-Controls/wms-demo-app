package com.rfcontrols.wms.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.rfcontrols.wms.domain.Zone} entity.
 */
public class ZoneDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String name;

    private String description;

    private Integer maxItems;

    @NotNull
    private Double x1;

    @NotNull
    private Double y1;

    @NotNull
    private Double z1;

    @NotNull
    private Double x2;

    @NotNull
    private Double y2;

    @NotNull
    private Double z2;

    @NotNull
    private Boolean associationZone;

    
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
    }

    public Double getx1() {
        return x1;
    }

    public void setx1(Double x1) {
        this.x1 = x1;
    }

    public Double gety1() {
        return y1;
    }

    public void sety1(Double y1) {
        this.y1 = y1;
    }

    public Double getz1() {
        return z1;
    }

    public void setz1(Double z1) {
        this.z1 = z1;
    }

    public Double getx2() {
        return x2;
    }

    public void setx2(Double x2) {
        this.x2 = x2;
    }

    public Double gety2() {
        return y2;
    }

    public void sety2(Double y2) {
        this.y2 = y2;
    }

    public Double getz2() {
        return z2;
    }

    public void setz2(Double z2) {
        this.z2 = z2;
    }

    public Boolean isAssociationZone() {
        return associationZone;
    }

    public void setAssociationZone(Boolean associationZone) {
        this.associationZone = associationZone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ZoneDTO)) {
            return false;
        }

        return id != null && id.equals(((ZoneDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ZoneDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", maxItems=" + getMaxItems() +
            ", x1=" + getx1() +
            ", y1=" + gety1() +
            ", z1=" + getz1() +
            ", x2=" + getx2() +
            ", y2=" + gety2() +
            ", z2=" + getz2() +
            ", associationZone='" + isAssociationZone() + "'" +
            "}";
    }
}
