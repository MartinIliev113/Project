package com.example.project.model.entity;

// Import statements

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "images")
public class ImageEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    // Additional fields for image properties (e.g., file name, content, etc.)

    // Getter and setter for product
    public ProductEntity getProduct() {
        return product;
    }

    public ImageEntity setProduct(ProductEntity product) {
        this.product = product;
        return this;
    }

    // Additional methods if needed
}
