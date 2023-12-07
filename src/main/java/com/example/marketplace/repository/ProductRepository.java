package com.example.marketplace.repository;


import com.example.marketplace.model.entity.CategoryEntity;
import com.example.marketplace.model.entity.ProductEntity;
import com.example.marketplace.model.entity.SubCategoryEntity;
import com.example.marketplace.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>,
        JpaSpecificationExecutor<ProductEntity> {
    List<ProductEntity> findAllByCategory(CategoryEntity category);

    List<ProductEntity> findAllBySubCategory(SubCategoryEntity byName);

    List<ProductEntity> findAll(Specification<ProductEntity> spec);

    List<ProductEntity> findAllByOwner(UserEntity userEntity);


    Page<ProductEntity> findAllByCategory(CategoryEntity category, Pageable pageable);

    Page<ProductEntity> findAllBySubCategory(SubCategoryEntity byName, Pageable pageable);

}
