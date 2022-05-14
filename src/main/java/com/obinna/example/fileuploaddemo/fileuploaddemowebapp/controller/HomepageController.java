package com.obinna.example.fileuploaddemo.fileuploaddemowebapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/", "/fileuploaddemo"})
public class HomepageController {

    @GetMapping(value = {"", "/home"})
    public String displayHomepage() {
        return "home/index";
    }

}
