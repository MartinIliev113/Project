package com.example.project.model.dtos;

import com.example.project.model.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


import java.math.BigDecimal;



public class ProductDTO {

    @Positive
    private BigDecimal price;

    private String subCategory;

    private String category;
    @Size(min = 3, max = 1000)
    private String description;
    private String ownerUsername;


    public BigDecimal getPrice() {
        return price;
    }

    public ProductDTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public ProductDTO setSubCategory(String subCategory) {
        this.subCategory = subCategory;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public ProductDTO setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public ProductDTO setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
        return this;
    }
}
