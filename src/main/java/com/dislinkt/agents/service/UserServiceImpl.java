package com.dislinkt.agents.service;


import com.dislinkt.agents.model.ApplicationUser;
import com.dislinkt.agents.model.ApplicationUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ApplicationUser findByEmail(String email) {
        ApplicationUser user = new ApplicationUser("2", "Tara", "Pogancev", "t@g.c", "123", ApplicationUserRole.USER);
        mongoTemplate.save(user);
        return user;
    }

}
