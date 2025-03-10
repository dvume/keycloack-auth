package ru.dvume.controller.v1;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @GetMapping("/hello")
    @PreAuthorize("hasRole('APPUSER')")
    public String hello() {
        return "hello from user";
    }
}
