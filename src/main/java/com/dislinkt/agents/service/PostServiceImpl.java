package com.dislinkt.agents.service;

import com.dislinkt.agents.dto.PostDTO;
import com.dislinkt.agents.service.interfaces.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<PostDTO> findAllDTO() {
        return null;
    }

    @Override
    public PostDTO findByIdDTO(String postId) {
        return null;
    }
}
