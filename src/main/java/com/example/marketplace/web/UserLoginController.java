package com.example.marketplace.web;

import com.example.marketplace.model.dtos.PassChangeDTO;
import com.example.marketplace.service.UserActivationService;
import com.example.marketplace.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserLoginController {
    private static final String BINDING_RESULT_PATH = "org.springframework.validation.BindingResult.";
    private final UserService userService;
    private final UserActivationService userActivationService;

    public UserLoginController(UserService userService, UserActivationService userActivationService) {
        this.userService = userService;
        this.userActivationService = userActivationService;
    }

    @ModelAttribute(name = "passChangeDTO")
    public PassChangeDTO initPassChangeDto() {
        return new PassChangeDTO();
    }

    @GetMapping("/users/login")
    public String login() {
        return "auth-login";
    }


    @PostMapping("/users/login-error")
    public String onFailedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String username,
            RedirectAttributes redirectAttributes) {

        boolean active = userService.isActive(username);
        if (!active) {
            redirectAttributes.addFlashAttribute("inactive", true);
            redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);
            redirectAttributes.addFlashAttribute("bad_credentials", false);
        } else {
            redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);
            redirectAttributes.addFlashAttribute("bad_credentials", true);
        }
        return "redirect:/users/login";
    }

    @GetMapping("/users/forgot-password")
    public String forgottenPassword() {
        return "forgot-password";
    }

    @PostMapping("/users/forgot-password")
    public String forgottenPassword(@RequestParam String email, Model model) {
        Boolean found = userService.findEmail(email);
        model.addAttribute("emailFound", found);
        if (found) {
            userService.passwordChangePublish(email);
        }
        return "forgot-password";
    }

    @GetMapping("/user/change-pass/{forgotPasswordCode}")
    public String changePass(@PathVariable String forgotPasswordCode, Model model) {
        String username = userActivationService.getUserNameByForgotPasswordCode(forgotPasswordCode);
        model.addAttribute("username", username);
        model.addAttribute("code", forgotPasswordCode);
        return "change-password";
    }

    @PostMapping("/user/change-pass/{forgotPasswordCode}")
    public String changePass(@PathVariable String forgotPasswordCode,
                             @Valid PassChangeDTO passChangeDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("passChangeDTO", passChangeDTO)
                    .addFlashAttribute(BINDING_RESULT_PATH + "passChangeDTO", bindingResult);

            return "redirect:/user/change-pass/" + forgotPasswordCode;
        }
        String username = userActivationService.getUserNameByForgotPasswordCode(forgotPasswordCode);
        userService.changePassword(username, passChangeDTO.getPassword());
        return "redirect:/users/login";
    }


}
