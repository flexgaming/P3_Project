package P3.Backend.ExternalServer.Docker.manager;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class WebClientPost {

    /** Sends an object as JSON to the given URI (asynchronous), without expecting a return from the receiver.
     *
     * @param webClient Is used to send the POST request.
     * @param requestBody Is used as the body of the POST request.
     * @param uri Is used as the destination of the POST request.
     */
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