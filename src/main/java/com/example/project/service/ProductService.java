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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private static final String BASE_IMAGES_PATH = "./src/main/resources/static/images/";

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
        CategoryEntity category = categoryRepository.findBySubCategoriesContaining(subCategory);
        product.setCategory(category);
        product.setSubCategory(subCategory);
        product.setOwner(userRepository.findById(userDetails.getId()).get());//TODO FIX
        category.getProducts().add(product);
        subCategory.getProducts().add(product);

        String filePath = getFilePath(userDetails.getUsername(), product.getTitle(), productDTO.getImage());
        boolean isUploaded = uploadImage(productDTO.getImage(), filePath);

        if (isUploaded) {
            product.setImageUrl(filePath);
        }


        productRepository.save(product);
    }

    private boolean uploadImage(MultipartFile image, String filePath) {
        try {
            File newFile = new File(BASE_IMAGES_PATH + filePath);
            newFile.getParentFile().mkdirs();
            if (newFile.createNewFile()) {
                try (OutputStream outputStream = new FileOutputStream(newFile)) {
                    outputStream.write(image.getBytes());
                    return true;
                } catch (IOException e) {
                    System.out.println("Error writing file: " + e.getMessage());
                }
            } else {
                System.out.println("File already exists");
            }
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }

        return false;
    }


    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(productEntity -> {
            ProductDTO productDTO = modelMapper.map(productEntity, ProductDTO.class);
            productDTO.setImageUrl("images/" + productEntity.getImageUrl());
            return productDTO;
        }).toList();
    }

    public ProductDTO getProductById(Long id) {
        return modelMapper.map(productRepository.findById(id), ProductDTO.class);
    }

    private String getFilePath(String username, String productTitle, MultipartFile pictureFile) {
        String[] splitPictureName = pictureFile.getOriginalFilename().split("\\.");
        String ext = splitPictureName[splitPictureName.length - 1];

        String pathPattern = "%s/%s/%s." + ext;
        return String.format(pathPattern,
                username,
                transformProductTitle(productTitle),
                UUID.randomUUID());
    }

    private String transformProductTitle(String productTitle) {
        return productTitle.toLowerCase()
                .replaceAll("\\s+", "_");
    }


    public List<ProductDTO> getAllByCategory(String category) {
        List<ProductEntity> productEntities = productRepository.findAllByCategory(categoryRepository.findByName(category));
        return productEntities.stream().map(productEntity -> {
            ProductDTO productDTO = modelMapper.map(productEntity, ProductDTO.class); //todo typemap
            productDTO.setImageUrl("images/"+productEntity.getImageUrl());
            return productDTO;
        }).toList();
    }

    public List<ProductDTO> getAllBySubCategory(String subCategory) {
        List<ProductEntity> productEntities = productRepository.findAllBySubCategory(subCategoryRepository.findByName(subCategory));
        return productEntities.stream().map(productEntity -> {
            ProductDTO productDTO = modelMapper.map(productEntity, ProductDTO.class); //todo typemap
            productDTO.setImageUrl("images/"+productEntity.getImageUrl());
            return productDTO;
        }).toList();
    }
}
