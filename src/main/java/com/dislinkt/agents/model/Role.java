package com.dislinkt.agents.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Role implements GrantedAuthority {
    @Id
    Long id;
    String name;

    private Set<Permission> permissions = new HashSet<Permission>();


    @Override
    public String getAuthority() {
        return name;
    }
}
