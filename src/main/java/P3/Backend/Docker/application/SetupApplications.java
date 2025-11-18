package P3.Backend.Docker.application;

import static P3.Backend.Docker.Persistent.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.DockerClient;

@Component
public class SetupApplications {

    // The default interval (in seconds) for the new containers.
    private static final Integer defaultIntervalTime = DEFAULT_INTERVAL_TIME;

    // Name for JSON file containing all of the containers:
    private static final String containerFilename = CURRENT_CONTAINER_PATH;

    // The path for where the JSON file is stored.
    private static final Path containerListPath = Path.of("" + containerFilename); // Replace "" with desired path.

    /** 
     * This function is used for setting up the containers and its intervals. 
     * 
     * You are able to see if there are inactive container in the JSON file as well as configuring both newly 
     * dicovered and already existing containers.
     * 
     * @param dockerClient Used for sending and receiving data from the docker.
     */
    public static void Initiation(DockerClient dockerClient) {
        // Gets all of the containers that is in the system and puts it into an array (containers).
        List<Container> containersTemp = dockerClient.listContainersCmd().withShowAll(true).exec();
        JSONArray fetchedContainersArr = new JSONArray(containersTemp);

        // Scans all of the input from the user.
        Scanner scanner = new Scanner(System.in); 
        
        // Check if the JSON file that contains all of the containers exists, 
        // if it does not exist then create one.
        checkFileCreated();

        // In order to use the library Files, you need to have a try and catch.
        try {
            // Get all of the content within the file.
            String content = Files.readString(containerListPath);
            
            // Convert all of the content back into a JSON format.
            JSONObject JSONFileObj = new JSONObject(content);

            // This is the id's that is already within the JSON file.
            JSONArray existingIdArr = new JSONArray(JSONFileObj.length());
    
            // Array over all of the new containers that is not within the JSON file.
            JSONArray newContainersArr = new JSONArray(fetchedContainersArr.length());

            // Filter the containers into inactive and new container, as well as asking if the inactive containers should be deleted.
            detectInactiveContainers(fetchedContainersArr, existingIdArr, JSONFileObj, newContainersArr, scanner);
            
            // Configure the containers, either all of the containers or just the newly discovered ones.
            showContainers(fetchedContainersArr, newContainersArr, existingIdArr, JSONFileObj, scanner);

            // Asks if user want to see the updated JSON file, printed out. 
            printAllContainers(JSONFileObj, scanner);

            // Close the input scanner.
            scanner.close();
        } catch (Exception e) {
            // If anything goes wrong, it is printed.
            e.printStackTrace();
        }
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

    /** This function is used to filter the containers into inactive and new container, as well as 
     * asking if the inactive containers should be deleted.
     * 
     * @param fetchedContainersArr Is used to compare and filter all of the containers on the system.
     * @param existingIdArr Is used to store all of the existing container id's.
     * @param JSONFileObj Is used to get all of the existing container id's as well as remove the inactive containers.
     * @param newContainersArr Is used to store all of the newly discovered containers on the system.
     * @param scanner Is used is used to scan all of the input from the user.
     */
    private static void detectInactiveContainers (JSONArray fetchedContainersArr, JSONArray existingIdArr, 
                                                        JSONObject JSONFileObj, JSONArray newContainersArr, Scanner scanner) {
        // Get all of the id's from each "key" in the JSON file and add them to the existingIdArr.
        for (String key : JSONFileObj.keySet()) {
            JSONObject tempContainer = JSONFileObj.getJSONObject(key);
            existingIdArr.put(tempContainer.getString("id"));
        }

        // Adds all of the containers from the JSON file and later removes the active containers from the list, 
        // leaving it with inactive containers.
        ArrayList<String> oldContainerList = new ArrayList<String>(existingIdArr.length());

        for (int i = 0; i < existingIdArr.length(); i++) {
            oldContainerList.add(existingIdArr.getString(i)); // Maybe this should be the whole thing - we can then get both name and id
        }
        
        // Filter all of the containers into either newly dicovered or inactive containers.
        filterContainerArrays(fetchedContainersArr, existingIdArr, oldContainerList, newContainersArr);
        
        // Convert the oldContainerList into an array.
        JSONArray oldContainersArr = new JSONArray(oldContainerList.size());
        for (int i = 0; i < oldContainerList.size(); i++) {
            if (!oldContainerList.get(i).isEmpty())
                oldContainersArr.put(oldContainerList.get(i));
        }
        System.out.println("Old containers: " + oldContainersArr.length());

        // Asks if the user wants to delete inactive containers.
        manageInactiveContainers(existingIdArr, oldContainersArr, JSONFileObj, scanner);

    }

    /** This function is used to filter all of the containers into either newly dicovered or inactive containers.
     * 
     * @param fetchedContainersArr Is used to get every single container and comparing them with the 
     * existingIdArr, as well as filtering the newly discovered containers into an array and remove the active 
     * containers from the oldContainersArr, leaving just the inactive behind. 
     * @param existingIdArr Is used to identify new or old containers, by comparing the existing id's 
     * in the JSON file with the fetchedContainersArr's id's.
     * @param oldContainerList Is used to store all of the inactive containers that are no longer on the system.
     * @param newContainersArr Is used to store all of the newly discovered containers that is on the system.
     */
    private static void filterContainerArrays(JSONArray fetchedContainersArr, JSONArray existingIdArr, 
                                                        ArrayList<String> oldContainerList, JSONArray newContainersArr) {
        
        // Go through all of the discovered containers.
        for (int i = 0; i < fetchedContainersArr.length(); i++) {
            JSONObject fetchedContainer = fetchedContainersArr.getJSONObject(i); // Get the i'th container from the array.
            String fetchedId = fetchedContainer.getString("id"); // Get the id from the container
            
            int count = 0;
            // Make sure that there are any existing id's and goes through each of them. 
            if (existingIdArr.length() != 0) {
                for (int j = 0; j < oldContainerList.size(); j++) {
                    
                    // Removes fetchedId from the oldContainersList, proving that it already exists and is still actively used.
                    if (oldContainerList.get(j).equals(fetchedId)) {
                        oldContainerList.remove(j);
                        break;
                    }
                    count++;
                    // Adds the new containers to the array of containers that is not within the JSON file.
                    if (count == existingIdArr.length()) {
                        newContainersArr.put(fetchedContainer);
                    }
                }
            } else { // If the JSON file is empty, then the if statement in the for loop will not execute.
                // Adds the container to the array of containers that is not within the JSON file.
                if (count == existingIdArr.length()) {
                    newContainersArr.put(fetchedContainer);
                }
            }
        }
    }

    /** This function is used to ask if the user wants to delete inactive containers. This is done either by 
     * deleting all of the containers or deleting them individually.
     * 
     * @param existingIdArr Is used to delete the each of the selected container from the JSON file.
     * @param oldContainersArr Is used for keeping an overview of each of the inactive containers, 
     * that can be deleted.
     * @param JSONFileObj Is used for deleting the selected inactive containers from the JSON file.
     * @param scanner Is used is used to scan all of the input from the user.
     */
    private static void manageInactiveContainers(JSONArray existingIdArr, JSONArray oldContainersArr,
                                                        JSONObject JSONFileObj, Scanner scanner) {
        // If there are any inactive containers to be deleted then proceed and list all of them.
        if (oldContainersArr.length() > 0) {
            System.out.println("There are some unused containers within the JSON file: " + oldContainersArr);
            
            // Making a while loop to ensure the right input.
            outer: while (true) {
                System.out.println("Do you want to delete any of these containers? (y/n)");
                String response = scanner.nextLine();
                if (response.toLowerCase().equals("y")) { // If user wants to delete any inactive containers, then proceed.
                    while (true) {
                        System.out.println("Do you want to delete all or individually?");
                        System.out.println("\n 1) Delete all.\n 2) Delete individually.\n -1) Exit container deletion");
                        response = scanner.nextLine();

                        // If user wants to delete all of the inactive containers, then proceed.
                        if (response.toLowerCase().equals("1")) { 
                            
                            // Delete every container that is inactive.
                            deleteSequentially(existingIdArr, oldContainersArr, JSONFileObj);
                            break outer;

                            // If user wants to individually delete some of the inactive containers, then proceeed.
                        } else if (response.toLowerCase().equals("2")) { 
                            
                            // Select which of the inactive containers the user want to delete.
                            deleteIndividually(existingIdArr, oldContainersArr, JSONFileObj, scanner);
                            break outer;
                            // If user does not want to delete any of the containers, then proceed.
                        } else if (response.toLowerCase().equals("-1")) {

                            // Make sure that all of the inactive is set to be inactive within the JSON file.
                            setInactiveContainers(existingIdArr, oldContainersArr, JSONFileObj);
                            return;
                        }
                    } // If the user does not want to delete any inactive containers, then proceed.
                } else if (response.toLowerCase().equals("n")) {

                    // Make sure that all of the inactive is set to be inactive within the JSON file.
                    setInactiveContainers(existingIdArr, oldContainersArr, JSONFileObj);
                    break; 
                }
            }
        }
    }

    /** This function is used to set all of the state of all of the inactive containers.
     * 
     * @param existingIdArr Is used to find the inactive containers within the JSON file.
     * @param oldContainersArr Is used to store all of the inactive containers.
     * @param JSONFileObj Is used to edit the state of each of the containers.
     */
    private static void setInactiveContainers(JSONArray existingIdArr, JSONArray oldContainersArr,
                                                        JSONObject JSONFileObj) {
        // Go through each of the containers in olContainersArr and compare them with each container in existingIdArr.
        for (int i = 0; i < oldContainersArr.length(); i++) {
            if (!oldContainersArr.getString(i).isEmpty()) {
                for (int j = 0; j < existingIdArr.length(); j++) {
                    if (existingIdArr.getString(j).equals(oldContainersArr.getString(i))) {
                        // Set the state of the container to be inactive.
                        String name = JSONFileObj.names().getString(j);
                        JSONFileObj.getJSONObject(name).put("state", "inactive");

                        updateJSONFile(JSONFileObj); // Inactive containers get the inactive state.
                        break;
                    }
                }
            }
        }
    }

    /** This function is used to delete every inactive containers.
     * 
     * @param existingIdArr Is used to make sure that the inactive container exists in the JSON file.
     * @param oldContainersArr Is used to get each of the inactive containers that can be deleted.
     * @param JSONFileObj Is used for deleting all of the inactive containers from the JSON file.
     */
    private static void deleteSequentially(JSONArray existingIdArr, JSONArray oldContainersArr, JSONObject JSONFileObj) {
        int deletedContainers = 0;
        // Go through each of the inactive containers and see if they exist in the existingIdArr.
        for (int i = 0; i < oldContainersArr.length(); i++) {
            for (int j = 0; j < existingIdArr.length(); j++) {
                
                // Compare if the current container is equal to the current existingId.
                if (existingIdArr.getString(j).equals(oldContainersArr.getString(i))) {
                    
                    // Get the name of the container that has to be deleted.
                    String temp = JSONFileObj.names().getString(j - deletedContainers);
                    JSONFileObj.remove(temp); 
                    deletedContainers++; // This is used to match the correct index. 
                    break;
                }
            }
        }
        try {
            // Updates the JSON file.
            Files.writeString(containerListPath, JSONFileObj.toString(4));
        } catch (Exception e) {
            // If anything goes wrong, it will be printed.
            e.printStackTrace();
        }
    }

    /** This function is used to individually delete each of the selected inactive containers.
     * 
     * @param existingIdArr Is used to make sure that the inactive container exists in the JSON file.
     * @param oldContainersArr Is used to get each of the inactive containers that can be deleted.
     * @param JSONFileObj Is used for deleting all of the inactive containers from the JSON file.
     * @param scanner Is used is used to scan all of the input from the user.
     */
    private static void deleteIndividually(JSONArray existingIdArr, JSONArray oldContainersArr, 
                                                JSONObject JSONFileObj, Scanner scanner) {
        int deletedContainers = 0;
        // Go through each of the inactive containers and ask the user if it should be deleted.
        for (int i = 0; i < oldContainersArr.length(); i++) {
            
            // Making a while loop to ensure the right input.
            inner: while (true) {
                // Prints each container individually and asks user whether to delete.
                System.out.println((i + 1) + ") Do you want to delete this container (id): " + oldContainersArr.getString(i) + "? (y/n)");
                String response = scanner.nextLine();
                if (response.equals("y")) { // If the user wants to delete this containers, then proceed.
                    for (int j = 0; j < existingIdArr.length(); j++) {

                        // Deletes all of the inactive containers from the JSON file.
                        if (existingIdArr.getString(j).equals(oldContainersArr.getString(i))) {
                            
                            // Get the name of the container that has to be deleted.
                            String name = JSONFileObj.names().getString(j - deletedContainers);
                            JSONFileObj.remove(name);
                            deletedContainers++; // This is used to match the correct index.
                            break inner;
                        }
                    } // If the user does not want this delete this container, then proceed to the next container.
                } else if (response.equals("n")) {

                    // Make sure that all of the inactive is set to be inactive within the JSON file.
                    setInactiveContainers(existingIdArr, oldContainersArr, JSONFileObj);
                    break;
                } else {
                    continue;
                }
            }
        }
        try {
            // Updates the JSON file.
            Files.writeString(containerListPath, JSONFileObj.toString(4));
        } catch (Exception e) {
            // If anything goes wrong, it will be printed.
            e.printStackTrace();
        }
    }

    /** This function is used to figure out what containers the user want to configure 
     * (either all of the containers or just the newly discovered ones).
     * 
     * @param fetchedContainersArr Is used to go through every single container and configure them.
     * @param newContainersArr Is used to go through only the newly discovered containers and configure them.
     * @param existingIdArr Is used for figuring out if the fetched containers already exists within the JSON file 
     * and if so get the name of said container.
     * @param JSONFileObj Is used for updating the JSON array and finding the intervals for the existing containers.
     * @param scanner Is used is used to scan all of the input from the user.
     */
    private static void showContainers(JSONArray fetchedContainersArr, JSONArray newContainersArr, JSONArray existingIdArr,  
                                            JSONObject JSONFileObj, Scanner scanner) {
        // If both newContainerArr and existingIdArr has any elements, then proceed to choose which to configure.
        if (newContainersArr.length() > 0 && existingIdArr.length() > 0) {
            System.out.println("Do you want to configure only the newly discovered containers or every container?");
            System.out.println("1) Only newly discovered containers. \n 2) Every single container. \n -1) For exiting the setup management.");
            
            while (true) {
                String response = scanner.nextLine();
                if (response.equals("1")) { // Only configure newly discovered containers.

                    // Go through only the new containers and set up the intervals.
                    AppendJSONContainerList(newContainersArr, existingIdArr, JSONFileObj, scanner);
                    break;
                } else if (response.equals("2")) { // Configure every single container.
                    // Go through all of the containers and set up the intervals.

                    AppendJSONContainerList(fetchedContainersArr, existingIdArr, JSONFileObj, scanner);
                    break; 
                } else if (response.equals("-1")) { // Do not configure any containers.
                    return;
                }
            }
        } else if (newContainersArr.length() == 0) { // If there is not any new containers detected.
            
            System.out.println("Do you want to configure every container? (y/n)");
            while (true) {
                String response = scanner.nextLine();
                if (response.equals("y")) { // Configure every containers.

                    // Go through only the new containers and set up the intervals.
                    AppendJSONContainerList(fetchedContainersArr, existingIdArr, JSONFileObj, scanner);
                    break;

                } else if (response.equals("n")) { // Do not configure any containers.
                    return; 
                }
            }
        } else if (existingIdArr.length() == 0) { // If there is not containers in the JSON file.
            
            System.out.println("Do you want to configure the newly discovered containers? (y/n)");
            while (true) {
                String response = scanner.nextLine();
                if (response.equals("y")) { // Configure every containers.

                    // Go through only the new containers and set up the intervals.
                    AppendJSONContainerList(newContainersArr, existingIdArr, JSONFileObj, scanner);
                    break;
                    
                } else if (response.equals("n")) { // Do not configure any containers.
                    return; 
                }
            }
        }
    }

    
    /** This function is used for configuring the interval of containers.
     * 
     * @param containersArr Is used to add every single one of the containers from the array to the JSON file.
     * @param existingIdArr Is used for getting the intervals from the already existing containers in the JSON file.
     * @param JSONFileObj Is used for updating the JSON array and finding the intervals for the existing containers.
     * @param scanner Is used is used to scan all of the input from the user.
     */
    private static void AppendJSONContainerList(JSONArray containersArr, JSONArray existingIdArr, 
                                                        JSONObject JSONFileObj, Scanner scanner) {
        // List all of the containers that is being configured.
        System.out.println("This is the setup for all of the containers.");
        System.out.println("Containers to set up: " + containersArr.length() + "\n");




        for (int i = 0; i < containersArr.length(); i++) {
            // Get the individual container from the list of containers (and extract the name and id).
            JSONObject container = containersArr.getJSONObject(i);
            String name = container.getJSONArray("names").getString(0).substring(1);
            String id = container.getString("id");
            
            // If there are any existing containers in the JSON file, then proceed. 
            Integer interval;
            if (existingIdArr.length() > 0) {
                // Find the interval of the existing containers, if the container is newly discovered, 
                // then the interval is set to the default interval
                interval = findContainerInterval(container, existingIdArr, JSONFileObj);
            } else {
                interval = defaultIntervalTime;
            }
            
            // Gets the new interval for sending data from the user.
            System.out.println("\n" + (i + 1) + ": Name: " + name);
            System.out.println("Id: " + id + "\n");
            System.out.println("The interval this container is going to send data (in seconds).");
            System.out.println("Latest interval for \"" + name + "\" is: " + interval + " seconds.");
            
            // Make sure that the user's interval input is an int and over 0.
            while (true) {
                System.out.print("Input new interval (over 0 sec): ");
                String response = scanner.nextLine();    
                try {
                    int value = Integer.parseInt(response);
                    if (value <= 0) {
                        continue;
                    }
                    interval = value;
                    break;
                } catch (Exception e) {
                    // If the input isn't an integer it will should try again
                    if (e instanceof NumberFormatException) {
                        continue;
                    } else {
                        e.printStackTrace();
                    }
                }

            }

            // Make a JSON object that contains all of the relevant information.
            JSONObject newContainer = new JSONObject();
            newContainer.put("name", name);
            newContainer.put("id", id);
            newContainer.put("interval", interval);
            newContainer.put("state", "active");

            // If there exists a directory "ports", then proceed.
            if (!container.getJSONArray("ports").isEmpty()) {
                // Get both public and private port.
                // public port is the port that the system is using to communicate with the docker container.
                // private port is the port that the docker container is using.
                JSONArray ports = container.getJSONArray("ports");
                Integer privatePort = ports.getJSONObject(0).getInt("privatePort");
                Integer publicPort = ports.getJSONObject(0).getInt("publicPort");
    
                // Put the ports in the newContainer.
                newContainer.put("privatePort", privatePort);
                newContainer.put("publicPort", publicPort);
            }
            
            // Add the new container to the existing content.
            JSONFileObj.put(name, newContainer);

            // Update the JSON file.
            updateJSONFile(JSONFileObj);
        }
    }

    /** This function is used to find the intervals of the existing containers that are within the JSON file.
     * 
     * @param currentContainer Is used for checking if the container already has an interval.
     * @param existingIdArr Is used for getting the intervals from the already existing containers in the JSON file.
     * @param JSONFileObj Is used to get the interval that is inside of the JSON file.
     * @return The return is either the interval from an already existing container or the default interval (used for newly 
     * discovered containers that is not within the JSON file).
     */
    private static int findContainerInterval(JSONObject currentContainer, JSONArray existingIdArr, JSONObject JSONFileObj) {
        int count = 0; 
        String currentId = currentContainer.getString("id");
        // This goes through the containerArr and gets the ID from each of the containers.
        for (int i = 0; i < existingIdArr.length(); i++) {
            
            // If currentId exists in the existing array it returns the correct interval.
            if (existingIdArr.getString(i).equals(currentId)) {
                
                String currentName = currentContainer.
                    getJSONArray("names").
                    getString(0).
                    substring(1);
                
                // If the id already exists within the JSON file, then the interval also exists.
                return JSONFileObj.getJSONObject(currentName).getInt("interval");
            } else {
                count++;
                // If the id does not exist within the JSON file, then it will return the default interval.
                if (count == existingIdArr.length()) {
                    return defaultIntervalTime; // Returns the default seconds if it is a new container.
                }
            }
        }
        return defaultIntervalTime; // To please the complier, we return something where.
    }

    /** This function is used for setting the JSON file with new input.
     * 
     * @param JSONFileObj Is used to set the parameters that is inside of the JSON file.
     */
    public static void updateJSONFile(JSONObject JSONFileObj) {
        // If the path exists add the content to the JSON file. 
        if (Files.exists(containerListPath)) {
            try {
                
                // Updates the JSON file.
                Files.writeString(containerListPath, JSONFileObj.toString(4));
                
            } catch (IOException e) {
                // If anything goes wrong, it will be printed.
                e.printStackTrace();
            }
        }
    }

    /** This function is used to print out all containers with each of the container's parameters.
     * 
     * @param JSONFileObj Is used to get the container's parameters that is inside of the JSON file.
     * @param scanner Is used to scan all of the input from the user.
     */
    private static void printAllContainers(JSONObject JSONFileObj, Scanner scanner) {
        System.out.println("Do you want to print all containers from the JSON file? (y/n)");
        while (true) {
            String response = scanner.nextLine();

            if (response.toLowerCase().equals("y")) { // If user wants to print out all the containers, then proceed.
                int count = 1;
                // Get all of the parameters from each "key" in the JSON file.
                for (String key : JSONFileObj.keySet()) {
                    JSONObject tempContainer = JSONFileObj.getJSONObject(key);
                    String name = tempContainer.getString("name");
                    String id = tempContainer.getString("id");
                    Integer interval = tempContainer.getInt("interval");
                    String state = tempContainer.getString("state");

                    // Prints out the parameters from each container.
                    System.out.println("\n" + count + ") Name: " + name);
                    System.out.println(" Id: " + id);
                    System.out.println(" Interval: " + interval);
                    System.out.println(" State: " + state + "\n");
                    count++;
                }
                // If the user doesn't want the container's parameters printed out, then proceed.
            } else if (response.toLowerCase().equals("n")) {
                return;
            }
        }
    }
}
