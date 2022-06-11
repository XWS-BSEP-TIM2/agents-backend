package com.dislinkt.agents.controller;

import com.dislinkt.agents.dto.JobOfferCommentDTO;
import com.dislinkt.agents.dto.JobOfferDTO;
import com.dislinkt.agents.model.JobOffer;
import com.dislinkt.agents.model.JobOfferComment;
import com.dislinkt.agents.service.LoggingService;
import com.dislinkt.agents.service.interfaces.JobOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job-offer")
public class JobOfferController {

    private final JobOfferService jobOfferService;
    private final LoggingService loggingService;

    @PostMapping
    @PreAuthorize("hasRole('COMPANY_OWNER')")
    public JobOffer postNewOffer(@RequestBody JobOfferDTO jobOffer) {
        return jobOfferService.postNewOffer(jobOffer);
    }

    @PostMapping("/comment")
    @PreAuthorize("hasRole('USER')")
    public JobOfferComment postNewOfferComment(@RequestBody JobOfferCommentDTO comment) {
        loggingService.MakeInfoLog("User "+ comment.user.getEmail() + " made a comment.");
        return jobOfferService.postNewComment(comment);
    }

    @PutMapping
    @PreAuthorize("hasRole('COMPANY_OWNER')")
    public JobOffer updateOffer(@RequestBody JobOfferDTO jobOffer) {
        loggingService.MakeInfoLog("Owner of "+ jobOffer.getCompany().getName() + " company made a offer.");
        return jobOfferService.updateOffer(jobOffer);
    }

    @PutMapping("/delete")
    @PreAuthorize("hasRole('COMPANY_OWNER')")
    public boolean deleteOffer(@RequestBody JobOfferDTO jobOffer) {
        loggingService.MakeInfoLog("Owner of "+ jobOffer.getCompany().getName() + " company deleted a offer.");
        return jobOfferService.deleteOffer(jobOffer);
    }

}
