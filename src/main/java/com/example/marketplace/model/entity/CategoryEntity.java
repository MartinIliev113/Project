package com.example.marketplace.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class CategoryEntity extends BaseEntity {

    public CategoryEntity() {
        subCategories = new ArrayList<>();
    }

    @Column(nullable = false)
    private String name;
    @OneToMany
    private List<SubCategoryEntity> subCategories;
    @OneToMany
    private List<ProductEntity> products;

    public List<SubCategoryEntity> getSubCategories() {
        return subCategories;
    }

    public CategoryEntity setSubCategories(List<SubCategoryEntity> subCategories) {
        this.subCategories = subCategories;
        return this;
    }

    public String getName() {
        return name;
    }

    public CategoryEntity setName(String name) {
        this.name = name;
        return this;
    }

    public List<ProductEntity> getProducts() {
        return products;
    }

    public CategoryEntity setProducts(List<ProductEntity> products) {
        this.products = products;
        return this;
    }
}
