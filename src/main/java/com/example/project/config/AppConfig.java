package com.example.project.config;

import com.example.project.model.dtos.ProductDTO;
import com.example.project.model.entity.ProductEntity;
import com.example.project.repository.CategoryRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();
        Converter<LocalDateTime, String> localDateTimeToStringConverter = context ->
                context.getSource().format(DateTimeFormatter.ofPattern("HH:mm:ss:dd:MM:yyyy"));

        modelMapper.addConverter(localDateTimeToStringConverter);

        // Define TypeMap for ProductEntity to ProductDto
        TypeMap<ProductEntity, ProductDTO> typeMap = modelMapper.createTypeMap(ProductEntity.class, ProductDTO.class);

        // Map LocalDateTime using the custom converter
        typeMap.addMapping(src -> src.getAddedOn(), ProductDTO::setAddedOn);

        // Example usage
        ProductEntity productEntity = new ProductEntity();
        productEntity.setAddedOn(LocalDateTime.now());

        ProductDTO productDTO = modelMapper.map(productEntity, ProductDTO.class);

        System.out.println(productDTO.getAddedOn());

        return modelMapper;
    }
}
