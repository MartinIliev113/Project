package com.example.marketplace.service;

import com.example.marketplace.model.dtos.CategoryDTO;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SubCategoryRepository subCategoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testGetSubCategories() {
        Long categoryId = 1L;
        CategoryEntity categoryEntity = new CategoryEntity();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryEntity));

        List<SubCategoryDto> result = categoryService.getSubCategories(categoryId);

        assertNotNull(result);
    }

    @Test
    public void testGetCategoryName() {
        Long categoryId = 1L;
        String categoryName = "TestCategory";
        CategoryEntity categoryEntity = new CategoryEntity().setName(categoryName);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryEntity));

        String result = categoryService.getCategoryName(categoryId);

        assertEquals(categoryName, result);
    }

    @Test
    public void testGetAllSubCategories() {
        List<SubCategoryEntity> subCategoryEntities = Arrays.asList(new SubCategoryEntity(), new SubCategoryEntity());
        when(subCategoryRepository.findAll()).thenReturn(subCategoryEntities);

        List<SubCategoryDto> result = categoryService.getAllSubCategories();

        assertNotNull(result);
    }

    @Test
    public void testCheckCategory() {
        String categoryName = "TestCategory";
        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.of(new CategoryEntity()));

        boolean result = categoryService.checkCategory(categoryName);

        assertTrue(result);
    }

    @Test
    public void testDeleteCategory() {
        String categoryName = "TestCategory";
        CategoryEntity categoryEntity = new CategoryEntity().setName(categoryName);
        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.of(categoryEntity));

        assertDoesNotThrow(() -> categoryService.deleteCategory(categoryName));
        Mockito.verify(categoryRepository, Mockito.times(1)).delete(categoryEntity);
    }
}
