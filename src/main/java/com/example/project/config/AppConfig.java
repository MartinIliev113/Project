package com.example.project.config;

import com.example.project.model.dtos.ProductDTO;
import com.example.project.model.entity.ProductEntity;
import com.example.project.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper(){


        return new ModelMapper();
    }
}
