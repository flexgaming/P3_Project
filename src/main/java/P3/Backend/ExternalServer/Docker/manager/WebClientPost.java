package P3.Backend.ExternalServer.Docker.manager;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class WebClientPost {

    public static void sendData(WebClient webClient, Object requestBody, String uri) {
        webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchangeToMono(response -> response.releaseBody())
                .onErrorResume(e -> Mono.empty())
                .subscribe();
    }
}