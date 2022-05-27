package com.dislinkt.agents.controller;

import com.dislinkt.agents.converter.DataConverter;
import com.dislinkt.agents.dto.UserDTO;
import com.dislinkt.agents.model.ApplicationUser;
import com.dislinkt.agents.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final DataConverter converter;

    @GetMapping("/{userId}")
    public ResponseEntity<?> registerNewUser(@PathVariable String userId) {
        ApplicationUser user = userService.findById(userId);
        if (user == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        else return new ResponseEntity(converter.convert(user, UserDTO.class), HttpStatus.OK);
    }


}
