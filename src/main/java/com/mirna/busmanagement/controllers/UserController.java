package com.mirna.busmanagement.controllers;

import com.mirna.busmanagement.dtos.UserDto;
import com.mirna.busmanagement.mappers.UserMapper;
import com.mirna.busmanagement.models.User;
import com.mirna.busmanagement.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String getAllUsers(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "8") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.getAllUsersPaginated(pageable);
        model.addAttribute("page", users);
        if (users.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(0, users.getTotalPages() - 1)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "users";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    @GetMapping("/update")
    public String showUpdateUserForm(@RequestParam String userId, Model model) {
        UserDto user = UserMapper.EntitytoUserDto(userService.getUserById(userId));
        model.addAttribute("user", user);
        model.addAttribute("userId", userId);
        return "update_user";
    }

    @PutMapping("/update")
    public String updateUser(@RequestParam String userId, @ModelAttribute(name = "user") @Valid UserDto user,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userId",userId);
            return "update_user";
        }
        try {
            userService.updateUser(userId, user);
            return "redirect:/users";
        } catch (Exception e) {
            model.addAttribute("userId",userId);
            model.addAttribute("notValid", e.getMessage());
            return "update_user";
        }
    }
}
