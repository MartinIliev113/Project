package com.example.project.model.dtos;


import com.example.project.model.validation.FileValidation;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;


import java.math.BigDecimal;



public class ProductDTO {

    private String title;
    @Positive
    private BigDecimal price;

    private String subCategory;

    private String category;
    @Size(min = 3, max = 1000)
    private String description;
    private String ownerUsername;


    @FileValidation(contentTypes = {"image/png", "image/jpeg"},message = "Select an image")
    private MultipartFile image;

    public MultipartFile getImage() {
        return image;
    }

    public ProductDTO setImage(MultipartFile image) {
        this.image = image;
        return this;
    }
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public ProductDTO setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
