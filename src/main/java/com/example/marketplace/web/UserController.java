package com.example.marketplace.web;

import com.example.marketplace.model.dtos.ProductDTO;
import com.example.marketplace.model.dtos.UserDTO;
import com.example.marketplace.model.dtos.UserEditDTO;
import com.example.marketplace.service.ProductService;
import com.example.marketplace.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {
    private final UserService userService;
    private final ProductService productService;
    @ModelAttribute("editUserDTO")
    public UserEditDTO editUserDTO() {
        return new UserEditDTO();
    }

    public UserController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/my-profile")
    public String myProfile(Authentication authentication, Model model) {


        List<ProductDTO> userProducts = productService.getUserProductsByUsername(authentication.getName());
        model.addAttribute("userProducts", userProducts);

        String username = authentication.getName();
        UserDTO user = userService.findByUsername(username);
        model.addAttribute("user", user);

        return "my-profile";
    }

    @PostMapping("/my-profile")
    public String editProfile(@Valid UserEditDTO editUserDTO,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes
            , Authentication authentication,Model model) {


        UserDTO loggedUser = userService.findByUsername(authentication.getName());
        if (!loggedUser.getUsername().equals(editUserDTO.getUsername()) &&
                userService.isUsernameTaken(editUserDTO.getUsername())) {
            model.addAttribute("usernameError", "Username is already taken");
            return "redirect:/my-profile";
        }

        if (!loggedUser.getEmail().equals(editUserDTO.getEmail()) &&
                userService.isEmailTaken(editUserDTO.getEmail())) {
            model.addAttribute("emailError", "Email is already taken");
            return "redirect:/my-profile";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("editUserDTO", editUserDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editUserDTO", bindingResult);
            return "redirect:/my-profile";
        }
        userService.editUser(editUserDTO,authentication.getName());
        return "redirect:/my-profile";
    }

}
