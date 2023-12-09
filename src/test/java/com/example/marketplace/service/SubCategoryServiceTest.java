package com.example.marketplace.service;

import com.example.marketplace.model.dtos.SubCategoryDto;
import com.example.marketplace.model.entity.CategoryEntity;
import com.example.marketplace.model.entity.SubCategoryEntity;
import com.example.marketplace.repository.CategoryRepository;
import com.example.marketplace.repository.SubCategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SubCategoryServiceTest {

    @InjectMocks
    private SubCategoryService subCategoryService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private SubCategoryRepository subCategoryRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    public void testAddSubcategory() {
        Long categoryId = 1L;
        SubCategoryDto subCategoryDto = new SubCategoryDto();
        CategoryEntity categoryEntity = new CategoryEntity();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryEntity));
        when(modelMapper.map(subCategoryDto, SubCategoryEntity.class)).thenReturn(new SubCategoryEntity());

        assertDoesNotThrow(() -> subCategoryService.addSubcategory(subCategoryDto, categoryId));

        Mockito.verify(subCategoryRepository, Mockito.times(1)).save(Mockito.any(SubCategoryEntity.class));
    }


    @Test
    public void testDeleteSubCategory() {
        String subCategoryName = "TestSubCategory";
        SubCategoryEntity subCategoryEntity = new SubCategoryEntity().setName(subCategoryName);
        CategoryEntity categoryEntity = new CategoryEntity();
        when(subCategoryRepository.findByName(subCategoryName)).thenReturn(Optional.of(subCategoryEntity));
        when(categoryRepository.findBySubCategoriesContaining(subCategoryEntity)).thenReturn(categoryEntity);

        assertDoesNotThrow(() -> subCategoryService.deleteSubCategory(subCategoryName));
        Mockito.verify(subCategoryRepository, Mockito.times(1)).delete(subCategoryEntity);
        Mockito.verify(categoryRepository, Mockito.times(1)).save(categoryEntity);
    }
}
