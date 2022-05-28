package com.dislinkt.agents.controller;

import com.dislinkt.agents.converter.DataConverter;
import com.dislinkt.agents.dto.*;
import com.dislinkt.agents.model.ApplicationUser;
import com.dislinkt.agents.service.interfaces.CompanyService;
import com.dislinkt.agents.service.interfaces.JobOfferService;
import com.dislinkt.agents.service.interfaces.PostService;
import com.dislinkt.agents.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/browse")
public class BrowseController {

    private final UserService userService;
    private final CompanyService companyService;
    private final PostService postService;
    private final JobOfferService jobOfferService;
    private final DataConverter converter;

    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        List<ApplicationUser> users = this.userService.findAll();
        return converter.convert(users, UserDTO.class);
    }

    @GetMapping("/user/{userId}")
    public UserDTO getUser(@PathVariable String userId) {
        return this.userService.findByIdDTO(userId);
    }

    @GetMapping("/companies")
    public List<CompanyDTO> getAllCompanies() {
        return this.companyService.findAllDTO();
    }

    @GetMapping("/company/{companyId}")
    public CompanyDTO getCompany(@PathVariable String companyId) {
        return this.companyService.findByIdDTO(companyId);
    }

    @GetMapping("/posts")
    public List<PostDTO> getAllPosts() {
        return this.postService.findAllDTO();
    }

    @GetMapping("/post/{postId}")
    public PostDTO getPost(@PathVariable String postId) {
        return this.postService.findByIdDTO(postId);
    }

    @GetMapping("/job-offers")
    public List<JobOfferDTO> getAllJobOffers() {
        return this.jobOfferService.findAllDTO();
    }

    @GetMapping("/job-offer/{offerId}")
    public JobOfferDTO getJobOffer(@PathVariable String offerId) {
        return this.jobOfferService.findByIdDTO(offerId);
    }

    @GetMapping("/job-offer/comments/{offerId}")
    public List<JobOfferCommentDTO> getJobOfferComments(@PathVariable String offerId) {
        return this.jobOfferService.findCommentsByIdDTO(offerId);
    }

}
