package com.prescripto.springBackend.controller;

import com.prescripto.springBackend.model.User;
import com.prescripto.springBackend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String test() {
        return "API is Working Well";
    }

    @PostMapping("/api/user/register")
    public ResponseEntity<Map<String, Object>> registerUser(@ModelAttribute User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/api/user/login")
    public ResponseEntity<Map<String, Object>> loginUser(@ModelAttribute User user) {
        return userService.loginUser(user);
    }

    @GetMapping("/api/user/get-profile")
    public ResponseEntity<Map<String, Object>> getProfile(HttpServletRequest request){
        Long userId = (Long) request.getAttribute("userId");
        return userService.getProfile(userId);
    }
}
