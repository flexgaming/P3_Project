package P3.Backend.Docker;

import org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi.ecCVCDSA224;
import org.json.JSONArray;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import P3.Backend.Docker.builder.DockerClientBuilder;
import P3.Backend.Docker.manager.DockerClientManager;
import P3.Backend.Docker.manager.DockerStatsService;
import P3.Backend.Docker.manager.DockerStatsService.ContainerStats;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;

import java.util.List;
import java.util.Scanner;

import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

import P3.Backend.Docker.Setup.SetupApplications;



@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		DockerClientBuilder builder = new DockerClientBuilder();
		DockerClient dockerClient = builder.dockerClient();

		dockerClient.pingCmd().exec();

		DockerStatsService dockerStatsService = new DockerStatsService(dockerClient);
		String containerId = "f7e5092e9534ab5e33a2d40835548577aec6b24c3a7a10a00e348511e39f119f";
		try {
			ContainerStats stats = dockerStatsService.getContainerStats(containerId);

			// Print stats only when successfully retrieved (stats is in-scope here)
			System.out.println("CPU Total Usage: " + stats.getCpuTotalUsage());
			System.out.println("Memory Usage: " + stats.getMemoryUsage());
			System.out.println("Memory Limit: " + stats.getMemoryLimit());
			System.out.println("PIDs: " + stats.getPids());
		} catch (InterruptedException e) {
			System.err.println("Failed to get container stats: " + e.getMessage());
			Thread.currentThread().interrupt();
		}

        SetupApplications.Initiation(dockerClient);









        // Gets all of the containers that is in the system.
        List<Container> containersTemp = dockerClient.listContainersCmd().withShowAll(true).exec();
        JSONArray containers = new JSONArray(containersTemp);
        //System.out.println(containers.toString(2)); // Prints a manageable overview.
        //System.out.println(containers.getJSONObject(0).getJSONArray("names").getString(0).substring(1));

        /*if (containers.getJSONObject(0).getJSONArray("names").length() > 0) {
            for (int i = 0; i < containers.getJSONObject(0).getJSONArray("names").length(); i++) {
                String name = containers.getJSONObject(0).getJSONArray("names").getString(i).substring(1);

                System.out.println(name);
            }
        } */



        // Name for JSON file containing all of the containers:
        final String containerFilename = "currentContainers.json";
        // Check if the JSON file that contains all of the containers exists, if it does not exist then create one.
        Path containerListPath = Path.of("" + containerFilename); // path

        if (!Files.exists(containerListPath)) {
            // If the file does not exist, then it has to be created.
            try {
                Files.writeString(containerListPath, "{}");
            } catch (Exception e) {
                // If anything goes wrong, it is printed.
                e.printStackTrace();
            }
        } 



        // If there are any old containers that is not within the currently pulled list of containers, ask user if it can be deleted.

        try {
             // Get all of the content within the file.
            String content = Files.readString(containerListPath);
         
        
            // Convert all of the content into a JSON format.
            JSONObject currentJSONFile = new JSONObject(content);
            
            // This is the id's that is already within the JSON file.
            JSONArray existingID = new JSONArray(currentJSONFile.length());
            for (String key : currentJSONFile.keySet()) {
                JSONObject tempContainer = currentJSONFile.getJSONObject(key);
                existingID.put(tempContainer.getString("id"));
            }

            // Array over all of the old containers that is not within the fetch.
            JSONArray oldContainersList = new JSONArray(existingID.length());
            for (int i = 0; i < existingID.length(); i++) {

                // Adds all of the containers from the JSON file, to figure out if it is active or not.
                oldContainersList.put(i, existingID.getString(i));
            }

            // Array over all of the new containers that is not within the JSON file.
            JSONArray newContainersList = new JSONArray(containers.length());
            
            // ############################################################################# Add comment here...
            int matchedContainers = 0;
            for (int i = 0; i < containers.length(); i++) {
                // This is the name of the container that was just pulled.
                String fetchedID = containers.getJSONObject(i).getString("id");
                
                int count = 0;
                for (int j = 0; j < existingID.length(); j++) {
                    
                    // Removes fetchedID from the oldContainersList, proving that it already exists but is still actively used.
                    if (existingID.getString(j).equals(fetchedID)) {
                        
                        oldContainersList.remove(j - matchedContainers);
                        matchedContainers++; // This is used to match the correct index. 
                        break;
                    }
                    count++;

                    // Adds to the list of containers that is not within the JSON file.
                    if (count == existingID.length()) {
                        newContainersList.put(fetchedID);
                    }
                }
            }
            System.out.println("new containers: " + newContainersList + "\n");
            System.out.println("old containers (not in use): " + oldContainersList + "\n");
            
            // Scans all of the input from the user
            Scanner scanner = new Scanner(System.in); // Closed in the buttom

            // do the user want to remove the containers that are not in the fetch?
            if (oldContainersList.length() > 0) {
                System.out.println("There are some unused containers within the JSON file: " + oldContainersList);
                
                outer: while (true) {
                    System.out.println("Do you want to delete any of these containers? (y/n)");
                    String response = scanner.nextLine();
                    if (response.toLowerCase().equals("y")) {
                        System.out.println("hello?");
                        while (true) {
                            System.out.println("Do you want to delete all or individually?\n 1) delete all\n 2) delete individually");
                            response = scanner.nextLine();
                            if (response.toLowerCase().equals("1")) {
                                // Delete all function:
                                // functionName(oldContainersList, ...)

                                // ############################################################################# Add comment here...
                                int removedContainers = 0;
                                for (int i = 0; i < oldContainersList.length(); i++) {
                                    for (int j = 0; j < existingID.length(); j++) {
                                        
                                        // Removes all of the inactive containers from the JSON file.
                                        if (existingID.getString(j).equals(oldContainersList.getString(i))) {
                                            
                                            // Get the name of the container that has to be removed.
                                            String temp = currentJSONFile.names().getString(j - removedContainers);
                                            currentJSONFile.remove(temp); // Will be updated later
                                            removedContainers++; // This is used to match the correct index. 
                                            break;
                                        }
                                    }
                                }
                                break outer;
                            } else if (response.toLowerCase().equals("2")) {
                                // Delete individually function:
                                // functionName(oldContainersList.getString(index))
                                
                                // ############################################################################# Add comment here...
                                int removedContainers = 0;
                                for (int i = 0; i < oldContainersList.length(); i++) {

                                    System.out.println((i+1) + ") Do you want to remove this container (id): " + oldContainersList.getString(i) + "? (y/n)");
                                    
                                    inner: while (true) {
                                        response = scanner.nextLine();
                                        if (response.equals("y")) {
                                            for (int j = 0; j < existingID.length(); j++) {
                                                System.out.println(j + ": " + existingID.getString(j) + " = " + oldContainersList.getString(i));
                                                // Removes all of the inayctive containers from the JSON file.
                                                if (existingID.getString(j).equals(oldContainersList.getString(i))) {
                                                    
                                                    // Get the name of the container that has to be removed.
                                                    String temp = currentJSONFile.names().getString(j - removedContainers);
                                                    currentJSONFile.remove(temp); // Will be updated later
                                                    removedContainers++; // This is used to match the correct index. 
                                                    
                                                    break inner;
                                                }
                                            }
                                        } else if (response.equals("n")) {
                                            break;
                                        }
                                    }
                                }
                                break outer;
                            }
                        }
                    } else if (response.toLowerCase().equals("n")) {
                        break;
                    }
                }
                
            }

            // does the user want to go through all of the containers or just the newly discorvered?
                // if the user want to go through all of the containers, they have the ability to look a the old intervals...
                // If there are any new containers then add them to a list and then go through the process ↓.

            


            // Set up all of the containers using a loop.
            System.out.println("This is the setup for all of the containers.");
            System.out.println("Containers to set up: " + containers.length());

            
            for (int i = 0; i < containers.length(); i++) {
                // Get the individual container from the list of containers (and extract the name and id).
                JSONObject container = containers.getJSONObject(i);
                String name = container.getJSONArray("names").getString(0).substring(1);
                String id = container.getString("id");

                // Gets the interval for sending data from the user
                System.out.println((i + 1) + ": Name: " + name);
                System.out.println("Id: " + id);
                System.out.print("The interval this container is going to send data (in seconds): ");
                Integer interval = scanner.nextInt();
                
                // Make a JSON object that contains all of the relevant information.
                JSONObject newContainer = new JSONObject();
                newContainer.put("name", name);
                newContainer.put("id", id);
                newContainer.put("interval", interval);

                // Add the content to the JSON file. 
                if (Files.exists(containerListPath)) {
                    try {
                        // Add the new container to the existing content.
                        currentJSONFile.put(name, newContainer);

                        // Updates the JSON file.
                        Files.writeString(containerListPath, currentJSONFile.toString(4));
                        System.out.println(currentJSONFile);
                    } catch (IOException e) {
                        // If anything goes wrong, it will be printed.
                        e.printStackTrace();
                    }
                }
            }

            scanner.close(); // Closing the scanner.
            
            // Print all of the docker containers with the different parameters (makes sure that the information is correct).
                // Name, id and intervals.
                // Print all of the containers that is 
            }
        catch (IOException e) {
            // If anything goes wrong, it will be printed (containerListPath).
            e.printStackTrace();
        }

       

