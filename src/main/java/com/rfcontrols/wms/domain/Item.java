package com.rfcontrols.wms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Item.
 */
@Entity
@Table(name = "item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "item")
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "epc", nullable = false)
    private String epc;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "description_2")
    private String description2;

    @Column(name = "weight")
    private Double weight;

    @Lob
    @Column(name = "thumbnail")
    private byte[] thumbnail;

    @Column(name = "thumbnail_content_type")
    private String thumbnailContentType;

    @Lob
    @Column(name = "detail_image")
    private byte[] detailImage;

    @Column(name = "detail_image_content_type")
    private String detailImageContentType;

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

    public Item name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEpc() {
        return epc;
    }

    public Item epc(String epc) {
        this.epc = epc;
        return this;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    public String getDescription() {
        return description;
    }

    public Item description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription2() {
        return description2;
    }

    public Item description2(String description2) {
        this.description2 = description2;
        return this;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public Double getWeight() {
        return weight;
    }

    public Item weight(Double weight) {
        this.weight = weight;
        return this;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public Item thumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnailContentType() {
        return thumbnailContentType;
    }

    public Item thumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
        return this;
    }

    public void setThumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
    }

    public byte[] getDetailImage() {
        return detailImage;
    }

    public Item detailImage(byte[] detailImage) {
        this.detailImage = detailImage;
        return this;
    }

    public void setDetailImage(byte[] detailImage) {
        this.detailImage = detailImage;
    }

    public String getDetailImageContentType() {
        return detailImageContentType;
    }

    public Item detailImageContentType(String detailImageContentType) {
        this.detailImageContentType = detailImageContentType;
        return this;
    }

    public void setDetailImageContentType(String detailImageContentType) {
        this.detailImageContentType = detailImageContentType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        return id != null && id.equals(((Item) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Item{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", epc='" + getEpc() + "'" +
            ", description='" + getDescription() + "'" +
            ", description2='" + getDescription2() + "'" +
            ", weight=" + getWeight() +
            ", thumbnail='" + getThumbnail() + "'" +
            ", thumbnailContentType='" + getThumbnailContentType() + "'" +
            ", detailImage='" + getDetailImage() + "'" +
            ", detailImageContentType='" + getDetailImageContentType() + "'" +
            "}";
    }
}
