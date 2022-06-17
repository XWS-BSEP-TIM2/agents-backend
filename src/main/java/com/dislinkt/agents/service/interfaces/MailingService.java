package com.dislinkt.agents.service.interfaces;

import com.dislinkt.agents.model.ApplicationUser;

public interface MailingService {

    boolean sendMagicTokenMail(String userMail);

    boolean sendVerificationCodeMail(ApplicationUser user);

    boolean sendRecoveryCodeMail(ApplicationUser user);
}
