package com.dislinkt.agents.service;

import com.dislinkt.agents.dto.JobOfferCommentDTO;
import com.dislinkt.agents.dto.JobOfferDTO;
import com.dislinkt.agents.model.JobOffer;
import com.dislinkt.agents.model.JobOfferComment;
import com.dislinkt.agents.model.Post;
import com.dislinkt.agents.model.enums.PostType;
import com.dislinkt.agents.service.interfaces.ConverterService;
import com.dislinkt.agents.service.interfaces.JobOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobOfferServiceImpl implements JobOfferService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ConverterService converterService;

    @Override
    public List<JobOfferDTO> findAllDTO() {
        List<JobOfferDTO> retVal = new ArrayList<>();
        for (JobOffer jobOffer: mongoTemplate.findAll(JobOffer.class)) {
            retVal.add(converterService.jobOfferToDto(jobOffer));
        }
        return retVal;
    }

    @Override
    public JobOfferDTO findByIdDTO(String offerId) {
        JobOffer jobOffer = mongoTemplate.findById(offerId, JobOffer.class);
        if (jobOffer != null) {
            return converterService.jobOfferToDto(jobOffer);
        } else {
            return null;
        }
    }

    @Override
    public List<JobOfferCommentDTO> findCommentsByIdDTO(String offerId) {
        List<JobOfferCommentDTO> retVal = new ArrayList<>();
        for (JobOfferComment comment: mongoTemplate.findAll(JobOfferComment.class)) {
            if (comment.getJobOfferId().equals(offerId)) {
                retVal.add(converterService.commentToDto(comment));
            }
        }
        return retVal;
    }

    @Override
    public JobOffer postNewOffer(JobOfferDTO jobOffer) {
        JobOffer newOffer = new JobOffer();
        newOffer.setCompanyId(jobOffer.company.id);
        newOffer.setDescription(jobOffer.getDescription());
        newOffer.setPosition(jobOffer.getPosition());
        newOffer.setTechnologies(jobOffer.getTechnologies());
        newOffer.setSeniority(jobOffer.getSeniority());
        newOffer.setPosition(jobOffer.getPosition());
        newOffer.setUserId(jobOffer.company.user.id);
        newOffer = mongoTemplate.save(newOffer);

        Post post = new Post();
        post.setJobOfferId(newOffer.getId());
        post.setPostType(PostType.NEW_JOB_OFFER);
        mongoTemplate.save(post);

        return newOffer;
    }

    @Override
    public JobOfferComment postNewComment(JobOfferCommentDTO comment) {
        JobOfferComment jobOfferComment = new JobOfferComment();
        jobOfferComment.setComment(comment.getComment());
        jobOfferComment.setJobOfferId(comment.getJobOffer().id);
        jobOfferComment.setRating(comment.getRating());
        jobOfferComment.setSalary(comment.getSalary());
        jobOfferComment.setUserId(comment.getUser().id);
        return mongoTemplate.save(jobOfferComment);
    }

    @Override
    public JobOffer updateOffer(JobOfferDTO jobOffer) {
        JobOffer updatedOffer = mongoTemplate.findById(jobOffer.id, JobOffer.class);
        if (updatedOffer != null) {
            updatedOffer.setDescription(jobOffer.getDescription());
            updatedOffer.setPosition(jobOffer.getPosition());
            updatedOffer.setTechnologies(jobOffer.getTechnologies());
            updatedOffer.setSeniority(jobOffer.getSeniority());
            updatedOffer.setPosition(jobOffer.getPosition());
            updatedOffer = mongoTemplate.save(updatedOffer);
        }

        return updatedOffer;
    }

    @Override
    public boolean deleteOffer(JobOfferDTO jobOffer) {
        JobOffer offer = mongoTemplate.findById(jobOffer.id, JobOffer.class);
        if (offer != null) {
            for (Post post: mongoTemplate.findAll(Post.class)) {
                if (post.getJobOfferId().equals(offer.getId())) {
                    mongoTemplate.remove(post);
                }
            }

            for (JobOfferComment comment: mongoTemplate.findAll(JobOfferComment.class)) {
                if (comment.getJobOfferId().equals(offer.getId())) {
                    mongoTemplate.remove(comment);
                }
            }

            mongoTemplate.remove(offer);

            return true;
        } else {
            return false;
        }
    }
}
