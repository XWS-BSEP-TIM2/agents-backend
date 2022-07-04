package com.dislinkt.agents.security;

import com.dislinkt.agents.model.ApplicationUser;
import com.dislinkt.agents.model.enums.ApplicationUserRole;
import com.dislinkt.agents.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String ROLE_PREFIX = "ROLE_";
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ApplicationUser user = userService.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        return new User(email, user.getPassword(), user.getAuthorities());
    }

    private List<SimpleGrantedAuthority> getRoles(ApplicationUserRole role) {
        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        String roleName = ROLE_PREFIX + role.toString().toUpperCase();
        grantedAuthorities.add(new SimpleGrantedAuthority(roleName));
        return grantedAuthorities;
    }

}