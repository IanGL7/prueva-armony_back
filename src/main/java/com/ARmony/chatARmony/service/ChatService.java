package com.ARmony.chatARmony.service;

import org.springframework.stereotype.Service;

@Service
public class ChatService {
    public String getPrompt(String emotion) {
        String prompt = "Si " + emotion +
                "en caso contrario, ofrece una breve sugerencia sobre cómo manejar la emoción de " + emotion +
                " con un ejercicio de relajación o mindfulness. Finaliza con la frase: 'Te recomiendo realizar el siguiente ejercicio en realidad aumentada para calmarte: [nombre del ejercicio]'.";

        return prompt;
    }

}
