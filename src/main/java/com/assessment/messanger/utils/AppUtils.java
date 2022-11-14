package com.assessment.messanger.utils;

import com.assessment.messanger.dto.MessageDto;
import com.assessment.messanger.entity.Message;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class AppUtils {

    public final static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    public static MessageDto entityToDto(Message message) {
        MessageDto messageDto = new MessageDto();
        BeanUtils.copyProperties(message, messageDto);
        LocalDateTime createdOn = message.getCreatedOn();
        if (Objects.nonNull(createdOn)) {
            String formattedDateTime = createdOn.format(formatter);
            messageDto.setCreatedOn(formattedDateTime);
        }
        return messageDto;
    }
}
