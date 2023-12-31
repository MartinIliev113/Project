package com.example.marketplace.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.marketplace.model.AppUserDetails;
import com.example.marketplace.model.dtos.ProductDTO;
import com.example.marketplace.model.dtos.SearchProductDTO;
import com.example.marketplace.model.entity.*;
import com.example.marketplace.model.enums.UserRoleEnum;
import com.example.marketplace.model.exceptions.ObjectNotFoundException;
import com.example.marketplace.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.marketplace.model.exceptions.ExceptionMessages.*;


@Service
public class ProductService {
    private static final String BASE_IMAGES_PATH = "./src/main/resources/static/images/";

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final SubCategoryRepository subCategoryRepository;
    private final CommentService commentService;
    private final Cloudinary cloudinary;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, UserRepository userRepository, ModelMapper modelMapper, SubCategoryRepository subCategoryRepository, CommentService commentService, Cloudinary cloudinary) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.subCategoryRepository = subCategoryRepository;
        this.commentService = commentService;
        this.cloudinary = cloudinary;
    }
    public void addProduct(ProductDTO productDTO, AppUserDetails userDetails) {
        ProductEntity product = modelMapper.map(productDTO, ProductEntity.class);
        SubCategoryEntity subCategory = subCategoryRepository.findByName(productDTO.getSubCategory())
                .orElseThrow(() -> new ObjectNotFoundException(CATEGORY_NOT_FOUND));
        CategoryEntity category = categoryRepository.findBySubCategoriesContaining(subCategory);
        product.setCategory(category);
        product.setSubCategory(subCategory);
        product.setOwner(userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ObjectNotFoundException(USER_NOT_FOUND)));
        category.getProducts().add(product);
        subCategory.getProducts().add(product);
        product.setAddedOn(LocalDateTime.now());

        // Upload primary image to Cloudinary with a structured public_id
        MultipartFile primaryImage = productDTO.getPrimaryImage();
        String primaryImageUrl = uploadImageToCloudinary(primaryImage, userDetails.getUsername(), productDTO.getTitle());
        product.setPrimaryImageUrl(primaryImageUrl);

        // Upload additional images to Cloudinary with a structured public_id
        List<String> imageUrls = productDTO.getImages().stream()
                .map(image -> uploadImageToCloudinary(image, userDetails.getUsername(), productDTO.getTitle()))
                .collect(Collectors.toList());

        product.setImageUrls(imageUrls);

        productRepository.save(product);
    }


    private String uploadImageToCloudinary(MultipartFile image, String username, String productTitle) {
        try {
            String folder = String.format("%s/%s", username, transformProductTitle(productTitle));

            // Use Cloudinary uploader to upload the image with a structured public_id
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(),
                    ObjectUtils.asMap("public_id", folder + "/" + UUID.randomUUID()));
            return (String) uploadResult.get("url");
        } catch (IOException e) {
            // Handle exception appropriately (e.g., log the error)
            throw new RuntimeException("Error uploading image to Cloudinary: " + e.getMessage(), e);
        }
    }


//    public void addProduct(ProductDTO productDTO, AppUserDetails userDetails) {
//        ProductEntity product = modelMapper.map(productDTO, ProductEntity.class);
//        SubCategoryEntity subCategory = subCategoryRepository.findByName(productDTO.getSubCategory()).orElseThrow(()->new ObjectNotFoundException(CATEGORY_NOT_FOUND));
//        CategoryEntity category = categoryRepository.findBySubCategoriesContaining(subCategory);
//        product.setCategory(category);
//        product.setSubCategory(subCategory);
//        product.setOwner(userRepository.findById(userDetails.getId()).orElseThrow(() -> new ObjectNotFoundException(USER_NOT_FOUND)));
//        category.getProducts().add(product);
//        subCategory.getProducts().add(product);
//        product.setAddedOn(LocalDateTime.now());
//
//        String primaryFilePath = getFilePath(userDetails.getUsername(), product.getTitle(), productDTO.getPrimaryImage());
//        boolean isPrimaryUploaded = uploadImage(productDTO.getPrimaryImage(), primaryFilePath);
//
//        if (isPrimaryUploaded) {
//            product.setPrimaryImageUrl(primaryFilePath);
//        }
//
//
//        List<String> imageUrls = productDTO.getImages().stream()
//                .map(image -> {
//                    String filePath = getFilePath(userDetails.getUsername(), product.getTitle(), image);
//                    boolean isUploaded = uploadImage(image, filePath);
//                    return isUploaded ? filePath : null;
//                })
//                .filter(url -> url != null)
//                .collect(Collectors.toList());
//
//        product.setImageUrls(imageUrls);
//
//        productRepository.save(product);
//    }
//
//    private boolean uploadImage(MultipartFile image, String filePath) {
//        try {
//            File newFile = new File(BASE_IMAGES_PATH + filePath);
//            newFile.getParentFile().mkdirs();
//            if (newFile.createNewFile()) {
//                try (OutputStream outputStream = new FileOutputStream(newFile)) {
//                    outputStream.write(image.getBytes());
//                    return true;
//                } catch (IOException e) {
//                    System.out.println("Error writing file: " + e.getMessage());
//                }
//            } else {
//                System.out.println("File already exists");
//            }
//        } catch (IOException e) {
//            System.out.println("Error creating file: " + e.getMessage());
//        }
//
//        return false;
//    }


