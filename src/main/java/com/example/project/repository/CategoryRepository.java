package com.example.project.repository;

import com.example.project.model.entity.CategoryEntity;
import com.example.project.model.entity.SubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity,Long> {
    CategoryEntity findByName(String name);
    CategoryEntity findBySubCategoriesContaining(SubCategoryEntity category);
}
