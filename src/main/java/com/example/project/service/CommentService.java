package com.example.project.service;

import com.example.project.model.dtos.CommentDTO;
import com.example.project.model.entity.CommentEntity;
import com.example.project.model.entity.ProductEntity;
import com.example.project.repository.CommentRepository;
import com.example.project.repository.ProductRepository;
import com.example.project.repository.UserRepository;
import org.modelmapper.ModelMapper;
import com.example.project.model.AppUserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    //todo fixxxxx
    public void create(CommentDTO commentDTO, AppUserDetails userDetails){
        CommentEntity comment = modelMapper.map(commentDTO, CommentEntity.class);
        comment.setId(null);
        comment.setAuthor(userRepository.findById(userDetails.getId()).get());//TODO FIX
        comment.setCreated(LocalDateTime.now());

        ProductEntity productEntity = productRepository.findById(commentDTO.getProductId()).get();
        commentRepository.save(comment);

        productEntity.getComments().add(comment);

        productRepository.save(productEntity);
    }
    public void delete(Long id){
        CommentEntity commentEntity = commentRepository.findById(id).get(); //todo
        ProductEntity product = commentEntity.getProduct();
        product.getComments().remove(commentEntity);
        commentRepository.deleteById(id);
    }
}
