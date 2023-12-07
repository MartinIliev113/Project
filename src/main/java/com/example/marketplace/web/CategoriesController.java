package com.example.marketplace.web;


import com.example.marketplace.model.dtos.CategoryDTO;
import com.example.marketplace.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoriesController {

    private final CategoryService categoryService;

    public CategoriesController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ModelAttribute(name = "categoryDTO")
    public CategoryDTO initUserRegistrationDto() {
        return new CategoryDTO();
    }

    @ModelAttribute("categories")
    public List<CategoryDTO> categories() {
        return categoryService.getAllCategories();
    }


    @GetMapping()
    public String categoriesPage() {
        return "categories";
    }


    @PostMapping()
    public String addCategory(@Valid CategoryDTO categoryDTO,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("categoryDTO", categoryDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.categoryDTO", bindingResult);
            return "redirect:/categories";
        }
        categoryService.addCategory(categoryDTO);
        return "redirect:/categories";
    }
    @DeleteMapping("/{category}")
    public String deleteCategory(@PathVariable String category){
        categoryService.deleteCategory(category);
        return "redirect:/categories";
    }

}
