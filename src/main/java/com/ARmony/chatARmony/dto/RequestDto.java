package com.ARmony.chatARmony.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.message.Message;
import java.util.ArrayList;
import java.util.List;



@NoArgsConstructor
@Data
public class RequestDto {

    private String model;
    private List<MessageDto> messages;

    public RequestDto(String model, String prompt) {
        this.model = model;
        messages = new ArrayList<>();
        messages.add(new MessageDto("user", prompt));
    }
}

