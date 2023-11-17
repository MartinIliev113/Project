package com.example.project.model.dtos;

public class CommentDTO {

    private Long productId;
    private String textContent;
    private String authorUsername;
    private Long id;

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
