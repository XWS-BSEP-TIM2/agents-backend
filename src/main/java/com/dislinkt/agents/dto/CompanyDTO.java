package com.dislinkt.agents.dto;

import lombok.Data;

import java.util.List;

@Data
public class CompanyDTO {

    public String id;
    public UserDTO user;
    public String name;
    public String description;
    public String tagline;
    public List<String> technologies;
    public List<String> emailList;
    public List<String> phoneNumberList;

}
