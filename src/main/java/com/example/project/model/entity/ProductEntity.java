package com.example.project.model.entity;

import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class ProductEntity extends BaseEntity {

    private String  primaryImageUrl;

    public String getPrimaryImageUrl() {
        return primaryImageUrl;
    }

    public ProductEntity setPrimaryImageUrl(String primaryImageUrl) {
        this.primaryImageUrl = primaryImageUrl;
        return this;
    }

    @Column(name = "image_url")
    @ElementCollection
    private List<String> imageUrls;

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public ProductEntity setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
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

    @OneToMany
    private List<CommentEntity> comments;
    private LocalDateTime addedOn;

    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public ProductEntity setAddedOn(LocalDateTime addedOn) {
        this.addedOn = addedOn;
        return this;
    }

    public ProductEntity() {
        this.comments=new ArrayList<>();
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public ProductEntity setComments(List<CommentEntity> comments) {
        this.comments = comments;
        return this;
    }

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
