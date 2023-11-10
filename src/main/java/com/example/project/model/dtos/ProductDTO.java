package com.example.project.model.dtos;

import com.example.project.model.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


import java.math.BigDecimal;
import java.util.List;


public class ProductDTO {

    private String title;
    @Positive
    private BigDecimal price;

    private String subCategory;

    private String category;
    @Size(min = 3, max = 1000)
    private String description;
    private String ownerUsername;

    private List<String> imageUrls;

    // Getter and setter for imageUrls
    public List<String> getImageUrls() {
        return imageUrls;
    }

    public ProductDTO setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
        return this;
    }



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

    public String getTitle() {
        return title;
    }

    public ProductDTO setTitle(String title) {
        this.title = title;
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