//    public List<ProductDTO> getAllProducts() {
//        return productRepository.findAll().stream().map(productEntity -> modelMapper.map(productEntity, ProductDTO.class)).toList();
//    }

    public ProductDTO getProductById(Long id, UserDetails viewer) {
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(PRODUCT_NOT_FOUND));
        ProductDTO productDTO = modelMapper.map(productEntity, ProductDTO.class);
        productDTO.setOwner(productEntity.getOwner().getUsername());
        if (viewer == null) {
            productDTO.setViewerIsOwner(false);
        } else if (isOwner(productEntity, viewer.getUsername())) {
            productDTO.setViewerIsOwner(true);
        }
        return productDTO;
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


    public Page<ProductDTO> getAllByCategory(String category, Pageable pageable) {
        return productRepository.findAllByCategory(categoryRepository.findByName(category).orElseThrow(()->new ObjectNotFoundException(CATEGORY_NOT_FOUND)), pageable)
                .map(productEntity -> modelMapper.map(productEntity, ProductDTO.class));
    }

    public Page<ProductDTO> getAllBySubCategory(String subCategory, Pageable pageable) {
        return productRepository.findAllBySubCategory(subCategoryRepository.findByName(subCategory).get(),
//                        .orElseThrow(()->new ObjectNotFoundException(CATEGORY_NOT_FOUND)),
                        pageable)
                .map(productEntity -> modelMapper.map(productEntity, ProductDTO.class));
    }

    public List<ProductDTO> searchOffer(SearchProductDTO searchProductDTO) {
        return productRepository.findAll(new ProductSpecification(searchProductDTO))
                .stream().map(productEntity -> modelMapper.map(productEntity, ProductDTO.class))
                .toList();
    }

    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(productEntity -> modelMapper.map(productEntity, ProductDTO.class));
    }

    public boolean isOwner(Long id, String userName) {
        return isOwner(
                productRepository.findById(id).orElse(null),
                userName
        );
    }

    private boolean isOwner(ProductEntity productEntity, String username) {
        if (productEntity == null || username == null) {
            return false;
        }

        UserEntity viewerEntity =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new ObjectNotFoundException(USER_NOT_FOUND));

        if (isAdmin(viewerEntity)) {
            return true;
        }

        return Objects.equals(
                productEntity.getOwner().getId(),
                viewerEntity.getId());
    }


    private boolean isAdmin(UserEntity userEntity) {
        return userEntity
                .getRoles()
                .stream()
                .map(UserRoleEntity::getRole)
                .anyMatch(r -> UserRoleEnum.ADMIN == r);
    }


    public void removeProduct(Long id) {
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(PRODUCT_NOT_FOUND));

        deleteImageFromCloudinary(productEntity.getPrimaryImageUrl());
        productEntity.getImageUrls().forEach(this::deleteImageFromCloudinary);

        SubCategoryEntity subCategory = productEntity.getSubCategory();
        subCategory.getProducts().remove(productEntity);
        CategoryEntity category = productEntity.getCategory();
        category.getProducts().remove(productEntity);

        List<CommentEntity> comments = new ArrayList<>(productEntity.getComments());
        Iterator<CommentEntity> iterator = comments.iterator();

        while (iterator.hasNext()) {
            CommentEntity comment = iterator.next();
            iterator.remove();
            commentService.delete(comment.getId());
        }

        productRepository.delete(productEntity);
    }

    private void deleteImageFromCloudinary(String imageUrl) {
        if (imageUrl != null) {
            try {
                // Parse Cloudinary URL to extract public_id
                String publicId = String.valueOf(cloudinary.url().publicId(imageUrl));

                // Delete the resource from Cloudinary
                cloudinary.api().deleteResources(Arrays.asList(publicId), ObjectUtils.emptyMap());
            } catch (Exception e) {
                throw new RuntimeException("Error deleting image from Cloudinary: " + e.getMessage(), e);
            }
        }
    }




    public boolean isLoggedUser(String username, String loggedUserUsername) {
        UserEntity userEntity = userRepository.findByUsername(loggedUserUsername).orElseThrow(() -> new ObjectNotFoundException(USER_NOT_FOUND));
        if (isAdmin(userEntity)) {
            return true;
        }
        return username.equals(loggedUserUsername);
    }


    public List<ProductDTO> getUserProductsByUsername(String username) {
        return productRepository.findAllByOwner(userRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException(USER_NOT_FOUND)))
                .stream().map(productEntity -> modelMapper.map(productEntity, ProductDTO.class)).toList();
    }
}

