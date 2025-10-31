package P3.Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
	public static void main(String[] args) {
        // Run main using Spring Boot
		SpringApplication.run(App.class, args);

        Database db = new Database();
	}

}
