package com.dislinkt.agents.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Company {

    @Id
    private String id;
    private String userId;

    private String name;
    private String description;
    private String tagline;
    private List<String> technologies;
    private List<String> emailList;
    private List<String> phoneNumberList;

}
