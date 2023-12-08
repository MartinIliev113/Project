package com.example.marketplace.web;


import com.example.marketplace.model.dtos.SubCategoryDto;
import com.example.marketplace.service.CategoryService;
import com.example.marketplace.service.SubCategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequestMapping("/subcategories/{id}")
@Controller
public class SubCategoriesController {
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;

    public SubCategoriesController(CategoryService categoryService, SubCategoryService subCategoryService) {
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
    }

    @ModelAttribute("categoryName")
    public String categoryName(@PathVariable("id") Long id) {
        return categoryService.getCategoryName(id);
    }

    @ModelAttribute("categoryId")
    public Long categoryId(@PathVariable("id") Long id) {
        return id;
    }

    @ModelAttribute("subcategories")
    public List<SubCategoryDto> subcategories(@PathVariable("id") Long id) {
        return categoryService.getSubCategories(id);
    }

    @ModelAttribute(name = "subCategoryDTO")
    public SubCategoryDto initUserRegistrationDto() {
        return new SubCategoryDto();
    }

    @GetMapping()
    public String subcategoriesPage(@PathVariable Long id) {
        subcategories(id);
        return "subcategories";
    }

    @PostMapping()
    public String addSubCategory(@Valid SubCategoryDto subCategoryDto,
                                 BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                 @PathVariable Long id) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("subCategoryDto", subCategoryDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.subCategoryDto", bindingResult);
            return "redirect:/categories";
        }
        subCategoryService.addSubcategory(subCategoryDto, id);
        return "redirect:/categories";
    }
    @DeleteMapping("/{subcategory}")
    public String deleteSubCategory(@PathVariable String subcategory){
        subCategoryService.deleteSubCategory(subcategory);
        return "redirect:/categories";
    }
}
