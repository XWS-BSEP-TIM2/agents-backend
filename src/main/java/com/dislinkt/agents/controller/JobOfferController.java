package com.dislinkt.agents.controller;

import com.dislinkt.agents.dto.JobOfferCommentDTO;
import com.dislinkt.agents.dto.JobOfferDTO;
import com.dislinkt.agents.dto.UserDTO;
import com.dislinkt.agents.model.ApplicationUser;
import com.dislinkt.agents.model.JobOffer;
import com.dislinkt.agents.model.JobOfferComment;
import com.dislinkt.agents.service.interfaces.JobOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job-offer")
public class JobOfferController {

    private final JobOfferService jobOfferService;

    @PostMapping
    @PreAuthorize("hasRole('COMPANY_OWNER')")
    public JobOffer postNewOffer(@RequestBody JobOfferDTO jobOffer) {
        return jobOfferService.postNewOffer(jobOffer);
    }

    @PostMapping("/comment")
    @PreAuthorize("hasRole('USER')")
    public JobOfferComment postNewOfferComment(@RequestBody JobOfferCommentDTO comment) {
        return jobOfferService.postNewComment(comment);
    }

    @PutMapping
    @PreAuthorize("hasRole('COMPANY_OWNER')")
    public JobOffer updateOffer(@RequestBody JobOfferDTO jobOffer) {
        return jobOfferService.updateOffer(jobOffer);
    }

    @PutMapping("/delete")
    @PreAuthorize("hasRole('COMPANY_OWNER')")
    public boolean deleteOffer(@RequestBody JobOfferDTO jobOffer) {
        return jobOfferService.deleteOffer(jobOffer);
    }

}
