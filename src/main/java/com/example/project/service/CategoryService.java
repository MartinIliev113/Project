package com.example.project.service;

import com.example.project.model.dtos.CategoryDTO;
import com.example.project.model.dtos.ProductDTO;
import com.example.project.model.dtos.SubCategoryDto;
import com.example.project.model.entity.CategoryEntity;
import com.example.project.repository.CategoryRepository;
import com.example.project.repository.SubCategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ModelMapper modelMapper;

    public CategoryService(CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.modelMapper = modelMapper;
    }

    public List<CategoryDTO> getAllCategories() {
        List<CategoryEntity> all = categoryRepository.findAll();
        return categoryRepository.findAll().stream().map(categoryEntity -> {
            CategoryDTO categoryDto = modelMapper.map(categoryEntity, CategoryDTO.class);
            categoryDto.setProducts(categoryEntity.getProducts()
                    .stream()
                    .map(product -> {
                        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
                        productDTO.setCategory(product.getCategory().getName());
                        productDTO.setSubCategory(productDTO.getSubCategory()); ////todo
                        return productDTO;
                    })
                    .toList());
            return categoryDto;
        }).toList();
    }


    public void addCategory(CategoryDTO categoryDTO) {
        categoryRepository.save(modelMapper.map(categoryDTO, CategoryEntity.class));
    }

    public List<SubCategoryDto> getSubCategories(Long id) {
        Optional<CategoryEntity> category = categoryRepository.findById(id);
        return category.map(categoryEntity -> categoryEntity.getSubCategories()
                .stream().map(subcategory -> modelMapper.map(subcategory, SubCategoryDto.class)).toList()).orElse(null); //todo exception
    }

    public String getCategoryName(Long id) {
        return categoryRepository.findById(id).get().getName();
    }

    public List<SubCategoryDto> getAllSubCategories() {
        return subCategoryRepository.findAll().stream().map(subCategory ->
                modelMapper.map(subCategory, SubCategoryDto.class)).toList();
    }
}
