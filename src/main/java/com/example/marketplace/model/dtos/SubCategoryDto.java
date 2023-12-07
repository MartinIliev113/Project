package com.example.marketplace.model.dtos;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryDto {

    private String name;

    private List<String> subCategoriesNames;

    private List<ProductDTO> products;


    public SubCategoryDto() {
        products = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public SubCategoryDto setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getSubCategoriesNames() {
        return subCategoriesNames;
    }

    public SubCategoryDto setSubCategoriesNames(List<String> subCategoriesNames) {
        this.subCategoriesNames = subCategoriesNames;
        return this;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public SubCategoryDto setProducts(List<ProductDTO> products) {
        this.products = products;
        return this;
    }
}
