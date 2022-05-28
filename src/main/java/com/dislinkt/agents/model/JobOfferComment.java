package com.dislinkt.agents.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class JobOfferComment {

    @Id
    private String id;
    private String userId;
    private String jobOfferId;

    private Integer rating;
    private String comment;
    private Double salary;

}
