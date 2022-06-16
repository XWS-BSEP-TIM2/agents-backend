package com.dislinkt.agents.email.context;

import com.dislinkt.agents.model.ApplicationUser;

public class SendVerificationCodeContext extends AbstractEmailContext{

    @Override
    public <T> void init(T context) {
        ApplicationUser user = (ApplicationUser) context;
        put("firstName", user.getName());
        setTemplateLocation("emails/verification");
        setSubject("Verification Acc");
        setFrom("dislinkt@outlook.com");
        setTo(user.getEmail());
        put("email", user.getName());
    }

    public void setRedirectUrl(final String verificationCode) {
        put("code", verificationCode);
    }


}
