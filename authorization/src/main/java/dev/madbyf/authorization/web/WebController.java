package dev.madbyf.authorization.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/clients")
    public String clients() {
        return "clients";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/client-form")
    public String clientForm() {
        return "client-form";
    }
}
