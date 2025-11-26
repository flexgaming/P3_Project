package P3.Backend.Docker.manager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class WebClientPost {

    // Send arbitrary object as JSON to the given URI (relative or absolute).
    public static Mono<String> sendData(WebClient webClient, Object requestBody, String uri) {
        return webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class);
    }

    // Blocking convenience wrapper
    public static String sendDataBlocking(WebClient webClient, Object requestBody, String uri) {
        return sendData(webClient, requestBody, uri).block();
    }

    // Read file as UTF-8 and send raw JSON text (useful when you already have containerData.json)
    public static Mono<String> sendFileContent(WebClient webClient, Path filePath, String uri) throws Exception {
        String json = Files.readString(filePath, StandardCharsets.UTF_8);
        return webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .retrieve()
                .bodyToMono(String.class);
    }

    public static String sendFileContentBlocking(WebClient webClient, Path filePath, String uri) throws Exception {
        return sendFileContent(webClient, filePath, uri).block();
    }
}