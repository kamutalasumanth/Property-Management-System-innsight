package com.innsight.controller;


import com.innsight.dto.UserResponse;
import com.innsight.model.User;
import com.innsight.security.SecurityContextUtil;
import com.innsight.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService){
        this.userService = userService;
    }
    @GetMapping("/")
    public String sayHello(){
        return "Welcome to innsight";
    }

    @GetMapping("/api/test/me")
    public String whoAmI() {
        return SecurityContextUtil.getCurrentUserEmail();
    }

    @GetMapping("/api/test/me-full")
    public UserResponse me() {

        User user = userService.getCurrentAuthenticatedUser();

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }

}
