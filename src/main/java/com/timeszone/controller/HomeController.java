package com.timeszone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectToGuestUser() {
        return "redirect:/guest/user";
    }
}