package com.dislinkt.agents.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateJobOfferOnDislinktDto {
    private String id;
    private String companyName;
    private String apiToken;
    private String position;
    private String seniority;
    private String description;
    private List<String> technologies;
}
