package com.dislinkt.agents.controller;

import com.dislinkt.agents.dto.QrCodeDto;
import com.dislinkt.agents.dto.VerifyQrCodeDto;
import com.dislinkt.agents.model.ApplicationUser;
import com.dislinkt.agents.model.PasswordlessToken;
import com.dislinkt.agents.security.JwtUtil;
import com.dislinkt.agents.security.model.AuthenticationRequest;
import com.dislinkt.agents.security.model.AuthenticationResponse;
import com.dislinkt.agents.service.PasswordlessTokenLoginService;
import com.dislinkt.agents.service.interfaces.MailingService;
import com.dislinkt.agents.service.LoggingService;
import com.dislinkt.agents.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final MailingService emailService;
    private final PasswordlessTokenLoginService passwordlessTokenRegistrationService;
    private final LoggingService loggingService;



    @PostMapping
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        ApplicationUser user = userService.findByEmail(authenticationRequest.getEmail());
        if(user == null){
            loggingService.MakeWarningLog("Tried to log in with not existing email.");
            return new ResponseEntity<>("Error: Incorrect email or password", HttpStatus.BAD_REQUEST);
        }

        if(user.isLocked()){
            loggingService.MakeWarningLog("User "+ authenticationRequest.getEmail() + " tried to login, rejected, acc is locked");
            return new ResponseEntity<>("Error: Your Acc is Locked, pleas recover", HttpStatus.BAD_REQUEST);
        }

        if(!user.isVerified()){
            loggingService.MakeWarningLog("User "+ authenticationRequest.getEmail() + " tried to login, rejected, acc is not verified");
            return new ResponseEntity<>("Error: Your Acc is not verified", HttpStatus.LOCKED);
        }

        if(!user.canTryLogin()){
            loggingService.MakeWarningLog("User "+ authenticationRequest.getEmail() + " tried to login, rejected, cool down");
            return new ResponseEntity<>("Error: Cool down period", HttpStatus.BAD_REQUEST);
        }

        try {
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword());
            authenticationManager.authenticate(token);
        } catch (BadCredentialsException e) {
            loggingService.MakeWarningLog("User "+ authenticationRequest.getEmail() + " tried to log in with bad credentials.");
            user.recordErrorLoginTry();
            userService.save(user);
            throw new Exception("Incorrect email or password.", e);
        }

        user.resetNumOfErrTryLogin();
        userService.save(user);

        String jwt="";
        boolean twoFactor=true;
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        if (!user.isUseTwoFactor()){
             jwt= jwtUtil.generateToken(userDetails);
             twoFactor=false;
        }

        loggingService.MakeInfoLog("User: "+ authenticationRequest.getEmail() + " Successfully logging in.");
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

    @GetMapping("send-mail/{mail}")
    public ResponseEntity<?> sendMail(@PathVariable("mail") String mail){
        if (emailService.sendMagicTokenMail(mail)){
            return new  ResponseEntity("Success", HttpStatus.OK);
        }else{
            return new  ResponseEntity("Wrong mail",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("verify-magic-link/{code}")
    public ResponseEntity<?> verifyMagicLink(@PathVariable("code") String tokenCode){

        if (passwordlessTokenRegistrationService.verifyToken(tokenCode)){
            PasswordlessToken token=passwordlessTokenRegistrationService.getTokenByCode(tokenCode);
            ApplicationUser user = userService.findById(token.getUserId());
            final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
            passwordlessTokenRegistrationService.deleteToken(token.getId());
            return ResponseEntity.ok(new AuthenticationResponse(jwtUtil.generateToken(userDetails), user.getFullName(), user.getEmail(), user.getId(), user.getRole(),user.isUseTwoFactor()));
        }else{
            return new ResponseEntity<>("Error",HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/verify-acc/{id}/{code}")
    public ResponseEntity<?> verifyAcc(@PathVariable("id") String id, @PathVariable("code") String code){
        if(userService.verifyAcc(id, code)){
            return new ResponseEntity<>("Successfully verified acc",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Error verification code are not valid or expired",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("resend-verify-code/{email}")
    public ResponseEntity<?> resendVerificationCode(@PathVariable("email") String email){
        ApplicationUser user = userService.resendVerificationCode(email);
        if(user != null){
            emailService.sendVerificationCodeMail(user);
            loggingService.MakeInfoLog("Successfully generated new verification code for user " + user.getEmail());
            return new ResponseEntity<>("Successfully generated new verification code",HttpStatus.OK);
        }else{
            loggingService.MakeWarningLog("Error resending verification code");
            return new ResponseEntity<>("Error",HttpStatus.BAD_REQUEST);
        }
    }



}
