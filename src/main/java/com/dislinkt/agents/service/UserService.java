package com.dislinkt.agents.service;

import com.dislinkt.agents.model.ApplicationUser;

public interface UserService {
    ApplicationUser findByEmail(String email);
}
