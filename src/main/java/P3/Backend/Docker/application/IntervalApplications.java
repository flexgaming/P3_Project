package P3.Backend.Docker.application;

import static P3.Backend.Docker.Persistent.CURRENT_CONTAINER_PATH;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.JSONObject;

import com.github.dockerjava.api.DockerClient;
import P3.Backend.Docker.classes.IntervalClass;
import P3.Backend.Docker.classes.ContainerClass;

public class IntervalApplications {
    
    // The path for where the JSON file is stored.
    private static final Path containerListPath = Path.of("" + CURRENT_CONTAINER_PATH); // Replace "" with desired path.

    public static ContainerClass[] containerArr;
    public static IntervalClass[] intervalArr;


    public static void Initiation(DockerClient dockerClient) {

        // Set up array of container classes
            // Call function
        SetupContainerArray();

        
        // Set up interval array (id, interval, tempInterval)
            // Call function
        SetupIntervalArray(containerArr);


        // Make while loop that remove one second from each of the elements from the interval array
            // If tempInterval is 0 after removing 1 second, then fetch information and paste it into array of container class
            // Set tempInterval to be equals to interval
            // wait a second - System.out.wait...


        // While loop kunne godt være en funktion...... #"#"#"#"#"#"#"#"#"#"#"#"#"#"#"#"

        while (true) {
            try {
                // Go through each index in intervalArr and remove 1 second from tempInterval.
                // Fetch container data and put it into containerArr if index's tempInterval is 0.
                for (int i = 0; i < intervalArr.length; i++) {

                    Integer newInterval = intervalArr[i].getTempInterval() - 1;
                    
                    if (newInterval == 0) {
                        newInterval = intervalArr[i].getInterval(); // Reset the interval.
                        intervalArr[i].setTempInterval(newInterval);


                        //////////////////////////////////
                        //      FETCH FUNCTION HERE     //
                        //////////////////////////////////

                    } else {
                        intervalArr[i].setTempInterval(newInterval);
                    }
                    // Wait 1 second.
                    System.out.wait(1000);
                }
                
            } catch (Exception e) {
                // If anything goes wrong, it is printed.
                e.printStackTrace();
            }
        }

    }

    //Container class function: get all JSONFILE DATA

    private static void SetupContainerArray() { 
        try {
            // Get all of the content within the file.
            String content = Files.readString(containerListPath);

            // Convert all of the content back into a JSON format.
            JSONObject JSONFileObj = new JSONObject(content);
    
            // Create the length for the array based on the JSON file.
            containerArr = new ContainerClass[JSONFileObj.length()];
    
            int index = 0;
            for (String key : JSONFileObj.keySet()) { // Go through each of the keys in the JSON file.
                JSONObject tempContainer = JSONFileObj.getJSONObject(key); // Get the current container.
                
                //////////////////////////////////////////// 
                //      Check state of tempContainer      //        
                ////////////////////////////////////////////

                // Set variables; name, id & interval based on data from JSON file.
                String name = tempContainer.getString("name");
                String id = tempContainer.getString("id");
                Integer interval = tempContainer.getInt("interval");
                
                // Make a new ContainerClass and add it to the containerArr.
                ContainerClass newContainer = new ContainerClass(name, id, interval);
                containerArr[index] = newContainer;
    
                index++; // Make sure to move the index.
            }
            
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

// Set up the interval array based on the containerArr

    private static void SetupIntervalArray(ContainerClass[] containerArr) {
        // Create the length for the array based on the containerArr's length.
        intervalArr = new IntervalClass[containerArr.length];

        // Go through each of the elements in the containerArr and set up the intervalArr.
        for (int i = 0; i < containerArr.length; i++) {

            // Set variables; name, id & interval based on data from JSON file.
            String id = containerArr[i].getcontainerId();
            Integer interval = containerArr[i].getcontainerInterval();

            // Set the new interval element to current index of intervalArr.
            // Interval used twice is correct, the last will be tempInterval.
            IntervalClass newIntervalElement = new IntervalClass(id, interval, interval);
            intervalArr[i] = newIntervalElement;
        }
    }


}
