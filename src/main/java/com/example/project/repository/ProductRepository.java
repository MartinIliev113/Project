package com.example.project.repository;

import com.example.project.model.entity.CategoryEntity;
import com.example.project.model.entity.ProductEntity;
import com.example.project.model.entity.SubCategoryEntity;
import com.example.project.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Long>,
        JpaSpecificationExecutor<ProductEntity> {
    List<ProductEntity> findAllByCategory(CategoryEntity category);
    List<ProductEntity> findAllBySubCategory(SubCategoryEntity byName);
    List<ProductEntity> findAll(Specification<ProductEntity> spec);
    List<ProductEntity> findAllByOwner(UserEntity userEntity);


    Page<ProductEntity> findAllByCategory(CategoryEntity category, Pageable pageable);

    Page<ProductEntity> findAllBySubCategory(SubCategoryEntity byName, Pageable pageable);

}
