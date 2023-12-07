package com.example.marketplace.repository;


import com.example.marketplace.model.entity.CategoryEntity;
import com.example.marketplace.model.entity.SubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByName(String name);

    CategoryEntity findBySubCategoriesContaining(SubCategoryEntity category);
}
