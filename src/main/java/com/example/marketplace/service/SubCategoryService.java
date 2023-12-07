package com.example.marketplace.service;


import com.example.marketplace.model.dtos.SubCategoryDto;
import com.example.marketplace.model.entity.CategoryEntity;
import com.example.marketplace.model.entity.SubCategoryEntity;
import com.example.marketplace.model.exceptions.ObjectNotFoundException;
import com.example.marketplace.repository.CategoryRepository;
import com.example.marketplace.repository.SubCategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static com.example.marketplace.model.exceptions.ExceptionMessages.CATEGORY_NOT_FOUND;

@Service
public class SubCategoryService {
    private final ModelMapper modelMapper;
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;

    public SubCategoryService(ModelMapper modelMapper, SubCategoryRepository subCategoryRepository, CategoryRepository categoryRepository) {
        this.modelMapper = modelMapper;
        this.subCategoryRepository = subCategoryRepository;
        this.categoryRepository = categoryRepository;
    }

    public void addSubcategory(SubCategoryDto subCategoryDto, Long id) {
        SubCategoryEntity subCategory = modelMapper.map(subCategoryDto, SubCategoryEntity.class);
        categoryRepository.findById(id).orElseThrow(()->new ObjectNotFoundException(CATEGORY_NOT_FOUND)).getSubCategories().add(subCategory);
        subCategoryRepository.save(subCategory);
    }

    public void deleteSubCategory(String subcategoryName) {
        SubCategoryEntity subCategoryEntity = subCategoryRepository.findByName(subcategoryName).orElseThrow(() -> new ObjectNotFoundException(CATEGORY_NOT_FOUND));
        CategoryEntity category = categoryRepository.findBySubCategoriesContaining(subCategoryEntity);
        category.getSubCategories().remove(subCategoryEntity);
        categoryRepository.save(category);
        subCategoryRepository.delete(subCategoryEntity);
    }
}
