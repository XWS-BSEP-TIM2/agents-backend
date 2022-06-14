package com.dislinkt.agents.email.context;

import com.dislinkt.agents.model.ApplicationUser;

public class SendPasswordlessTokenContext extends AbstractEmailContext {
    @Override
    public <T> void init(T context) {
        //we can do any common configuration setup here
        // like setting up some base URL and context
        ApplicationUser user = (ApplicationUser) context;
        put("firstName", user.getName());
        setTemplateLocation("emails/magic-link");
        setSubject("Magic Link Login!");
        setFrom("isaprojectftn@outlook.com");
        setTo(user.getEmail());
    }

    public void setRedirectUrl(final String tokenCode) {
        put("code", tokenCode);
    }
}
