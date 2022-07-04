package com.dislinkt.agents.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
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

    private boolean locked;
    private int numOfErrTryLogin;
    private LocalDateTime lastErrTryLoginTime;
    private String recoveryPasswordCode;
    private LocalDateTime recoveryPasswordCodeTime;
    private boolean verified;
    private String verificationCode;
    private LocalDateTime verificationCodeTime;

    public ApplicationUser(String id, String name, String surname, String email, String password, String apiToken, List<Role> roles, String secret, boolean useTwoFactor, boolean locked, boolean verified) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.apiToken = apiToken;
        this.roles = roles;
        this.secret = secret;
        this.useTwoFactor = useTwoFactor;
        this.locked = locked;
        this.verified = verified;
        this.numOfErrTryLogin = 0;
        this.lastErrTryLoginTime = LocalDateTime.now();
        this.recoveryPasswordCodeTime = LocalDateTime.now();
        this.verificationCodeTime = LocalDateTime.now();
        this.verificationCode = "";
        this.recoveryPasswordCode = "";
    }

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
        return !locked;
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

    public void recordErrorLoginTry() {
        numOfErrTryLogin++;
        lastErrTryLoginTime = LocalDateTime.now();
        if(numOfErrTryLogin > 5){
            locked = true;
        }
    }

    public void resetNumOfErrTryLogin(){
        numOfErrTryLogin = 0;
    }

    public boolean canTryLogin() {
        if(numOfErrTryLogin >= 5)
            return elapsedMinutes(60);
        if(numOfErrTryLogin == 4)
            return elapsedMinutes(15);
        if(numOfErrTryLogin == 3)
            return elapsedMinutes(3);

        return true;
    }

    private boolean elapsedMinutes(int minutes){
        return lastErrTryLoginTime.isBefore(LocalDateTime.now().minusMinutes(minutes));
    }

    public void setNewVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
        this.verificationCodeTime = LocalDateTime.now();
    }

    public boolean isVerificationCodeNotExpired() {
        return this.verificationCodeTime.isAfter(LocalDateTime.now().minusMinutes(10));
    }

    public void setNewRecoveryCode(String recoveryCode) {
        this.recoveryPasswordCode = recoveryCode;
        this.recoveryPasswordCodeTime = LocalDateTime.now();
    }

    public boolean isRecoveryCodeNotExpired() {
        return this.recoveryPasswordCodeTime.isAfter(LocalDateTime.now().minusMinutes(5));
    }

}
