package com.dislinkt.agents.controller;

import com.dislinkt.agents.model.ApplicationUser;
import com.dislinkt.agents.security.JwtUtil;
import com.dislinkt.agents.security.model.AuthenticationRequest;
import com.dislinkt.agents.security.model.AuthenticationResponse;
import com.dislinkt.agents.service.LoggingService;
import com.dislinkt.agents.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final LoggingService loggingService;


    @PostMapping
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword());
            authenticationManager.authenticate(token);
            loggingService.MakeInfoLog("User: "+ authenticationRequest.getEmail() + " logging in.");
        } catch (BadCredentialsException e) {
            loggingService.MakeWarningLog("User "+ authenticationRequest.getEmail() + " tryed to log in with bad credentials.");
            throw new Exception("Incorrect email or password.", e);
        }

        ApplicationUser user = userService.findByEmail(authenticationRequest.getEmail());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt, user.getFullName(), user.getEmail(), user.getId(), user.getRole()));
    }

    @GetMapping
    public String sendRequestTest(){
        RestTemplate restTemplate;
        RestTemplateBuilder restTemplateBuilder=new RestTemplateBuilder();
        restTemplate = restTemplateBuilder.build();
        String url = "http://localhost:9000/test";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+jwtUtil.generateApiToken("API","lln8Z4GiwdwJxzzxjil8GbaLpswZs"));
        final HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<String> ret=restTemplate.exchange(url, HttpMethod.GET,entity,String.class);
        return ret.toString();
    }

}
