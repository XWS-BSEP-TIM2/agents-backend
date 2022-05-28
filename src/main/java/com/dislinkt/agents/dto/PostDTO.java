package com.dislinkt.agents.dto;

import com.dislinkt.agents.model.enums.PostType;
import lombok.Data;

import java.util.Date;

@Data
public class PostDTO {

    public String id;
    public UserDTO user;
    public CompanyDTO company;
    public JobOfferDTO jobOffer;
    public PostType type;
    public Date timestamp;
}
