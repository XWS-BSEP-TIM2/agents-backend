package com.dislinkt.agents.dto;

import lombok.Data;

import java.util.List;

@Data
public class JobOfferDTO {

    public String id;
    public CompanyDTO company;

    private String position;
    private String seniority;
    private String description;
    private List<String> technologies;

}
