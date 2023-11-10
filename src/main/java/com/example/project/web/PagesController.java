package com.example.project.web;


import com.example.project.model.AppUserDetails;
import com.example.project.model.dtos.CategoryDTO;
import com.example.project.service.CategoryService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class PagesController {

  private final CategoryService categoryService;

  public PagesController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @ModelAttribute("categories")
  public List<CategoryDTO> categories() {
    return categoryService.getAllCategories();
  }

  @GetMapping("/")
  public String home(@AuthenticationPrincipal AppUserDetails appUserDetails, Model model) {

    if (appUserDetails != null) {
      model.addAttribute("fullName", appUserDetails.getFullName());
    }

    return "index";
  }

  @GetMapping("/pages/all")
  public String all() {
    return "all";
  }
}
//
//  @GetMapping("/pages/admins")
//  public String admins() {
//    return "admins";
//  }
//
//  @GetMapping("/pages/moderators")
//  public String moderators() {
//    return "moderators";
//  }
//}
