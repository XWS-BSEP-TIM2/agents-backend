package com.dislinkt.agents.dto;


import lombok.Data;

@Data
public class ChangePasswordDTO {

    private String email;
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;

    public boolean passwordMatch() {
        return newPassword.equals(confirmNewPassword);
    }
}
