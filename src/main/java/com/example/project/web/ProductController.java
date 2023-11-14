package com.example.project.web;

import com.example.project.model.AppUserDetails;
import com.example.project.model.dtos.CategoryDTO;
import com.example.project.model.dtos.ProductDTO;
import com.example.project.service.CategoryService;
import com.example.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("products")
@CrossOrigin("*")
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

    @GetMapping("/add")
    public String addProduct() {
        return "offer-add";
    }

    @PostMapping("/add")
    public String addProduct(@Valid ProductDTO productDTO,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal AppUserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productDTO", productDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productDTO", bindingResult);
            return "redirect:/products/add";
        }
        productService.addProduct(productDTO, userDetails);
        return "redirect:/products/all";
    }

    @GetMapping("/all")
    public String all() {
        return "all";
    }

    @GetMapping()
    @ResponseBody
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{category}")
    @ResponseBody
    public List<ProductDTO> getProductsByCategory(@PathVariable String category) {
        if (categoryService.checkCategory(category)) {
            return productService.getAllByCategory(category);
        }
        return productService.getAllBySubCategory(category);
    }
}




//    @GetMapping("/{id}")
//    @ResponseBody
//    public ProductDTO getProductById(@PathVariable Long id) {
//        return productService.getProductById(id);
//    }

