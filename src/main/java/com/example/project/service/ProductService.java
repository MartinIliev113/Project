package com.example.project.service;

import com.example.project.model.AppUserDetails;
import com.example.project.model.dtos.ProductDTO;
import com.example.project.model.entity.CategoryEntity;
import com.example.project.model.entity.ProductEntity;
import com.example.project.model.entity.SubCategoryEntity;
import com.example.project.repository.CategoryRepository;
import com.example.project.repository.ProductRepository;
import com.example.project.repository.SubCategoryRepository;
import com.example.project.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final SubCategoryRepository subCategoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, UserRepository userRepository, ModelMapper modelMapper, SubCategoryRepository subCategoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.subCategoryRepository = subCategoryRepository;
    }

    public void addProduct(ProductDTO productDTO, AppUserDetails userDetails) {
        ProductEntity product = modelMapper.map(productDTO, ProductEntity.class);
        SubCategoryEntity subCategory = subCategoryRepository.findByName(productDTO.getSubCategory());
        CategoryEntity category=categoryRepository.findBySubCategoriesContaining(subCategory);
        product.setCategory(category);
        product.setSubCategory(subCategory);
        product.setOwner(userRepository.findById(userDetails.getId()).get());//TODO FIX
        category.getProducts().add(product);
        subCategory.getProducts().add(product);
        productRepository.save(product);
    }


}
