package com.ARmony.chatARmony.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDto {

    private List<Choice> choices;

    @Data
    public static class Choice {
        private MessageDto message;
        private int index;
    }
}
