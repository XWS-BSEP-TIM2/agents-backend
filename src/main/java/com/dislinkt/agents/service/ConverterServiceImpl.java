package com.dislinkt.agents.service;

import com.dislinkt.agents.dto.*;
import com.dislinkt.agents.model.*;
import com.dislinkt.agents.service.interfaces.ConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class ConverterServiceImpl implements ConverterService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public PostDTO postToDto(Post post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTimestamp(post.getTimestamp());
        dto.setType(post.getPostType());

        switch (post.getPostType()) {
            case NEW_USER:
                if (post.getUserId() != null) {
                    ApplicationUser user = mongoTemplate.findById(post.getUserId(), ApplicationUser.class);
                    dto.setUser(userToDto(user));
                }
                break;

            case NEW_COMPANY:
                if (post.getCompanyId() != null) {
                    Company company = mongoTemplate.findById(post.getCompanyId(), Company.class);
                    dto.setCompany(companyToDto(company));
                }
                break;

            case NEW_JOB_OFFER:
                if (post.getJobOfferId() != null) {
                    JobOffer jobOffer = mongoTemplate.findById(post.getJobOfferId(), JobOffer.class);
                    dto.setJobOffer(jobOfferToDto(jobOffer));
                }
                break;
        }

        return dto;
    }

    @Override
    public UserDTO userToDto(ApplicationUser user) {
        UserDTO dto = new UserDTO();
        dto.setEmail(user.getEmail());
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setRole(user.getRole());
        dto.setApiToken(user.getApiToken());
        return dto;
    }

    @Override
    public CompanyDTO companyToDto(Company company) {
        CompanyDTO dto = new CompanyDTO();
        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setDescription(company.getDescription());
        dto.setEmailList(company.getEmailList());
        dto.setPhoneNumberList(company.getPhoneNumberList());
        dto.setTagline(company.getTagline());
        dto.setTechnologies(company.getTechnologies());

        ApplicationUser companyOwner = mongoTemplate.findById(company.getUserId(), ApplicationUser.class);
        dto.setUser(userToDto(companyOwner));

        return dto;
    }

    @Override
    public JobOfferDTO jobOfferToDto(JobOffer jobOffer) {
        JobOfferDTO dto = new JobOfferDTO();
        dto.setDescription(jobOffer.getDescription());
        dto.setId(jobOffer.getId());
        dto.setTechnologies(jobOffer.getTechnologies());
        dto.setPosition(jobOffer.getPosition());
        dto.setSeniority(jobOffer.getSeniority());

        Company company = mongoTemplate.findById(jobOffer.getCompanyId(), Company.class);
        dto.setCompany(companyToDto(company));

        return dto;
    }

    @Override
    public JobOfferCommentDTO commentToDto(JobOfferComment comment) {
        JobOfferCommentDTO dto = new JobOfferCommentDTO();
        dto.setId(comment.getId());
        dto.setRating(comment.getRating());
        dto.setSalary(comment.getSalary());
        dto.setComment(comment.getComment());

        ApplicationUser user = mongoTemplate.findById(comment.getUserId(), ApplicationUser.class);
        JobOffer jobOffer = mongoTemplate.findById(comment.getJobOfferId(), JobOffer.class);

        dto.setUser(userToDto(user));
        dto.setJobOffer(jobOfferToDto(jobOffer));

        return dto;
    }

    @Override
    public CompanyOwnerRequestDTO requestToDto(CompanyOwnerRequest request) {
        CompanyOwnerRequestDTO dto = new CompanyOwnerRequestDTO();
        dto.setId(request.getId());
        dto.setTimestamp(request.getTimestamp());
        dto.setAccepted(request.getAccepted());
        Company company = mongoTemplate.findById(request.getCompanyId(), Company.class);
        dto.setCompany(companyToDto(company));
        return dto;
    }
}
