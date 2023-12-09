package com.example.marketplace.service;

import com.example.marketplace.model.AppUserDetails;
import com.example.marketplace.model.dtos.CommentDTO;
import com.example.marketplace.model.entity.CommentEntity;
import com.example.marketplace.model.entity.ProductEntity;
import com.example.marketplace.model.entity.UserEntity;
import com.example.marketplace.repository.CommentRepository;
import com.example.marketplace.repository.ProductRepository;
import com.example.marketplace.repository.UserRepository;
import com.example.marketplace.model.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CommentServiceTest {

    List<GrantedAuthority> authorities=new ArrayList<>();
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreate() {
        CommentDTO commentDTO = new CommentDTO();
        AppUserDetails userDetails = new AppUserDetails("username", "password",authorities);

        UserEntity author = new UserEntity();
        author.setId(1L);

        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(1L);

        when(modelMapper.map(commentDTO, CommentEntity.class)).thenReturn(commentEntity);
        when(userRepository.findById(userDetails.getId())).thenReturn(Optional.of(author));
        when(productRepository.findById(commentDTO.getProductId())).thenReturn(Optional.of(productEntity));

        assertDoesNotThrow(() -> commentService.create(commentDTO, userDetails));

        verify(commentRepository).save(commentEntity);
        verify(productRepository).save(productEntity);
        assertTrue(productEntity.getComments().contains(commentEntity));
    }

    @Test
    public void testCreate_UserNotFound() {
        CommentDTO commentDTO = new CommentDTO();
        AppUserDetails userDetails = new AppUserDetails( "username", "password", authorities);

        when(userRepository.findById(userDetails.getId())).thenReturn(Optional.empty());
        when(modelMapper.map(commentDTO, CommentEntity.class)).thenReturn(new CommentEntity());

        assertThrows(ObjectNotFoundException.class, () -> commentService.create(commentDTO, userDetails));
    }

    @Test
    public void testCreate_ProductNotFound() {
        CommentDTO commentDTO = new CommentDTO();
        AppUserDetails userDetails = new AppUserDetails( "username", "password", authorities);

        UserEntity author = new UserEntity();
        author.setId(1L);

        when(userRepository.findById(userDetails.getId())).thenReturn(Optional.of(author));
        when(productRepository.findById(commentDTO.getProductId())).thenReturn(Optional.empty());
        when(modelMapper.map(commentDTO, CommentEntity.class)).thenReturn(new CommentEntity());

        assertThrows(ObjectNotFoundException.class, () -> commentService.create(commentDTO, userDetails));
    }

    @Test
    public void testDelete_CommentNotFound() {
        long commentId = 1L;

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> commentService.delete(commentId));
    }
}
