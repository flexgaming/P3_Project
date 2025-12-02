package P3.Backend.ExternalServer.Docker;

import static P3.Backend.ExternalServer.Docker.Persistent.CONTAINER_NAME;
import static P3.Backend.ExternalServer.Docker.Persistent.COMPANY_INFO;
import static P3.Backend.ExternalServer.Docker.Persistent.CURRENT_CONTAINER_PATH;
import P3.Backend.ExternalServer.Docker.application.IntervalApplications;
import P3.Backend.ExternalServer.Docker.application.SetupApplications;
import P3.Backend.ExternalServer.Docker.builder.DockerClientBuilder;

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

    // The path for where the JSON file is stored.
    private static final Path containerListPath = Path.of(CURRENT_CONTAINER_PATH + CONTAINER_NAME);

    // The path for where the company info JSON file is stored.
    private static final Path companyInfoPath = Path.of(CURRENT_CONTAINER_PATH + COMPANY_INFO);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        DockerClient dockerClient = DockerClientBuilder.dockerConnection();
        
        dockerClient.pingCmd().exec();

        // Get WebClient from Spring context
        WebClient webClient = context.getBean(WebClient.class);
        
        // Get user input for selecting application mode.
        Scanner scanner = new Scanner(System.in);

        // Check if the JSON file that contains all of the containers exists, if not create one.
        checkFileCreated();
        try {
            // Get all of the company information from the JSON file with all company info.
            String info = Files.readString(companyInfoPath);

            // Convert all of the company info back into a JSON format.
            JSONObject companyInfoObj = new JSONObject(info);

            // Go through each of the company info fields and check if any of them are empty.
            for (String key : companyInfoObj.keySet()) {
                if (companyInfoObj.getString(key).isEmpty()) {

                    // If any of the company info fields are empty, then give a warning and exit the application.
                    System.out.println("==============================================================");
                    System.out.println("\n\n\n" + "Remember to fill in the company info in the JSON file: " + COMPANY_INFO + "\n");
                    System.out.println("It is " + "\u001B[1;4;31m" +  "VERY important" + "\u001B[0;37m" + " that the fields are correctly filled out!" + "\n\n\n");
                    System.out.println("==============================================================");
                    System.exit(0);
                }
            }

            // Get all of the content within the file.
            String content = Files.readString(containerListPath);
            
            // Convert all of the content back into a JSON format.
            JSONObject JSONFileObj = new JSONObject(content);
            
            if (JSONFileObj.length() == 0) {
                System.out.println("\n\nThe container JSON file is empty. Proceeding to Setup Applications.\n\n");
                SetupApplications.Initiation(dockerClient, scanner);
            }
        } catch (Exception e) {
            // If anything goes wrong, it is printed.
            e.printStackTrace();
        }
        
        // Decide what part of the application user want to use:
        outer: while (true) {
            printApplicationChoices(); // Print the choices
            System.out.print("Enter choice (1, 2 or 3): ");
            String choice;
            while (true) {
                choice = scanner.nextLine();
                if (choice.equals("1")) {
                    System.out.println("You selected option " + choice);
                    
                    // If user selects Setup Applications, then proceed.
                    SetupApplications.Initiation(dockerClient, scanner);

                    printApplicationChoices(); // Print the choices
                } else if (choice.equals("2")) {
                    System.out.println("You selected option " + choice);
                    
                    // If user selects Interval Applications, then proceed.
                    IntervalApplications.Initiation(dockerClient, webClient, scanner);

                    printApplicationChoices(); // Print the choices
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
        System.exit(0);
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
        if (!Files.exists(companyInfoPath)) {
            // If the file does not exist, then it has to be created.
            try {
                // In order for it to be considered a JSON file, it has to contain a template.
                System.out.println("Creating company info JSON file.");

                // Create a template for the company info JSON file.
                JSONObject template = new JSONObject();
                template.put("CompanyName", "");
                template.put("CompanyRegion", "");
                template.put("CompanyServer", "");

                Files.writeString(companyInfoPath, template.toString(4));

            } catch (Exception e) {
                // If anything goes wrong, it is printed.
                e.printStackTrace();
            }
        }
    }

    private static void printApplicationChoices() {
        System.out.println("\nSelect application mode:");
        System.out.println("1. Setup Applications");
        System.out.println("2. Interval Applications");
        System.out.println("3. Exit");
    }

}
