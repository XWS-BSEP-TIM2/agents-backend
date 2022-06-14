package com.dislinkt.agents.service;

import com.dislinkt.agents.model.PasswordlessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class PasswordlessTokenLoginService implements com.dislinkt.agents.service.interfaces.PasswordlessTokenLoginService {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public PasswordlessToken findById(String id) {
        PasswordlessToken token = mongoTemplate.findById(id, PasswordlessToken.class);
        return token;
    }

    @Override
    public String create(PasswordlessToken token) {
        this.deleteUsersPasswordlessTokens(token.getUserId());
        PasswordlessToken persistedToken = mongoTemplate.save(token);
        return persistedToken.getId();
    }

    @Override
    public void deleteToken(String id) {
        PasswordlessToken token=mongoTemplate.findById(id,PasswordlessToken.class);
        mongoTemplate.remove(token);
    }

    @Override
    public void deleteUsersPasswordlessTokens(String userID) {
        List<PasswordlessToken> tokens = mongoTemplate.findAll(PasswordlessToken.class);
        for (PasswordlessToken token : tokens) {
            if (token.getUserId().equals(userID))
                deleteToken(token.getId());
        }
    }

    public PasswordlessToken getTokenByCode(String tokenCode) {
        List<PasswordlessToken> tokens = mongoTemplate.findAll(PasswordlessToken.class);
        for (PasswordlessToken token : tokens) {
            if (token.getCode().equals(tokenCode)) {
                return token;
            }
        }
        return null;
    }

    @Override
    public boolean verifyToken(String code) {
        PasswordlessToken token=this.getTokenByCode(code);
        boolean ret=true;
        if (token==null){
            ret=false;
        }
        Calendar date = Calendar.getInstance();
        System.out.println("Current Date and TIme : " + date.getTime());
        long timeInSecs = date.getTimeInMillis();
        Date afterAdding10Mins = new Date(timeInSecs + (10 * 60 * 1000));
        if (token.getTimestamp().after(afterAdding10Mins)){
            ret=false;
        }
        return ret;
    }
}
