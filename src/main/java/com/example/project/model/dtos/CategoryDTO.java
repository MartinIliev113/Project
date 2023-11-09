package com.example.project.model.dtos;



import jakarta.validation.constraints.Size;

import java.util.List;



public class CategoryDTO {

    private Long id;
    @Size(min = 3)
    private String name;
    private List<ProductDTO> products;
    private List<SubCategoryDto> subCategories;

    public Long getId() {
        return id;
    }

    public CategoryDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CategoryDTO setName(String name) {
        this.name = name;
        return this;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public CategoryDTO setProducts(List<ProductDTO> products) {
        this.products = products;
        return this;
    }

    public List<SubCategoryDto> getSubCategories() {
        return subCategories;
    }

    public CategoryDTO setSubCategories(List<SubCategoryDto> subCategories) {
        this.subCategories = subCategories;
        return this;
    }
}
