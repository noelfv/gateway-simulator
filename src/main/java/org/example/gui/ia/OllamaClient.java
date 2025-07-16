package org.example.gui.ia;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class OllamaClient {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    private final String model;

    public OllamaClient(String baseUrl, String model) {
        this.baseUrl = baseUrl;
        this.model = model;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public OllamaClient() {
        this("http://localhost:11434", "llama3.2:3b");
        //  this("http://localhost:11434", "deepseek-r1:1.5b");
    }

    public String queryDocument(String question, List<String> relevantChunks) throws IOException, InterruptedException {
        String context = String.join("\n\n", relevantChunks);

        String prompt = buildPrompt(question, context);

        ObjectNode requestJson = objectMapper.createObjectNode();
        requestJson.put("model", model);
        requestJson.put("prompt", prompt);
        requestJson.put("stream", false);
        requestJson.put("options", objectMapper.createObjectNode()
                .put("temperature", 0.3)
                .put("top_p", 0.9)
                .put("max_tokens", 500));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/generate"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestJson.toString()))
                .timeout(Duration.ofMinutes(2))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Error en Ollama API: " + response.statusCode() + " - " + response.body());
        }

        return parseResponse(response.body());
    }

    private String buildPrompt(String question, String context) {
        return String.format(
                "Eres un asistente especializado en responder preguntas sobre un manual de usuario. " +
                        "Utiliza ÚNICAMENTE la información del manual proporcionada a continuación para responder.\n\n" +
                        "MANUAL DE USUARIO:\n%s\n\n" +
                        "PREGUNTA: %s\n\n" +
                        "INSTRUCCIONES:\n" +
                        "- Responde únicamente basándote en la información del manual\n" +
                        "- Si la información no está en el manual, di que no se encuentra esa información\n" +
                        "- Sé claro y conciso\n" +
                        "- Incluye referencias específicas del manual cuando sea relevante\n\n" +
                        "RESPUESTA:",
                context, question
        );
    }

    private String parseResponse(String responseBody) throws IOException {
        ObjectNode responseJson = (ObjectNode) objectMapper.readTree(responseBody);
        return responseJson.get("response").asText();
    }

    public boolean isOllamaAvailable() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/api/tags"))
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }
}