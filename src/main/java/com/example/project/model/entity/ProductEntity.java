package com.example.project.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class ProductEntity extends BaseEntity {

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageEntity> images = new ArrayList<>();

    // Getter and setter for images
    public List<ImageEntity> getImages() {
        return images;
    }

    public ProductEntity setImages(List<ImageEntity> images) {
        this.images = images;
        return this;
    }
    private String title;
    @Column(nullable = false)
    private BigDecimal price;
    @ManyToOne
    private CategoryEntity category;
    @ManyToOne
    private SubCategoryEntity subCategoryEntity;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    private UserEntity owner;

    public String getTitle() {
        return title;
    }

    public ProductEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public ProductEntity setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public SubCategoryEntity getSubCategoryEntity() {
        return subCategoryEntity;
    }

    public ProductEntity setSubCategory(SubCategoryEntity subCategoryEntity) {
        this.subCategoryEntity = subCategoryEntity;
        return this;
    }

    public ProductEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public ProductEntity setOwner(UserEntity owner) {
        this.owner = owner;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public ProductEntity setCategory(CategoryEntity category) {
        this.category = category;
        return this;
    }
}
