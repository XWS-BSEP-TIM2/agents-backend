package com.dislinkt.agents.service.interfaces;

import com.dislinkt.agents.dto.UserDTO;
import com.dislinkt.agents.model.ApplicationUser;

import java.util.List;

public interface UserService {

    List<ApplicationUser> findAll();

    ApplicationUser findById(String id);

    ApplicationUser findByEmail(String email);

    ApplicationUser registerNewUser(UserDTO newUser);
}