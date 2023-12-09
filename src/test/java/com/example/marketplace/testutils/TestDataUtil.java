package com.example.marketplace.testutils;


import com.example.marketplace.model.entity.*;
import com.example.marketplace.repository.CategoryRepository;
import com.example.marketplace.repository.CommentRepository;
import com.example.marketplace.repository.ProductRepository;
import com.example.marketplace.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class TestDataUtil {



    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;



    public ProductEntity createTestProduct(UserEntity owner) {

        CategoryEntity categoryEntity = categoryRepository.save(new CategoryEntity()
                .setName("Test Category")
                );
        SubCategoryEntity subCategoryEntity = subCategoryRepository.save(new SubCategoryEntity()
                .setName("Test Category"));

        ProductEntity product = new ProductEntity()
                .setAddedOn(LocalDateTime.now())
                .setCategory(categoryEntity)
                .setSubCategory(subCategoryEntity)
                .setDescription("test description")
                .setPrice(BigDecimal.valueOf(123))
                .setOwner(owner);

        return productRepository.save(product);
    }
    public CommentEntity createTestComment(UserEntity owner) {


        return commentRepository.save(new CommentEntity()
                .setTextContent("test").setCreated(LocalDateTime.now())
        );
    }
    public void addAuthor(CommentEntity commentEntity,UserEntity userEntity){
        commentRepository.save(commentEntity.setAuthor(userEntity));
    }

    public void cleanUp() {
        commentRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        subCategoryRepository.deleteAll();

    }

    public void addProductToComment(CommentEntity testComment, ProductEntity productEntity) {
         commentRepository.save(testComment.setProduct(productEntity));
    }
}
