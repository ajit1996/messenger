package com.assessment.messanger.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "message")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
    @Id
    private long message_id;
    @JsonProperty(value = "user_id")
    @Field(value = "user_id")
    private String userId;
    private String topic;
    private String content;
    @JsonProperty(value = "created_on")
    @Field(value = "created_on")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
}
