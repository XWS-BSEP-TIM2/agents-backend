package com.dislinkt.agents.service.interfaces;

import com.dislinkt.agents.model.PasswordlessToken;

public interface PasswordlessTokenLoginService {
    PasswordlessToken findById(String id);

    String create(PasswordlessToken token);

    void deleteToken(String id);

    void deleteUsersPasswordlessTokens(String userID);

    PasswordlessToken getTokenByCode(String tokenCode);

    boolean verifyToken(String code);
}
