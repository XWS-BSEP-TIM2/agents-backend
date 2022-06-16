package com.dislinkt.agents.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Permission implements GrantedAuthority {
    private static final long serialVersionUID = 1L;

    @Id
    Long id;
    String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
