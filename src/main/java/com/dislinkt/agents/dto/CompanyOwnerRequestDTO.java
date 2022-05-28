package com.dislinkt.agents.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CompanyOwnerRequestDTO {

    public String id;
    public Date timestamp;
    public CompanyDTO company;
    public Boolean accepted;

}
