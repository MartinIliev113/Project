package com.example.project.web;

import com.example.project.model.AppUserDetails;
import com.example.project.model.dtos.CategoryDTO;
import com.example.project.model.dtos.ProductDTO;
import com.example.project.model.dtos.SubCategoryDto;
import com.example.project.service.CategoryService;
import com.example.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProductController {
    private final CategoryService categoryService;
    private final ProductService productService;

    public ProductController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @ModelAttribute("productDTO")
    public ProductDTO initProductDto() {
        return new ProductDTO();
    }
    @ModelAttribute("categories")
    public List<CategoryDTO> categories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/products/add")
    public String addProduct(){
        return "offer-add";
    }
    @PostMapping("/products/add")
    public String addProduct(@Valid ProductDTO productDTO,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal AppUserDetails userDetails){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("productDTO",productDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productDTO", bindingResult);
            return "redirect:/products/add";
        }
        productService.addProduct(productDTO,userDetails);
        return "redirect:/products";
    }
}
