package com.dislinkt.agents.controller;

import com.dislinkt.agents.converter.DataConverter;
import com.dislinkt.agents.dto.UserDTO;
import com.dislinkt.agents.model.ApplicationUser;
import com.dislinkt.agents.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final DataConverter converter;

    @GetMapping("/{userId}")
    public UserDTO registerNewUser(@PathVariable String userId) {
        ApplicationUser user = userService.findById(userId);
        if (user == null) {
            return null;
        }
        else return converter.convert(user, UserDTO.class);
    }


}
