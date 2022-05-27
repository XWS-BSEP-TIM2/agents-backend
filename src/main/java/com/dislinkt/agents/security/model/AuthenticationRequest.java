package com.dislinkt.agents.security.model;

import lombok.Data;

@Data
public class AuthenticationRequest {

    private String email;
    private String password;

}
