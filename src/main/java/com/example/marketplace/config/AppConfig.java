package com.example.marketplace.config;


import com.example.marketplace.model.dtos.ProductDTO;
import com.example.marketplace.model.entity.ProductEntity;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

        TypeMap<ProductEntity, ProductDTO> typeMap = modelMapper.createTypeMap(ProductEntity.class, ProductDTO.class);

        typeMap.addMapping(src -> src.getAddedOn(), ProductDTO::setAddedOn);

        return modelMapper;
    }
}
