package com.dislinkt.agents.email.context;

import com.dislinkt.agents.model.ApplicationUser;

public class SendRecoveryCodeContext extends AbstractEmailContext {

    @Override
    public <T> void init(T context) {
        ApplicationUser user = (ApplicationUser) context;
        setTemplateLocation("emails/recovery");
        setSubject("Dislinkt Recovery Password");
        setFrom("dislinkt@outlook.com");
        setTo(user.getEmail());
        put("email", user.getName());
        put("code", user.getRecoveryPasswordCode());
    }
}
