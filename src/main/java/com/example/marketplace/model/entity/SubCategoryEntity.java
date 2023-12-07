package com.example.marketplace.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "subcategories")
public class SubCategoryEntity extends BaseEntity {

    private String name;
    @OneToMany
    private List<ProductEntity> products;


    public String getName() {
        return name;
    }

    public SubCategoryEntity setName(String name) {
        this.name = name;
        return this;
    }

    public List<ProductEntity> getProducts() {
        return products;
    }

    public SubCategoryEntity setProducts(List<ProductEntity> products) {
        this.products = products;
        return this;
    }
}
