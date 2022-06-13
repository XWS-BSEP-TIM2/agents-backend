package com.dislinkt.agents.security.model;

import com.dislinkt.agents.model.enums.ApplicationUserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationResponse {

    private String jwt;
    private String fullName;
    private String email;
    private String id;
    private ApplicationUserRole role;
    private boolean twoFactor;

}
