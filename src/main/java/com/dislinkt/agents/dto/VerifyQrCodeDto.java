package com.dislinkt.agents.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyQrCodeDto {
    private String userId;
    private String code;
}
