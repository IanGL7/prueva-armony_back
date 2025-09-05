package com.ARmony.chatARmony.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChatServiceTest {

    @Test
    void recommendsExerciseEveryThirdMessage() {
        ChatService service = new ChatService();
        String first = service.getPrompt("tristeza");
        String second = service.getPrompt("tristeza");
        String third = service.getPrompt("tristeza");

        assertThat(first).doesNotContain("ejercicio en realidad aumentada");
        assertThat(second).doesNotContain("ejercicio en realidad aumentada");
        assertThat(third).contains("Te recomiendo realizar el siguiente ejercicio en realidad aumentada");
    }
}
