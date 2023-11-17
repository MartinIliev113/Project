package com.example.project.repository;

import com.example.project.model.dtos.SearchProductDTO;
import com.example.project.model.entity.ProductEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification implements Specification<ProductEntity> {

    private final SearchProductDTO searchProductDTO;

    public ProductSpecification(SearchProductDTO searchProductDTO) {
        this.searchProductDTO = searchProductDTO;
    }

    @Override
    public Predicate toPredicate(Root<ProductEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();

        if (searchProductDTO.getTitle() != null && !searchProductDTO.getTitle().isEmpty()) {
            predicate = cb.and(predicate, cb.like(cb.lower(root.get("title")), "%" + searchProductDTO.getTitle().toLowerCase() + "%"));
        }


        if (searchProductDTO.getMinPrice() != null) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("price"), searchProductDTO.getMinPrice()));
        }

        if (searchProductDTO.getMaxPrice() != null) {
            predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("price"), searchProductDTO.getMaxPrice()));
        }

        return predicate;
    }
}

