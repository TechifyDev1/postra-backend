package com.qudus.postra.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestingController {

    @GetMapping("/test")
    public String test() {
        return "Working...";
    }
}
