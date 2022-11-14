package com.assessment.messanger.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class User {
    @Indexed(unique = true)
    @Id
    private String username;
    private String password;
    private String role;
}