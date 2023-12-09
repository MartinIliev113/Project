package com.example.marketplace.service;


import com.example.marketplace.model.AppUserDetails;
import com.example.marketplace.model.dtos.CommentDTO;
import com.example.marketplace.model.entity.CommentEntity;
import com.example.marketplace.model.entity.ProductEntity;
import com.example.marketplace.model.entity.UserEntity;
import com.example.marketplace.model.entity.UserRoleEntity;
import com.example.marketplace.model.enums.UserRoleEnum;
import com.example.marketplace.model.exceptions.ObjectNotFoundException;
import com.example.marketplace.repository.CommentRepository;
import com.example.marketplace.repository.ProductRepository;
import com.example.marketplace.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.example.marketplace.model.exceptions.ExceptionMessages.USER_NOT_FOUND;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CommentService(CommentRepository commentRepository, ModelMapper modelMapper, UserRepository userRepository, ProductRepository productRepository) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public void create(CommentDTO commentDTO, AppUserDetails userDetails) {
        CommentEntity comment = modelMapper.map(commentDTO, CommentEntity.class);
        comment.setId(null);
        comment.setAuthor(userRepository.findById(userDetails.getId()).orElseThrow(() -> new ObjectNotFoundException(USER_NOT_FOUND)));
        comment.setCreated(LocalDateTime.now());

        ProductEntity productEntity = productRepository.findById(commentDTO.getProductId()).orElseThrow(() -> new ObjectNotFoundException(USER_NOT_FOUND));
        commentRepository.save(comment);

        productEntity.getComments().add(comment);

        productRepository.save(productEntity);
    }

    public void delete(Long id) {
        CommentEntity commentEntity = commentRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(USER_NOT_FOUND));
        ProductEntity product = commentEntity.getProduct();
        product.getComments().remove(commentEntity);
        commentRepository.deleteById(id);
    }

    public boolean isAuthor(Long id, String userName) {
        return isAuthor(
                commentRepository.findById(id).orElse(null),
                userName
        );
    }
    private boolean isAuthor(CommentEntity commentEntity, String username) {
        if (commentEntity == null || username == null) {
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
                commentEntity.getAuthor().getId(),
                viewerEntity.getId());
    }
    private boolean isAdmin(UserEntity userEntity) {
        return userEntity
                .getRoles()
                .stream()
                .map(UserRoleEntity::getRole)
                .anyMatch(r -> UserRoleEnum.ADMIN == r);
    }

}
