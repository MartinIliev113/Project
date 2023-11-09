package com.example.project.repository;

import com.example.project.model.entity.SubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategoryEntity,Long> {
    SubCategoryEntity findByName(String category);
}
