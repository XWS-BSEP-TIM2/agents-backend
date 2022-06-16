package com.dislinkt.agents.model;

import com.dislinkt.agents.model.enums.ApplicationUserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class ApplicationUser implements UserDetails {

    @Id
    private String id;

    private String name;
    private String surname;
    private String email;
    private String password;
    public String apiToken;
    private List<Role> roles;
    private String secret;
    private boolean useTwoFactor;

    public String getFullName() {
        return name + " " + surname;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Permission> permissions = new HashSet<Permission>();
        for(Role role : this.roles){
            for(Permission permission : role.getPermissions()){
                permissions.add(permission);
            }
        }
        return permissions;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getRole(){
        return this.roles.get(0).getName();
    }
}
