package com.dislinkt.agents.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class PasswordlessToken {
    @Id
    private String id;
    private Date timestamp;
    private String code;
    private String userId;
}
