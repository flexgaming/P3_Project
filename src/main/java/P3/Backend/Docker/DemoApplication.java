package P3.Backend.Docker;

import static P3.Backend.Docker.Persistent.CURRENT_CONTAINER_PATH;
import P3.Backend.Docker.application.IntervalApplications;
import P3.Backend.Docker.application.SetupApplications;
import P3.Backend.Docker.builder.DockerClientBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.reactive.function.client.WebClient;

import com.github.dockerjava.api.DockerClient;


@SpringBootApplication
public class DemoApplication {

    // Name for JSON file containing all of the containers:
    private static final String containerFilename = CURRENT_CONTAINER_PATH;

    // The path for where the JSON file is stored.
    private static final Path containerListPath = Path.of("" + containerFilename); // Replace "" with desired path. Should be the same in SetupApplications.java and IntervalApplications.java

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        DockerClient dockerClient = DockerClientBuilder.dockerConnection();
        
        dockerClient.pingCmd().exec();

        // Get WebClient from Spring context
        WebClient webClient = context.getBean(WebClient.class);

        // Decide what part of the application user want to use:
        checkFileCreated();
        try {
            // Get all of the content within the file.
            String content = Files.readString(containerListPath);
            
            // Convert all of the content back into a JSON format.
            JSONObject JSONFileObj = new JSONObject(content);

            if (JSONFileObj.length() == 0) {
                System.out.println("The container JSON file is empty. Proceeding to Setup Applications.");
                SetupApplications.Initiation(dockerClient);
            }
        } catch (Exception e) {
            // If anything goes wrong, it is printed.
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);

        outer: while (true) {
            System.out.println("Select application mode:");
            System.out.println("1. Setup Applications");
            System.out.println("2. Interval Applications");
            System.out.println("3. Exit");
            System.out.print("Enter choice (1, 2 or 3): ");
            while (true) {
                String choice = scanner.nextLine();
                if (choice.equals("1")) {
                    System.out.println("You selected option " + choice);
                    
                    // If user selects Setup Applications, then proceed.
                    SetupApplications.Initiation(dockerClient);
                    break;
                } else if (choice.equals("2")) {
                    System.out.println("You selected option " + choice);
                    
                    // If user selects Interval Applications, then proceed.
                    IntervalApplications.Initiation(dockerClient, webClient);
                    break;
                } else if (choice.equals("3")) {
                    System.out.println("You selected option " + choice);
                    
                    // If user selects exit, then proceed.
                    break outer;
                } else {
                    System.out.print("Invalid choice. Please enter 1, 2 or 3: ");
                }
            }
        }
            
        // Close the scanner after use.
        scanner.close(); 
            
        //####################################//
        //####################################//
        
        // We have to send the region, company and container to the backend.
        // So remember to add the region and company parameters in the SetupApplications.

        //####################################//
        //####################################//

        //SetupApplications.Initiation(dockerClient);
	}

    /**
     * This function is used to check if the JSON file that contains all of the containers exists, 
     * if it does not exist then create one.
     */
    public static void checkFileCreated() {
        if (!Files.exists(containerListPath)) {
            // If the file does not exist, then it has to be created.
            try {
                // In order for it to be considered a JSON file, it has to contain {}.
                Files.writeString(containerListPath, "{}");
            } catch (Exception e) {
                // If anything goes wrong, it is printed.
                e.printStackTrace();
            }
        } 
    }

}
