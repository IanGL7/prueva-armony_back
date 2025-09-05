package com.ARmony.chatARmony.service;

import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private int messageCount = 0;

    public String getPrompt(String context) {
        messageCount++;
        String prompt = """
                Eres ARmony, un asistente dedicado a la gestión emocional. Responde con énfasis y empatía.
                Si la petición del usuario está fuera de lugar o no se relaciona con la gestión emocional, respóndele con calidez y aclara que estás para apoyar en la regulación emocional.
                No sugieras ejercicios de relajación ni mindfulness hasta que el usuario brinde un contexto breve.
                Cuando exista ese contexto, ofrece una sugerencia para manejar la emoción de %s.
                """.formatted(context);

        if (messageCount % 3 == 0) {
            prompt += " Te recomiendo realizar el siguiente ejercicio en realidad aumentada para calmarte: [nombre del ejercicio].";
        }

        return prompt;
    }
}
