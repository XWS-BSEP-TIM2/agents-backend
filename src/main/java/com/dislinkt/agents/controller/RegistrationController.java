package com.dislinkt.agents.controller;

import com.dislinkt.agents.dto.UserDTO;
import com.dislinkt.agents.model.ApplicationUser;
import com.dislinkt.agents.service.LoggingService;
import com.dislinkt.agents.service.interfaces.UserService;
import com.dislinkt.agents.service.interfaces.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;
    private final LoggingService loggingService;
    private final ValidationService validationService;

    @PostMapping
    public boolean registerNewUser(@RequestBody UserDTO newUser) {
        validationService.validateUser(newUser);
        ApplicationUser createdUser = userService.registerNewUser(newUser);
        if (createdUser != null) {
            loggingService.MakeWarningLog("Registration not successfull.");
            return true;
        }
        else {
            loggingService.MakeInfoLog("User "+ newUser.getEmail() +" successfully registered.");
            return false;
        }
    }

}

