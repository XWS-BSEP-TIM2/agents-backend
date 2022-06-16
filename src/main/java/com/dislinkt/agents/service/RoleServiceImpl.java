package com.dislinkt.agents.service;

import com.dislinkt.agents.model.Role;
import com.dislinkt.agents.service.interfaces.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Role findRoleByID(Long id) {
       return mongoTemplate.findById(id, Role.class);
    }
}
