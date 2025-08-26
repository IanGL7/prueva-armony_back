package com.ARmony.chatARmony.controller;

import com.ARmony.chatARmony.dto.MessageDto;
import com.ARmony.chatARmony.dto.RequestDto;
import com.ARmony.chatARmony.dto.ResponseDto;
import com.ARmony.chatARmony.service.ChatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@CrossOrigin // ajusta allowedOrigins si necesitas restringir
public class MainController {

    private final RestTemplate restTemplate;
    private final ChatService chatService;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    public MainController(RestTemplate restTemplate, ChatService chatService) {
        this.restTemplate = restTemplate;
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public ResponseEntity<MessageDto> chat(@RequestBody Map<String, Object> body) {
        Object p = body.get("prompt");
        String prompt = p == null ? "" : String.valueOf(p);
        RequestDto requestDto = new RequestDto(model, chatService.getPrompt(prompt));
        ResponseDto responseDto = restTemplate.postForObject(apiUrl, requestDto, ResponseDto.class);
        if (responseDto == null || responseDto.getChoices() == null || responseDto.getChoices().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(responseDto.getChoices().get(0).getMessage());
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }
}
