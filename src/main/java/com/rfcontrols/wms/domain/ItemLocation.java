package com.rfcontrols.wms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

/**
 * A ItemLocation.
 */
@Entity
@Table(name = "item_location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "itemlocation")
public class ItemLocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "last_location_update", nullable = false)
    private Instant lastLocationUpdate;

    @Column(name = "zone_enter_instant")
    private Instant zoneEnterInstant;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "x", nullable = false)
    private Double x;

    @NotNull
    @Column(name = "y", nullable = false)
    private Double y;

    @NotNull
    @Column(name = "z", nullable = false)
    private Double z;

    @OneToOne
    @JoinColumn(unique = true)
    private Item item;

    @ManyToOne
    @JsonIgnoreProperties(value = "epcs", allowSetters = true)
    private Zone zone;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getLastLocationUpdate() {
        return lastLocationUpdate;
    }

    public ItemLocation lastLocationUpdate(Instant lastLocationUpdate) {
        this.lastLocationUpdate = lastLocationUpdate;
        return this;
    }

    public void setLastLocationUpdate(Instant lastLocationUpdate) {
        this.lastLocationUpdate = lastLocationUpdate;
    }

    public Instant getZoneEnterInstant() {
        return zoneEnterInstant;
    }

    public ItemLocation zoneEnterInstant(Instant zoneEnterInstant) {
        this.zoneEnterInstant = zoneEnterInstant;
        return this;
    }

    public void setZoneEnterInstant(Instant zoneEnterInstant) {
        this.zoneEnterInstant = zoneEnterInstant;
    }

    public String getDescription() {
        return description;
    }

    public ItemLocation description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getX() {
        return x;
    }

    public ItemLocation x(Double x) {
        this.x = x;
        return this;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public ItemLocation y(Double y) {
        this.y = y;
        return this;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getZ() {
        return z;
    }

    public ItemLocation z(Double z) {
        this.z = z;
        return this;
    }

    public void setZ(Double z) {
        this.z = z;
    }

    public Item getItem() {
        return item;
    }

    public ItemLocation item(Item item) {
        this.item = item;
        return this;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Zone getZone() {
        return zone;
    }

    public ItemLocation zone(Zone zone) {
        this.zone = zone;
        return this;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemLocation)) {
            return false;
        }
        return id != null && id.equals(((ItemLocation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemLocation{" +
            "id=" + getId() +
            ", lastLocationUpdate='" + getLastLocationUpdate() + "'" +
            ", zoneEnterInstant='" + getZoneEnterInstant() + "'" +
            ", description='" + getDescription() + "'" +
            ", x=" + getX() +
            ", y=" + getY() +
            ", z=" + getZ() +
            "}";
    }
}
