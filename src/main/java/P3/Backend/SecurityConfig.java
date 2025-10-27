package P3.Backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

// |||||||||||||||||||||||||||||||||||||||||||||||||||
// |||  Security Configuration  (wtf is going on)  |||
// |||||||||||||||||||||||||||||||||||||||||||||||||||

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Disable CSRF for REST API
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/dockers/**").permitAll()  // Allow all Docker endpoints
                .anyRequest().authenticated()
            );
        
        return http.build();
    }
}