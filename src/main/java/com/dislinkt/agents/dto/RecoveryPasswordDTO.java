package com.dislinkt.agents.dto;

import lombok.Data;

@Data
public class RecoveryPasswordDTO {

    private String email;
    private String recoveryCode;
    private String newPassword;
    private String confirmNewPassword;

    public boolean passwordMatch() {
        return newPassword.equals(confirmNewPassword);
    }
}
