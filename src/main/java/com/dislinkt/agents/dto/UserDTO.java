package com.dislinkt.agents.dto;

import com.dislinkt.agents.model.ApplicationUserRole;
import lombok.Data;

@Data
public class UserDTO {

    public String id;
    public String name;
    public String surname;
    public String email;
    public String password;
    public ApplicationUserRole role;

}
