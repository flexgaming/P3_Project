
package P3.Backend.ExternalServer.Docker.manager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {

    /**
     * This function is used to create a WebClinet config.
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
