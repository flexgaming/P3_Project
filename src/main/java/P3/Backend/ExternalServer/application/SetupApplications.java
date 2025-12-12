package P3.Backend.ExternalServer.application;

import static P3.Backend.ExternalServer.Docker.Persistent.CURRENT_CONTAINER_PATH;
import static P3.Backend.ExternalServer.Docker.Persistent.CONTAINER_NAME;
import static P3.Backend.ExternalServer.Docker.Persistent.DEFAULT_INTERVAL_TIME;

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

    private static final Path containerListPath = Path.of(CURRENT_CONTAINER_PATH + CONTAINER_NAME);

    public static void initiation(DockerClient dockerClient, Scanner scanner) {
        List<Container> containersTemp = dockerClient.listContainersCmd().withShowAll(true).exec();
        JSONArray fetchedContainersArr = new JSONArray(containersTemp);

        try {
            String content = Files.readString(containerListPath);

            JSONObject JSONFileObj = new JSONObject(content);

            JSONArray existingIdArr = new JSONArray(JSONFileObj.length());

            JSONArray newContainersArr = new JSONArray(fetchedContainersArr.length());

            detectInactiveContainers(fetchedContainersArr, existingIdArr, JSONFileObj, newContainersArr, scanner);

            showContainers(fetchedContainersArr, newContainersArr, existingIdArr, JSONFileObj, scanner);

            printAllContainers(JSONFileObj, scanner);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void detectInactiveContainers (JSONArray fetchedContainersArr, JSONArray existingIdArr,
                                                  JSONObject JSONFileObj, JSONArray newContainersArr, Scanner scanner) {
        for (String key : JSONFileObj.keySet()) {
            JSONObject tempContainer = JSONFileObj.getJSONObject(key);
            existingIdArr.put(tempContainer.getString("id"));
        }

        ArrayList<String> oldContainerList = new ArrayList<String>(existingIdArr.length());

        for (int i = 0; i < existingIdArr.length(); i++) {
            oldContainerList.add(existingIdArr.getString(i)); 
        }

        filterContainerArrays(fetchedContainersArr, existingIdArr, oldContainerList, newContainersArr);

        JSONArray oldContainersArr = new JSONArray(oldContainerList.size());
        for (int i = 0; i < oldContainerList.size(); i++) {
            if (!oldContainerList.get(i).isEmpty())
                oldContainersArr.put(oldContainerList.get(i));
        }
        System.out.println("Old containers: " + oldContainersArr.length());

        manageInactiveContainers(existingIdArr, oldContainersArr, JSONFileObj, scanner);
    }

    private static void filterContainerArrays(JSONArray fetchedContainersArr, JSONArray existingIdArr,
                                              ArrayList<String> oldContainerList, JSONArray newContainersArr) {
        for (int i = 0; i < fetchedContainersArr.length(); i++) {
            JSONObject fetchedContainer = fetchedContainersArr.getJSONObject(i); 
            String fetchedId = fetchedContainer.getString("id"); 

            int count = 0;
            if (existingIdArr.length() != 0) {
                for (int j = 0; j < oldContainerList.size(); j++) {

                    if (oldContainerList.get(j).equals(fetchedId)) {
                        oldContainerList.remove(j);
                        break;
                    }
                    count++;
                    if (count == existingIdArr.length()) {
                        newContainersArr.put(fetchedContainer);
                    }
                }
            } else { 
                if (count == existingIdArr.length()) {
                    newContainersArr.put(fetchedContainer);
                }
            }
        }
    }

    private static void manageInactiveContainers(JSONArray existingIdArr, JSONArray oldContainersArr,
                                                 JSONObject JSONFileObj, Scanner scanner) {
        if (oldContainersArr.length() > 0) {
            System.out.println("There are some unused containers within the JSON file: " + oldContainersArr);

            outer: while (true) {
                System.out.println("Do you want to delete any of these containers? (y/n)");
                String response = scanner.nextLine();
                if (response.toLowerCase().equals("y")) { 
                    while (true) {
                        System.out.println("Do you want to delete all or individually?");
                        System.out.println("\n 1) Delete all.\n 2) Delete individually.\n -1) Exit container deletion");
                        response = scanner.nextLine();

                        if (response.toLowerCase().equals("1")) {

                            deleteSequentially(existingIdArr, oldContainersArr, JSONFileObj);
                            break outer;

                        } else if (response.toLowerCase().equals("2")) {

                            deleteIndividually(existingIdArr, oldContainersArr, JSONFileObj, scanner);
                            break outer;
                        } else if (response.toLowerCase().equals("-1")) {

                            setInactiveContainers(existingIdArr, oldContainersArr, JSONFileObj);
                            return;
                        }
                    } 
                } else if (response.toLowerCase().equals("n")) {

                    setInactiveContainers(existingIdArr, oldContainersArr, JSONFileObj);
                    break;
                }
            }
        }
    }

    private static void deleteSequentially(JSONArray existingIdArr, JSONArray oldContainersArr, JSONObject JSONFileObj) {
        int deletedContainers = 0;
        for (int i = 0; i < oldContainersArr.length(); i++) {
            for (int j = 0; j < existingIdArr.length(); j++) {

                if (existingIdArr.getString(j).equals(oldContainersArr.getString(i))) {

                    String temp = JSONFileObj.names().getString(j - deletedContainers);
                    JSONFileObj.remove(temp);
                    deletedContainers++; 
                    break;
                }
            }
        }
        try {
            Files.writeString(containerListPath, JSONFileObj.toString(4));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteIndividually(JSONArray existingIdArr, JSONArray oldContainersArr,
                                           JSONObject JSONFileObj, Scanner scanner) {
        int deletedContainers = 0;
        for (int i = 0; i < oldContainersArr.length(); i++) {

            inner: while (true) {
                System.out.println((i + 1) + ") Do you want to delete this container (id): " + oldContainersArr.getString(i) + "? (y/n)");
                String response = scanner.nextLine();
                if (response.equals("y")) { 
                    for (int j = 0; j < existingIdArr.length(); j++) {

                        if (existingIdArr.getString(j).equals(oldContainersArr.getString(i))) {

                            String name = JSONFileObj.names().getString(j - deletedContainers);
                            JSONFileObj.remove(name);
                            deletedContainers++; 
                            break inner;
                        }
                    } 
                } else if (response.equals("n")) {

                    setInactiveContainers(existingIdArr, oldContainersArr, JSONFileObj);
                    break;
                } else {
                    continue;
                }
            }
        }
        try {
            Files.writeString(containerListPath, JSONFileObj.toString(4));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setInactiveContainers(JSONArray existingIdArr, JSONArray oldContainersArr,
                                              JSONObject JSONFileObj) {
        for (int i = 0; i < oldContainersArr.length(); i++) {
            if (!oldContainersArr.getString(i).isEmpty()) {
                for (int j = 0; j < existingIdArr.length(); j++) {
                    if (existingIdArr.getString(j).equals(oldContainersArr.getString(i))) {
                        String name = JSONFileObj.names().getString(j);
                        JSONFileObj.getJSONObject(name).put("state", "inactive");

                        updateJSONFile(JSONFileObj); 
                        break;
                    }
                }
            }
        }
    }

    private static void showContainers(JSONArray fetchedContainersArr, JSONArray newContainersArr, JSONArray existingIdArr,
                                       JSONObject JSONFileObj, Scanner scanner) {
        if (newContainersArr.length() > 0 && existingIdArr.length() > 0) {
            System.out.println("Do you want to configure only the newly discovered containers or every container?");
            System.out.println("1) Only newly discovered containers. \n 2) Every single container. \n -1) For exiting the setup management.");

            while (true) {
                String response = scanner.nextLine();
                if (response.equals("1")) { 

                    appendJSONContainerList(newContainersArr, existingIdArr, JSONFileObj, scanner);
                    break;
                } else if (response.equals("2")) { 

                    appendJSONContainerList(fetchedContainersArr, existingIdArr, JSONFileObj, scanner);
                    break;
                } else if (response.equals("-1")) { 
                    return;
                }
            }
        } else if (newContainersArr.length() == 0) { 

            System.out.println("Do you want to configure every container? (y/n)");
            while (true) {
                String response = scanner.nextLine();
                if (response.equals("y")) { 

                    appendJSONContainerList(fetchedContainersArr, existingIdArr, JSONFileObj, scanner);
                    break;

                } else if (response.equals("n")) { 
                    return;
                }
            }
        } else if (existingIdArr.length() == 0) { 

            System.out.println("Do you want to configure the newly discovered containers? (y/n)");
            while (true) {
                String response = scanner.nextLine();
                if (response.equals("y")) { 

                    appendJSONContainerList(newContainersArr, existingIdArr, JSONFileObj, scanner);
                    break;

                } else if (response.equals("n")) { 
                    return;
                }
            }
        }
    }

    private static void appendJSONContainerList(JSONArray containersArr, JSONArray existingIdArr,
                                                JSONObject JSONFileObj, Scanner scanner) {
        System.out.println("This is the setup for all of the containers.");
        System.out.println("Containers to set up: " + containersArr.length() + "\n");

        for (int i = 0; i < containersArr.length(); i++) {
            JSONObject container = containersArr.getJSONObject(i);
            String name = container.getJSONArray("names").getString(0).substring(1);
            String id = container.getString("id");
            String image = container.getString("image");

            Integer interval;
            if (existingIdArr.length() > 0) {
                interval = findContainerInterval(container, existingIdArr, JSONFileObj);
            } else {
                interval = DEFAULT_INTERVAL_TIME;
            }

            System.out.println("\n" + (i + 1) + ": Name: " + name);
            System.out.println("Id: " + id + "\n");
            System.out.println("The interval this container is going to send data (in seconds).");
            System.out.println("Latest interval for \"" + name + "\" is: " + interval + " seconds.");

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
                    if (e instanceof NumberFormatException) {
                        continue;
                    } else {
                        e.printStackTrace();
                    }
                }
            }
            JSONObject newContainer = new JSONObject();
            newContainer.put("name", name);
            newContainer.put("id", id);
            newContainer.put("interval", interval);
            newContainer.put("state", "active");
            newContainer.put("image", image);

            if (!container.getJSONArray("ports").isEmpty()) {
                JSONArray ports = container.getJSONArray("ports");
                Integer publicPort = ports.getJSONObject(0).getInt("publicPort");

                newContainer.put("publicPort", publicPort);
            } else {

                int temp = 0;
                for (String key : JSONFileObj.keySet()) {
                    JSONObject tempContainer = JSONFileObj.getJSONObject(key);
                    String tempId = tempContainer.getString("id");

                    if (newContainer.getString("id").equals(tempId)) {

                        if (!tempContainer.has("publicPort")) {
                            newContainer.put("state", "unconfigured");

                            System.out.println("====================================================================");
                            System.out.println("\n\n\n\\u001B[1;4;31m\" +  \"Warning:\" + \"\\u001B[0;37m\" + \" The container " + name + " has not been set up correctly, please make sure that it is running and reconfigure it.\n\n\n");
                            System.out.println("====================================================================");
                        } else {
                            Integer publicPort = tempContainer.getInt("publicPort");

                            newContainer.put("publicPort", publicPort);
                        }
                        break;
                    }
                    temp++;
                    if (temp == JSONFileObj.length()) {
                        newContainer.put("state", "unconfigured");

                        System.out.println("====================================================================");
                        System.out.println("\n\n\n\u001B[1;4;31m" +  "Warning:" + "\u001B[0;37m" + " The container " + name + " has not been set up correctly, please make sure that it is running and reconfigure it.\n\n\n");
                        System.out.println("====================================================================");
                    }
                }
            }
            JSONFileObj.put(name, newContainer);

            updateJSONFile(JSONFileObj);
        }
    }

    private static int findContainerInterval(JSONObject currentContainer, JSONArray existingIdArr, JSONObject JSONFileObj) {
        int count = 0;
        String currentId = currentContainer.getString("id");
        for (int i = 0; i < existingIdArr.length(); i++) {

            if (existingIdArr.getString(i).equals(currentId)) {

                String currentName = currentContainer.
                        getJSONArray("names").
                        getString(0).
                        substring(1);

                return JSONFileObj.getJSONObject(currentName).getInt("interval");
            } else {
                count++;
                if (count == existingIdArr.length()) {
                    return DEFAULT_INTERVAL_TIME; 
                }
            }
        }
        return DEFAULT_INTERVAL_TIME; 
    }

    public static void updateJSONFile(JSONObject JSONFileObj) {
        if (Files.exists(containerListPath)) {
            try {

                Files.writeString(containerListPath, JSONFileObj.toString(4));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void printAllContainers(JSONObject JSONFileObj, Scanner scanner) {
        System.out.println("Do you want to print all containers from the JSON file? (y/n)");
        while (true) {
            String response = scanner.nextLine();

            if (response.toLowerCase().equals("y")) { 
                int count = 1;
                for (String key : JSONFileObj.keySet()) {
                    JSONObject tempContainer = JSONFileObj.getJSONObject(key);
                    String name = tempContainer.getString("name");
                    String id = tempContainer.getString("id");
                    String image = tempContainer.getString("image");
                    Integer interval = tempContainer.getInt("interval");
                    String state = tempContainer.getString("state");

                    System.out.println("\n" + count + ") Name: " + name);
                    System.out.println(" Id: " + id);
                    System.out.println(" Image: " + image);
                    System.out.println(" Interval: " + interval);
                    System.out.println(" State: " + state + "\n");
                    count++;
                }
                return;
            } else if (response.toLowerCase().equals("n")) {
                return;
            }
        }
    }
}
