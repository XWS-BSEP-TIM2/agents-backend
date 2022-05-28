package com.dislinkt.agents.service;


import com.dislinkt.agents.dto.UserDTO;
import com.dislinkt.agents.model.ApplicationUser;
import com.dislinkt.agents.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<ApplicationUser> findAll() {
        return mongoTemplate.findAll(ApplicationUser.class);
    }

    @Override
    public ApplicationUser findById(String id) {
        return mongoTemplate.findById(id, ApplicationUser.class);
    }

    @Override
    public ApplicationUser findByEmail(String email) {
        for (ApplicationUser user : findAll()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public ApplicationUser registerNewUser(UserDTO newUser) {
        if (findByEmail(newUser.getEmail()) == null) {
            ApplicationUser user = new ApplicationUser(null, newUser.name,
                    newUser.surname, newUser.email, new BCryptPasswordEncoder().encode(newUser.password), newUser.role);
            return mongoTemplate.save(user);
        }
        return null;
    }

}
