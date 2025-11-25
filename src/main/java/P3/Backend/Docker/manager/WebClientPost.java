package P3.Backend.Docker.manager;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class WebClientPost {


/*     public Mono<String> sendData(WebClient webClient, Object request) {
        return webClient
                .post()
                .uri("/api/upload-json") // <-- replace with real path (e.g. "localhost:port/api/upload-json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class);
    } */


/*     public String sendDataBlocking(WebClient webClient, Object request) {
        return sendData(webClient, request).block();
    } */
} 