package P3.Backend.ExternalServer.application;

import static P3.Backend.ExternalServer.Docker.Persistent.CONTAINER_NAME;
import static P3.Backend.ExternalServer.Docker.Persistent.SERVER_INFO;
import static P3.Backend.ExternalServer.Docker.Persistent.CURRENT_CONTAINER_PATH;

import P3.Backend.ExternalServer.Docker.builder.DockerClientBuilder;
import P3.Backend.ExternalServer.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.function.client.WebClient;

import com.github.dockerjava.api.DockerClient;

@SpringBootApplication
@ComponentScan(basePackages = {"P3.Backend.ExternalServer"})
public class ExternalApplication {

    private static final Path containerListPath = Path.of(CURRENT_CONTAINER_PATH + CONTAINER_NAME);

    private static final Path serverInfoPath = Path.of(CURRENT_CONTAINER_PATH + SERVER_INFO);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ExternalApplication.class, args);
        DockerClient dockerClient = DockerClientBuilder.dockerConnection();

        WebClient webClient = context.getBean(WebClient.class);

        Scanner scanner = new Scanner(System.in);

        checkFileCreated();
        try {
            String info = Files.readString(serverInfoPath);

            JSONObject companyInfoObj = new JSONObject(info);

            for (String key : companyInfoObj.keySet()) {
                if (companyInfoObj.getString(key).isEmpty()) {

                    System.out.println("==============================================================");
                    System.out.println("\n\n\n" + "Remember to fill in the company info in the JSON file: " + SERVER_INFO + "\n");
                    System.out.println("It is " + "\u001B[1;4;31m" +  "VERY important" + "\u001B[0;37m" + " that the fields are correctly filled out!" + "\n\n\n");
                    System.out.println("==============================================================");
                    System.exit(0);
                }
            }
            String content = Files.readString(containerListPath);

            JSONObject JSONFileObj = new JSONObject(content);

            if (JSONFileObj.length() == 0) {
                System.out.println("\n\nThe container JSON file is empty. Proceeding to Setup Applications.\n\n");
                SetupApplications.initiation(dockerClient, scanner);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        outer: while (true) {
            printApplicationChoices(); 
            String choice;
            while (true) {
                choice = scanner.nextLine();
                if (choice.equals("1")) {
                    System.out.println("You selected option " + choice);

                    SetupApplications.initiation(dockerClient, scanner);

                    printApplicationChoices(); 
                } else if (choice.equals("2")) {
                    System.out.println("You selected option " + choice);

                    IntervalApplications.initiation(dockerClient, webClient, scanner);

                    printApplicationChoices(); 
                } else if (choice.equals("3")) {
                    System.out.println("You selected option " + choice);

                    break outer;
                } else {
                    System.out.print("Invalid choice. Please enter 1, 2 or 3: ");
                }
            }
        }
        scanner.close();
        System.exit(0);
    }

    public static void checkFileCreated() {
        if (!Files.exists(containerListPath)) {
            try {
                Files.writeString(containerListPath, "{}");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!Files.exists(serverInfoPath)) {
            try {
                System.out.println("Creating company info JSON file.");

                JSONObject template = new JSONObject();
                template.put("region", "");
                template.put("company", "");
                template.put("server", "");

                Files.writeString(serverInfoPath, template.toString(4));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void printApplicationChoices() {
        System.out.println("\nSelect application mode:");
        System.out.println("1. Setup Applications");
        System.out.println("2. Interval Applications");
        System.out.println("3. Exit");
        System.out.print("Enter choice (1, 2 or 3): ");
    }
}
