package com.dislinkt.agents.service;

import com.dislinkt.agents.dto.PostDTO;
import com.dislinkt.agents.model.Post;
import com.dislinkt.agents.service.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ConverterService converterService;

    @Override
    public List<PostDTO> findAllDTO() {
        List<PostDTO> retVal = new ArrayList<>();
        for (Post post: mongoTemplate.findAll(Post.class)) {
            retVal.add(converterService.postToDto(post));
        }

        return retVal;
    }

    @Override
    public PostDTO findByIdDTO(String postId) {
        Post post = mongoTemplate.findById(postId, Post.class);
        if (post == null) {
            return null;
        } else {
            return converterService.postToDto(post);
        }
    }

}
