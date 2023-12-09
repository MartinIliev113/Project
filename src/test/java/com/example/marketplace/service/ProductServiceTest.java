package com.example.marketplace.service;

import com.example.marketplace.model.dtos.ProductDTO;
import com.example.marketplace.model.dtos.SearchProductDTO;
import com.example.marketplace.model.entity.CategoryEntity;
import com.example.marketplace.model.entity.SubCategoryEntity;
import com.example.marketplace.model.entity.UserEntity;
import com.example.marketplace.repository.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SubCategoryRepository subCategoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testGetAllByCategory() {
        String categoryName = "TestCategory";
        Pageable pageable = Mockito.mock(Pageable.class);
        CategoryEntity categoryEntity = new CategoryEntity().setName(categoryName);
        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.of(categoryEntity));
        when(productRepository.findAllByCategory(categoryEntity, pageable)).thenReturn(new PageImpl<>(Arrays.asList(/* list of ProductEntity */)));

        Page<ProductDTO> result = productService.getAllByCategory(categoryName, pageable);

        assertNotNull(result);
        // Add assertions based on the expected result
    }

    @Test
    public void testGetAllBySubCategory() {
        String subCategoryName = "TestSubCategory";
        Pageable pageable = Mockito.mock(Pageable.class);
        SubCategoryEntity subCategoryEntity = new SubCategoryEntity().setName(subCategoryName);
        when(subCategoryRepository.findByName(subCategoryName)).thenReturn(Optional.of(subCategoryEntity));
        when(productRepository.findAllBySubCategory(subCategoryEntity, pageable)).thenReturn(new PageImpl<>(Arrays.asList(/* list of ProductEntity */)));

        Page<ProductDTO> result = productService.getAllBySubCategory(subCategoryName, pageable);

        assertNotNull(result);
        // Add assertions based on the expected result
    }

    @Test
    public void testSearchOffer() {
        SearchProductDTO searchProductDTO = new SearchProductDTO(/* initialize with search criteria */);
        when(productRepository.findAll(new ProductSpecification(searchProductDTO))).thenReturn(Arrays.asList(/* list of ProductEntity */));

        List<ProductDTO> result = productService.searchOffer(searchProductDTO);

        assertNotNull(result);
        // Add assertions based on the expected result
    }

    @Test
    public void testGetUserProductsByUsername() {
        String username = "TestUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new UserEntity().setUsername(username)));
        when(productRepository.findAllByOwner(new UserEntity().setUsername(username))).thenReturn(Arrays.asList(/* list of ProductEntity */));

        List<ProductDTO> result = productService.getUserProductsByUsername(username);

        assertNotNull(result);
        // Add assertions based on the expected result
    }
}
