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

    // Sends an object as JSON to the given URI (asynchronous).
    public static Mono<String> sendData(WebClient webClient, Object requestBody, String uri) {
        return webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class);
    }

    // Blocking version of sendData (synchronous).
    public static String sendDataBlocking(WebClient webClient, Object requestBody, String uri) {
        return sendData(webClient, requestBody, uri).block();
    }

    // Read containerData.json and send raw JSON text to the given URI (asynchronous).
    public static Mono<String> sendFileContent(WebClient webClient, Path filePath, String uri) throws Exception {
        String json = Files.readString(filePath, StandardCharsets.UTF_8);
        return webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .retrieve()
                .bodyToMono(String.class);
    }

    // Blocking version of sendFileContent (synchronous).
    public static String sendFileContentBlocking(WebClient webClient, Path filePath, String uri) throws Exception {
        return sendFileContent(webClient, filePath, uri).block();
    }
}