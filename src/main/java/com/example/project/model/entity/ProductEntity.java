package com.example.project.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class ProductEntity extends BaseEntity {

    @Column(name = "image_url")
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public ProductEntity setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }


    private String title;
    @Column(nullable = false)
    private BigDecimal price;
    @ManyToOne
    private CategoryEntity category;
    @ManyToOne
    private SubCategoryEntity subCategory;

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

    public SubCategoryEntity getSubCategory() {
        return subCategory;
    }

    public ProductEntity setSubCategory(SubCategoryEntity subCategory) {
        this.subCategory = subCategory;
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
