package com.example.marketplace.web;


import com.example.marketplace.model.AppUserDetails;
import com.example.marketplace.model.dtos.CategoryDTO;
import com.example.marketplace.model.dtos.CommentDTO;
import com.example.marketplace.model.dtos.ProductDTO;
import com.example.marketplace.model.dtos.SearchProductDTO;
import com.example.marketplace.service.CategoryService;
import com.example.marketplace.service.ProductService;
import com.example.marketplace.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private final UserService userService;

    public ProductController(CategoryService categoryService, ProductService productService, UserService userService, UserService userService1) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.userService = userService1;
    }

    @ModelAttribute("productDTO")
    public ProductDTO initProductDto() {
        return new ProductDTO();
    }

    @ModelAttribute("commentDTO")
    public CommentDTO initCommentDTO() {
        return new CommentDTO();
    }

    @ModelAttribute("searchProductDTO")
    public SearchProductDTO initSearchProductDTO() {
        return new SearchProductDTO();
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
    public Page<ProductDTO> getAllProducts(@PageableDefault(page = 0, size = 3) Pageable pageable,
                                           @RequestParam(name = "sort", defaultValue = "addedOn") String sort) {
        Page<ProductDTO> productPage = productService.getAllProducts(pageable);
        return new PageImpl<>(productPage.getContent(), pageable, productPage.getTotalElements());
    }


    @GetMapping("/{category}")
    @ResponseBody
    public Page<ProductDTO> getProductsByCategory(
            @PathVariable String category,
            @PageableDefault(page = 0, size = 3) Pageable pageable,
            @RequestParam(name = "sort", defaultValue = "addedOn") String sort) {
        if (categoryService.checkCategory(category)) {
            Page<ProductDTO> productPage = productService.getAllByCategory(category, pageable);
            return new PageImpl<>(productPage.getContent(), pageable, productPage.getTotalElements());
        }
        Page<ProductDTO> productPage = productService.getAllBySubCategory(category, pageable);
        return new PageImpl<>(productPage.getContent(), pageable, productPage.getTotalElements());
    }


    @GetMapping("details/{id}")
    public String productDetails(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails viewer) {
        model.addAttribute("productDTO", productService.getProductById(id, viewer));
        if(viewer!=null) {
            model.addAttribute("viewerUsername", viewer.getUsername());
            model.addAttribute("isAdmin", userService.isAdmin(viewer.getUsername()));
            model.addAttribute("isModerator", userService.isModerator(viewer.getUsername()));
        }
        return "product-details";
    }

    @GetMapping("/search")
    public String search() {
        return "product-search";
    }
//    @PostMapping("/search")
//    public ResponseEntity<List<ProductDTO>> search(
//            @Valid @ModelAttribute SearchProductDTO searchProductDTO,
//            BindingResult bindingResult) {
//
//        if (bindingResult.hasErrors()) {
//            // Handle validation errors and return a bad request response
//            return ResponseEntity.badRequest().build();
//        }
//
//        // Perform the search and get the results
//        List<ProductDTO> searchResults = productService.searchOffer(searchProductDTO);
//
//        // Return the search results in the response body
//        return ResponseEntity.ok(searchResults);
//    }

    @PostMapping("/search")
    public String search(@Valid SearchProductDTO searchProductDTO,
                         BindingResult bindingResult,
                         Model model) {

        boolean searchButtonPressed = false;
        model.addAttribute("searchButtonPressed", searchButtonPressed);

        if (bindingResult.hasErrors()) {
            model.addAttribute("searchProductDTO", searchProductDTO);
            model.addAttribute(
                    "org.springframework.validation.BindingResult.searchProductDTO",
                    bindingResult);
            return "product-search";
        }

        if (!model.containsAttribute("searchProductDTO")) {
            model.addAttribute("searchProductDTO", searchProductDTO);
        }

        if (!searchProductDTO.isEmpty()) {
            model.addAttribute("products", productService.searchOffer(searchProductDTO));
        }

        return "product-search";
    }

    @PreAuthorize("@productService.isOwner(#id, #userDetails.username)")
    @DeleteMapping("/{id}")
    public String removeProduct(@PathVariable Long id,
                                @AuthenticationPrincipal UserDetails userDetails) {
        productService.removeProduct(id);
        return "redirect:/products/all";
    }


    @PreAuthorize("@productService.isLoggedUser(#username, #userDetails.username)")
    @GetMapping("/user-products/{username}")
    public String userListings(@PathVariable String username, Model model,
                               @AuthenticationPrincipal UserDetails userDetails) {
        List<ProductDTO> userProducts = productService.getUserProductsByUsername(username);

        model.addAttribute("username", username);
        model.addAttribute("userProducts", userProducts);

        return "products-user";
    }


}
