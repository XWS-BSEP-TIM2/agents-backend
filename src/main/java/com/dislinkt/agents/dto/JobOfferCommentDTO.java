package com.dislinkt.agents.dto;

import lombok.Data;

@Data
public class JobOfferCommentDTO {

    public String id;
    public UserDTO user;
    public JobOfferDTO jobOffer;
    public Integer rating;
    public String comment;
    public Double salary;

}
