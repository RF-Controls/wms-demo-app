package com.rfcontrols.wms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Zone.
 */
@Entity
@Table(name = "zone")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "zone")
public class Zone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "max_items")
    private Integer maxItems;

    @NotNull
    @Column(name = "x_1", nullable = false)
    private Double x1;

    @NotNull
    @Column(name = "y_1", nullable = false)
    private Double y1;

    @NotNull
    @Column(name = "z_1", nullable = false)
    private Double z1;

    @NotNull
    @Column(name = "x_2", nullable = false)
    private Double x2;

    @NotNull
    @Column(name = "y_2", nullable = false)
    private Double y2;

    @NotNull
    @Column(name = "z_2", nullable = false)
    private Double z2;

    @NotNull
    @Column(name = "association_zone", nullable = false)
    private Boolean associationZone;

    @OneToMany(mappedBy = "zone")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ItemLocation> epcs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Zone name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Zone description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxItems() {
        return maxItems;
    }

    public Zone maxItems(Integer maxItems) {
        this.maxItems = maxItems;
        return this;
    }

    public void setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
    }

    public Double getx1() {
        return x1;
    }

    public Zone x1(Double x1) {
        this.x1 = x1;
        return this;
    }

    public void setx1(Double x1) {
        this.x1 = x1;
    }

    public Double gety1() {
        return y1;
    }

    public Zone y1(Double y1) {
        this.y1 = y1;
        return this;
    }

    public void sety1(Double y1) {
        this.y1 = y1;
    }

    public Double getz1() {
        return z1;
    }

    public Zone z1(Double z1) {
        this.z1 = z1;
        return this;
    }

    public void setz1(Double z1) {
        this.z1 = z1;
    }

    public Double getx2() {
        return x2;
    }

    public Zone x2(Double x2) {
        this.x2 = x2;
        return this;
    }

    public void setx2(Double x2) {
        this.x2 = x2;
    }

    public Double gety2() {
        return y2;
    }

    public Zone y2(Double y2) {
        this.y2 = y2;
        return this;
    }

    public void sety2(Double y2) {
        this.y2 = y2;
    }

    public Double getz2() {
        return z2;
    }

    public Zone z2(Double z2) {
        this.z2 = z2;
        return this;
    }

    public void setz2(Double z2) {
        this.z2 = z2;
    }

    public Boolean isAssociationZone() {
        return associationZone;
    }

    public Zone associationZone(Boolean associationZone) {
        this.associationZone = associationZone;
        return this;
    }

    public void setAssociationZone(Boolean associationZone) {
        this.associationZone = associationZone;
    }

    public Set<ItemLocation> getEpcs() {
        return epcs;
    }

    public Zone epcs(Set<ItemLocation> itemLocations) {
        this.epcs = itemLocations;
        return this;
    }

    public Zone addEpc(ItemLocation itemLocation) {
        this.epcs.add(itemLocation);
        itemLocation.setZone(this);
        return this;
    }

    public Zone removeEpc(ItemLocation itemLocation) {
        this.epcs.remove(itemLocation);
        itemLocation.setZone(null);
        return this;
    }

    public void setEpcs(Set<ItemLocation> itemLocations) {
        this.epcs = itemLocations;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Zone)) {
            return false;
        }
        return id != null && id.equals(((Zone) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Zone{" +
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
