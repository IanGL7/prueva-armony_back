package com.ARmony.chatARmony.service;

import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ChatService {

    // Contador global: cada 3 mensajes se agrega la invitación AR
    private final AtomicInteger turnCounter = new AtomicInteger(0);

    /**
     * Genera un prompt empático y EXTENSO para el LLM.
     * - Si el mensaje del usuario NO trata sobre emociones/bienestar emocional, el LLM debe responder con empatía
     *   que aquí solo se abordan temas emocionales (sin invitar a AR).
     * - Cada tercer mensaje (3, 6, 9, ...) añade la línea del ejercicio de Realidad Aumentada.
     *
     * @param emotion emoción guía (ej.: "ansiedad", "estrés", "tristeza") usada solo para elegir el ejercicio AR.
     */
    public String getPrompt(String emotion) {
        int n = turnCounter.incrementAndGet();
        boolean includeAR = (n % 3 == 0);
        String exercise = includeAR ? pickExercise(emotion, n) : "";

        StringBuilder prompt = new StringBuilder();
        prompt.append("""
                Eres **Emonical**, un chatbot de apoyo emocional. Habla con mucha calidez, cercanía y respeto,
                sin diagnosticar ni juzgar, y evita hacer promesas médicas.

                Objetivo: acompañar al usuario ante %s (p. ej., tristeza, ansiedad, estrés) con una respuesta
                clara, detallada y práctica.

                Reglas de tema:
                1)primero trata de sacar conversacion antes de jusgar el propocito.
                2) Si el mensaje del usuario NO está relacionado con emociones o bienestar emocional (p. ej., trámites,
                   matemáticas, recetas, programación, noticias u otros temas informativos, mensajes sin sentido), responde con empatía que aquí
                   solo puedes ayudar con apoyo emocional e invítalo amablemente a contarte cómo se siente.
                   En este caso, **no** incluyas invitación a ejercicio AR .

                Estilo y extensión cuando SÍ sea emocional:
                - Escribe un texto largo).
                - Mantén un tono cálido y validante, en **segunda persona**.
                - Estructura recomendada:
                  a) **Bloque empático (3–5 frases):**
                  b) **Cierre (1–2 frases):** .
                  c) **Genera consejos o Tips si el usuario lo pide con respecto al tema**
                - Si detectas señales de riesgo o urgencia emocional, prioriza un **mensaje de seguridad** y recomienda
                  buscar ayuda inmediata con personas de confianza o servicios de emergencia locales.

                Control:
                - include_ar_exercise: %s
                - ejercicio_ar_nombre: %s
                - mensaje_numero: %d

                Formato de salida:
                - Si el contenido del usuario está fuera de tema emocional:
                  Responde con 2–3 frases empáticas indicando que aquí te enfocas en apoyo emocional y sugiere que te cuente cómo se siente.
                  No incluyas invitación a ejercicio AR.
                - Si SÍ es emocional:
                  Redacta el texto extenso siguiendo el estilo y la estructura indicados.
                  
                 - Si el suario denega que no quiere la ayuda responder de manera empatica y finalizar
                 
                 - Si el usuario toca temas sencubles(ejem: suicidio, acdentes, situaciones bunerables, etc.) o situaciones que afecte la vida genrame una respuesta con el numero de emergencia dispoble en mexico  o alguuna linea de ayuda y que haga enfasis que acuda a un profesional.
                %s
                """.formatted(
                safe(emotion),
                includeAR ? "true" : "false",
                includeAR ? exercise : "",
                n,
                includeAR
                        ? "- Al final, añade exactamente: \"Te recomiendo realizar el siguiente ejercicio en realidad aumentada para calmarte: " + exercise + ".\""
                        : "- No incluyas invitación a ejercicio AR en este turno."
        ));

        return prompt.toString();
    }

    // ---------- Helpers para elegir un ejercicio AR según la emoción guía ----------

    private String pickExercise(String emotion, int n) {
        String e = normalize(emotion);

        List<String> ansiedad = List.of(
                "Respiración en caja 4-4-4-4 (AR)",
                "Anclaje 5-4-3-2-1 con burbujas (AR)",
                "Escaneo corporal 60s bajo cielo sereno (AR)"
        );
        List<String> estres = List.of(
                "Pausa 60s + exhalación lenta (AR)",
                "Soltar hombros y mandíbula (AR)",
                "Cuenta regresiva calmante 10→1 (AR)"
        );
        List<String> tristeza = List.of(
                "Respiración 4-7-8 con luz tenue (AR)",
                "Agradecimiento mínimo (1 cosa) (AR)",
                "Abrazo mariposa (tapping suave) (AR)"
        );

        List<String> pool =
                e.contains("ansied") ? ansiedad :
                        e.contains("estres")  ? estres   :
                                e.contains("triste")  ? tristeza :
                                        // genérico
                                        List.of(
                                                "Respiración en caja 4-4-4-4 (AR)",
                                                "Anclaje 5-4-3-2-1 con burbujas (AR)",
                                                "Escaneo corporal 60s bajo cielo sereno (AR)"
                                        );

        // n=3 -> idx 0, n=6 -> idx 1, n=9 -> idx 2, y así en carrusel
        int idx = Math.floorMod((n / 3) - 1, pool.size());
        return pool.get(idx);
    }

    private String normalize(String s) {
        if (s == null) return "";
        return Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .trim();
    }

    private String safe(String s) {
        return (s == null || s.isBlank()) ? "apoyo emocional" : s.trim();
    }

    // (Opcional) reinicia el contador, p. ej., al cerrar la conversación.
    public void resetCounter() {
        turnCounter.set(0);
    }
}
