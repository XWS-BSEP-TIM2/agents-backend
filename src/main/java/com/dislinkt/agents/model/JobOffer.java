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
public class JobOffer {

    @Id
    private String id;
    private String companyId;
    private String userId;
    private String position;
    private String seniority;
    private String description;
    private List<String> technologies;
    private boolean promotedOnDislinkt;

}
