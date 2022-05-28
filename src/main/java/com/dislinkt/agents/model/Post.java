package com.dislinkt.agents.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Post {

    @Id
    private String id;
    private String userId;
    private String companyId;
    private String jobOfferId;

    private PostType postType;
    private Date timestamp = new Date();

}
