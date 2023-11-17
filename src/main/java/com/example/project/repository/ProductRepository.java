package com.example.project.repository;

import com.example.project.model.entity.CategoryEntity;
import com.example.project.model.entity.ProductEntity;
import com.example.project.model.entity.SubCategoryEntity;
import com.example.project.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Long> {
    List<ProductEntity> findAllByCategory(CategoryEntity category);
    List<ProductEntity> findAllBySubCategory(SubCategoryEntity byName);

    ProductEntity findByOwner(UserEntity author);
}
