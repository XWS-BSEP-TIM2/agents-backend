package com.dislinkt.agents.email.service;

import com.dislinkt.agents.email.context.AbstractEmailContext;

public interface EmailService {
    void sendMail(final AbstractEmailContext email) ;

}
