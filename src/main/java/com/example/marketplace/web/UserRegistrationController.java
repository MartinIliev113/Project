package com.example.marketplace.web;


import com.example.marketplace.model.dtos.UserDTO;
import com.example.marketplace.service.EmailService;
import com.example.marketplace.service.UserActivationService;
import com.example.marketplace.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller

public class UserRegistrationController {
    private static final String BINDING_RESULT_PATH = "org.springframework.validation.BindingResult.";
    private final UserService userService;
    private final UserActivationService userActivationService;

    @ModelAttribute(name = "registrationDTO")
    public UserDTO initUserRegistrationDto() {
        return new UserDTO();
    }

    public UserRegistrationController(UserService userService, EmailService emailService, UserActivationService userActivationService) {
        this.userService = userService;
        this.userActivationService = userActivationService;
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

    @GetMapping("/user/activate")
    public String activateUser(@RequestParam("activation_code") String activationCode) {
        userActivationService.activateUser(activationCode);
        return "redirect:/products/all";
    }
}
