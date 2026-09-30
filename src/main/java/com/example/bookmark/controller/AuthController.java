package com.example.bookmark.controller;

import com.example.bookmark.model.User;
import com.example.bookmark.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute User user,
            Model model) {

        if (userRepository.existsByUsername(
                user.getUsername())) {

            model.addAttribute(
                    "error", "Username already exists");

            return "register";
        }

        if (userRepository.existsByEmail(
                user.getEmail())) {

            model.addAttribute(
                    "error", "Email already exists");

            return "register";
        }

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        userRepository.save(user);

        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}