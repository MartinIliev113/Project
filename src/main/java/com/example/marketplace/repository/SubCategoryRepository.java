package com.example.marketplace.repository;


import com.example.marketplace.model.entity.SubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategoryEntity, Long> {
    Optional<SubCategoryEntity> findByName(String category);
}
