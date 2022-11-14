package com.assessment.messanger.exception;

import com.assessment.messanger.utils.constants.MessageConstants;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.function.BiFunction;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);
        errorAttributes.computeIfPresent(MessageConstants.TIMESTAMP, formatDateTime());
        errorAttributes.remove(MessageConstants.REQUEST_ID);
        return errorAttributes;
    }

    private static BiFunction<String, Object, Object> formatDateTime() {
        return (k, v) -> new SimpleDateFormat(MessageConstants.DATE_TIMESTAMP_PATTERN).format((Date) v);
    }
}