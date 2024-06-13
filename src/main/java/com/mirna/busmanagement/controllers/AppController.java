package com.mirna.busmanagement.controllers;

import com.mirna.busmanagement.dtos.UserDto;
import com.mirna.busmanagement.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AppController {

    private final UserService userService;

    @Autowired
    public AppController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/home", "/"})
    public String home() {
        return "home";
    }

    @GetMapping("/registration")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "registration";

    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute(name = "user") @Valid UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        try {
            userService.saveUser(userDto);
            return "redirect:/home";
        } catch (Exception e) {
            model.addAttribute("notValid", "ERROR:"+e.getMessage());
            return "registration";
        }
    }

}