/*
        ObjectMapper mapper = new ObjectMapper();
        String JSON;
        try {
            JSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(containers);
            System.out.println(JSON.);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } */
        

		/* while (true) {
			try {
				Thread.sleep(5000);

				long start = System.nanoTime();
				dockerClient.pingCmd().exec(); // Sends ping to Docker daemon
				long end = System.nanoTime();
				double latencyMs = (end - start) / 1_000_000.0; // convert ns → ms
				System.out.printf("Docker daemon latency: %.2f ms%n", latencyMs);
			
			} catch (InterruptedException e) {
				System.err.println("Sleep interrupted: " + e.getMessage());
			} catch (Exception e) {
				System.err.println("Failed to ping Docker daemon: " + e.getMessage());
			}
		}
		try {
			String id = "f7e5092e9534ab5e33a2d40835548577aec6b24c3a7a10a00e348511e39f119f";

			var response = dockerClient.inspectContainerCmd(id).exec();
			
			dockerClient.statsCmd(id);

			var state = response.getState();
			Boolean running = (state != null) ? state.getRunning() : null;
			String status = (state != null) ? state.getStatus() : null;

			System.out.printf("Container summary: name=%s id=%s running=%s status=%s image=%s%n",
				response.getName(),
				response.getId(),
				String.valueOf(running),
				String.valueOf(status),
				(response.getConfig() != null ? response.getConfig().getImage() : null)
			);

			// Print a few additional safe fields to avoid serialization issues with docker-java types
			String ipAddress = null;
			if (response.getNetworkSettings() != null && response.getNetworkSettings().getNetworks() != null) {
				var networks = response.getNetworkSettings().getNetworks();
				if (!networks.isEmpty()) {
					ipAddress = networks.values().iterator().next().getIpAddress();
				}
			}
			System.out.printf("Network: ip=%s%n", String.valueOf(ipAddress));
		} catch (NotFoundException nf) {
			System.err.println("Container not found. Listing known containers (including stopped):");
			dockerClient.listContainersCmd().withShowAll(true).exec().forEach(c -> {
				String names = (c.getNames() != null && c.getNames().length > 0) ? String.join(", ", c.getNames()) : "<no-name>";
				System.err.printf("- %s %s (image=%s) state=%s%n", c.getId(), names, c.getImage(), c.getState());
			});
		} catch (Exception e) {
			System.err.println("Failed to inspect container: " + e.getMessage());
		}*/
		
	}

}
