package com.dislinkt.agents.controller;

import com.dislinkt.agents.dto.QrCodeDto;
import com.dislinkt.agents.dto.UserDTO;
import com.dislinkt.agents.dto.VerifyQrCodeDto;
import com.dislinkt.agents.model.ApplicationUser;
import com.dislinkt.agents.security.JwtUtil;
import com.dislinkt.agents.security.model.AuthenticationRequest;
import com.dislinkt.agents.security.model.AuthenticationResponse;
import com.dislinkt.agents.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.jboss.aerogear.security.otp.Totp;
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

import javax.websocket.server.PathParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    @PostMapping
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword());
            authenticationManager.authenticate(token);
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect email or password.", e);
        }

        ApplicationUser user = userService.findByEmail(authenticationRequest.getEmail());

        String jwt="";
        boolean twoFactor=true;
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        if (!user.isUseTwoFactor()){
             jwt= jwtUtil.generateToken(userDetails);
             twoFactor=false;
        }

        return ResponseEntity.ok(new AuthenticationResponse(jwt, user.getFullName(), user.getEmail(), user.getId(), user.getRole(),twoFactor));
    }

    @GetMapping("/generate-qr/{id}")
    public ResponseEntity<QrCodeDto> generateQrCode(@PathVariable("id") String userId){
        String qr=userService.generateQUrl(userId);
        if (qr==null){
            return (ResponseEntity<QrCodeDto>) ResponseEntity.internalServerError();
        }else{
            return ResponseEntity.ok(new QrCodeDto(qr));
        }
    }

    @PostMapping("/verify-qr")
    public ResponseEntity<?> verifyQrCode(@RequestBody VerifyQrCodeDto dto){
        ApplicationUser user = userService.findById(dto.getUserId());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        Totp totp = new Totp(user.getSecret());
        if (!totp.verify(dto.getCode())) {
            throw new BadCredentialsException("Invalid verification code");
        }else{
            return ResponseEntity.ok(new AuthenticationResponse(jwtUtil.generateToken(userDetails), user.getFullName(), user.getEmail(), user.getId(), user.getRole(),true));
        }
    }



}
