package com.example.project.web;


import com.example.project.model.dtos.UserDTO;
import com.example.project.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller

public class UserRegistrationController {
    private static final String BINDING_RESULT_PATH = "org.springframework.validation.BindingResult.";
    private final UserService userService;

    @ModelAttribute(name = "registrationDTO")
    public UserDTO initUserRegistrationDto(){
        return new UserDTO();
    }

    public UserRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("users/register")
    public String register() {
        return "auth-register";
    }

    @PostMapping("/users/register")
    public String registerNewUser(
            @Valid UserDTO registrationDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("registrationDTO", registrationDTO)
                    .addFlashAttribute(BINDING_RESULT_PATH + "registrationDTO", bindingResult);

            return "redirect:/users/register";
        }

        userService.registerUser(registrationDTO);

        return "redirect:/users/login";
    }


}
