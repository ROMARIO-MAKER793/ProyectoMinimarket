package com.minimarket.app.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String inicio() {
        return "auth/login";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
}