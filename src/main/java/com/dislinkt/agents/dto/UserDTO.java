package com.dislinkt.agents.dto;

import com.dislinkt.agents.model.enums.ApplicationUserRole;
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
