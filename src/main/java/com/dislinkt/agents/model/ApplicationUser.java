package com.dislinkt.agents.model;

import com.dislinkt.agents.model.enums.ApplicationUserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class ApplicationUser {

    @Id
    private String id;

    private String name;
    private String surname;
    private String email;
    private String password;
    private ApplicationUserRole role;

    public String getFullName() {
        return name + " " + surname;
    }

}
