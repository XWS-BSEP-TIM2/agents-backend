package com.dislinkt.agents.service.interfaces;

import com.dislinkt.agents.dto.CompanyDTO;
import com.dislinkt.agents.dto.PostDTO;

import java.util.List;

public interface PostService {
    List<PostDTO> findAllDTO();

    PostDTO findByIdDTO(String postId);
}
