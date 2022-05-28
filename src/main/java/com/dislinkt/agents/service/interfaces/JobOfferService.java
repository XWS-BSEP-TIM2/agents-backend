package com.dislinkt.agents.service.interfaces;

import com.dislinkt.agents.dto.JobOfferCommentDTO;
import com.dislinkt.agents.dto.JobOfferDTO;
import com.dislinkt.agents.model.JobOffer;
import com.dislinkt.agents.model.JobOfferComment;

import java.util.List;

public interface JobOfferService {
    List<JobOfferDTO> findAllDTO();

    JobOfferDTO findByIdDTO(String offerId);

    List<JobOfferCommentDTO> findCommentsByIdDTO(String offerId);

    JobOffer postNewOffer(JobOfferDTO jobOffer);

    JobOfferComment postNewComment(JobOfferCommentDTO comment);

    JobOffer updateOffer(JobOfferDTO jobOffer);

    boolean deleteOffer(JobOfferDTO jobOffer);
}
