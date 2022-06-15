package com.dislinkt.agents.service;

import com.dislinkt.agents.email.context.SendPasswordlessTokenContext;
import com.dislinkt.agents.email.service.EmailService;
import com.dislinkt.agents.model.ApplicationUser;
import com.dislinkt.agents.model.PasswordlessToken;
import com.dislinkt.agents.service.interfaces.MailingService;
import com.dislinkt.agents.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class MailingServiceImpl implements MailingService {
    private final EmailService emailService;
    private final UserService userService;
    private final PasswordlessTokenLoginService tokenRegistrationService;
    private final String AGENTS_URL="https://localhost:4201";

    @Override
    public boolean sendMagicTokenMail(String userMail) {

        SendPasswordlessTokenContext context=new SendPasswordlessTokenContext();
        context.setRedirectUrl("");
        ApplicationUser user=userService.findByEmail(userMail);
        if (user==null){
            return false;
        }
        PasswordlessToken token=new PasswordlessToken();
        token.setId("");
        token.setCode(generateCode());
        token.setUserId(user.getId());
        token.setTimestamp(new Date());
        String tokenId=tokenRegistrationService.create(token);
        context.setRedirectUrl(AGENTS_URL+"/magic-link/"+token.getCode());
        context.init(user);
        emailService.sendMail(context);
        return true;
    }

    private String generateCode(){
        Random rand = new Random();

        String str = rand.ints(48, 123)

                .filter(num -> (num<58 || num>64) && (num<91 || num>96))

                .limit(15)

                .mapToObj(c -> (char)c).collect(StringBuffer::new, StringBuffer::append, StringBuffer::append)

                .toString();

        return str;

    }
}
