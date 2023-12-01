package com.example.project.model.dtos;

import java.time.LocalDateTime;

public class CommentDTO {

    private Long productId;
    private String textContent;
    private String authorUsername;
    private Long id;
    private LocalDateTime created;

    public LocalDateTime getCreated() {
        return created;
    }

    public CommentDTO setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }

    public Long getId() {
        return id;
    }

    public CommentDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public CommentDTO setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
        return this;
    }

    public Long getProductId() {
        return productId;
    }

    public CommentDTO setProductId(Long productId) {
        this.productId = productId;
        return this;
    }

    public String getTextContent() {
        return textContent;
    }

    public CommentDTO setTextContent(String textContent) {
        this.textContent = textContent;
        return this;
    }

}
