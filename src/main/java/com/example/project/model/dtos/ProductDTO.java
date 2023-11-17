package com.example.project.model.dtos;


import com.example.project.model.validation.FileValidation;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class ProductDTO {

    public ProductDTO() {
        comments=new ArrayList<>();
    }

    private Long id;

    public Long getId() {
        return id;
    }

    public ProductDTO setId(Long id) {
        this.id = id;
        return this;
    }

    private String title;
    @Positive
    private BigDecimal price;

    private String subCategory;

    private String category;
    @Size(min = 3, max = 1000)
    private String description;
    private String ownerUsername;


    //@FileValidation(contentTypes = {"image/png", "image/jpeg"},message = "Select an image")
    private List<MultipartFile> images;

    private MultipartFile primaryImage;
    private String primaryImageUrl;

    public List<String> imagesUrls;

    private List<CommentDTO> comments;

    public List<CommentDTO> getComments() {
        return comments;
    }

    public ProductDTO setComments(List<CommentDTO> comments) {
        this.comments = comments;
        return this;
    }

    public List<String> getImagesUrls() {
        return imagesUrls;
    }

    public ProductDTO setImagesUrls(List<String> imagesUrls) {
        this.imagesUrls = imagesUrls;
        return this;
    }

    public List<MultipartFile> getImages() {
        return images;
    }

    public ProductDTO setImages(List<MultipartFile> images) {
        this.images = images;
        return this;
    }

    public MultipartFile getPrimaryImage() {
        return primaryImage;
    }

    public ProductDTO setPrimaryImage(MultipartFile primaryImage) {
        this.primaryImage = primaryImage;
        return this;
    }

    public String getPrimaryImageUrl() {
        return primaryImageUrl;
    }

    public ProductDTO setPrimaryImageUrl(String primaryImageUrl) {
        this.primaryImageUrl = primaryImageUrl;
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
