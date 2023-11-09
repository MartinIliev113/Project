package com.example.project.service;

import com.example.project.model.dtos.SubCategoryDto;
import com.example.project.model.entity.SubCategoryEntity;
import com.example.project.repository.CategoryRepository;
import com.example.project.repository.SubCategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
    public void addSubcategory(SubCategoryDto subCategoryDto,Long id) {
        SubCategoryEntity subCategory = modelMapper.map(subCategoryDto, SubCategoryEntity.class);
        categoryRepository.findById(id).get().getSubCategories().add(subCategory);
        subCategoryRepository.save(subCategory);
    }

}
