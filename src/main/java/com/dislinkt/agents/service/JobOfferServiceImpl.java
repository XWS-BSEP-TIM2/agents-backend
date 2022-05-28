package com.dislinkt.agents.service;

import com.dislinkt.agents.dto.JobOfferCommentDTO;
import com.dislinkt.agents.dto.JobOfferDTO;
import com.dislinkt.agents.model.JobOffer;
import com.dislinkt.agents.model.JobOfferComment;
import com.dislinkt.agents.service.interfaces.JobOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobOfferServiceImpl implements JobOfferService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<JobOfferDTO> findAllDTO() {
        return null;
    }

    @Override
    public JobOfferDTO findByIdDTO(String offerId) {
        return null;
    }

    @Override
    public List<JobOfferCommentDTO> findCommentsByIdDTO(String offerId) {
        return null;
    }

    @Override
    public JobOffer postNewOffer(JobOfferDTO jobOffer) {
        return null;
    }

    @Override
    public JobOfferComment postNewComment(JobOfferCommentDTO comment) {
        return null;
    }

    @Override
    public JobOffer updateOffer(JobOfferDTO jobOffer) {
        return null;
    }

    @Override
    public boolean deleteOffer(JobOfferDTO jobOffer) {
        return false;
    }
}
