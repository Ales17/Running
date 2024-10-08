package com.rungroup.web.controller;

import com.rungroup.web.dto.UserDto;
import com.rungroup.web.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RolesAllowed("ADMIN")
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("")
    public String adminHomePage() {
        return "admin";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<UserDto> users = userService.findBasicUsers();
        model.addAttribute("users", users);
        // Table containing all (no ROLE_ADMIN) users
        return "fragments/admin :: users";
    }

    @GetMapping("/users/{userId}")
    public String userDetail(Model model, @PathVariable Long userId) {
        UserDto user = userService.findById(userId);
        model.addAttribute("user", user);
        // Single user row if edit is canceled (HTMX)
        return "fragments/admin :: users-row";
    }

    @GetMapping("/users/{userId}/edit")
    public String editUserRow(Model model, @PathVariable Long userId) {
        UserDto user = userService.findById(userId);
        model.addAttribute("user", user);
        // Single user row with fields to edit (HTMX)
        return "fragments/admin :: users-row-edit";
    }

    @PutMapping("/users/{userId}/edit")
    public String editUser(Model model, @PathVariable Long userId, @ModelAttribute UserDto user) {
        UserDto updatedUser = userService.updateUserWithoutPassword(user);
        model.addAttribute("user", updatedUser);
        // Return table row (tr) with updated user (HTMX)
        return "fragments/admin :: users-row";
    }
}
