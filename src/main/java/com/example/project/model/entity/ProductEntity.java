package com.example.project.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class ProductEntity extends BaseEntity {
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
