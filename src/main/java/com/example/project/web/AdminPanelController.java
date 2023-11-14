package com.example.project.web;

import com.example.project.model.dtos.UserDTO;
import com.example.project.model.dtos.UserRoleDto;
import com.example.project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminPanelController {

    @ModelAttribute("userRoleDto")
    UserRoleDto initUserRoleDto(){
        return new UserRoleDto();
    }

    private final UserService userService;

    public AdminPanelController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("moderators")
    public List<UserDTO> moderators(){
        return userService.getModerators();
    }
    @ModelAttribute("admins")
    public List<UserDTO> admins(){
        return userService.getAdmins();
    }
    @ModelAttribute("users")
    public List<UserDTO> users(){
        return userService.getUsers();
    }


    @GetMapping
    public String admin(){
        moderators();
        System.out.println();
        return "admins";
    }

    @GetMapping("/{username}")
    public  String roleChange(@PathVariable String username, Model model){
        model.addAttribute(userService.findByUsername(username).getUsername(),"username");

        return "change-role";
    }
    @PostMapping("/{username}")
    public String roleChanger(@PathVariable String username,UserRoleDto userRoleDto){
        userService.changeRole(userRoleDto,username);

        return  "redirect:/admin";
    }
}
