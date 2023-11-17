package com.example.project.model.dtos;

public class SearchProductDTO {
    private String title;
    private Double minPrice;
    private Double maxPrice;

    public String getTitle() {
        return title;
    }

    public SearchProductDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public SearchProductDTO setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
        return this;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public SearchProductDTO setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
        return this;
    }
    public boolean isEmpty() {
        return (title == null || title.isEmpty()) &&
                minPrice == null &&
                maxPrice == null;
    }



}
