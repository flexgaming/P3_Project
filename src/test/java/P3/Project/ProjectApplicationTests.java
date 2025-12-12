package P3.Project;

import java.nio.file.Files;
import java.nio.file.Path;

import static P3.Backend.ExternalServer.Docker.builder.DockerClientBuilder.dockerConnection;
import static P3.Backend.ExternalServer.application.SetupApplications.updateJSONFile;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import org.json.JSONObject;

import com.github.dockerjava.api.DockerClient;

import P3.Backend.ExternalServer.application.ExternalApplication;
import P3.Project.util.TestResult;

import static P3.Backend.ExternalServer.Docker.Persistent.CONTAINER_NAME;
import static P3.Backend.ExternalServer.Docker.Persistent.CURRENT_CONTAINER_PATH;


@SpringBootTest(classes = ExternalApplication.class)
@ExtendWith(TestResult.class)
class ProjectApplicationTests {
	@Test
	void DockerConnectionTest() {
		DockerClient dockerClient = dockerConnection();
		assertNotNull(dockerClient, "Could not connect to Docker - dockerClient is null");

		assertDoesNotThrow(()->dockerClient.pingCmd().exec(), "Could not ping to Docker");
		/* try {
			dockerClient.pingCmd().exec();
		} catch (Exception e) {
			fail("Could not ping to Docker");
		} */
	}

	// TODO Needs to be redone to work properly.
	/*@Test
	void ListAllContainersTest() throws JSONException {
		DockerClient dockerClient = dockerConnection();

		 JSONArray containers = assertDoesNotThrow(
			() -> DockerClientManager.ListAllContainers(dockerClient),
			"An error occured while trying to get a list of all containers.");

		List<Container> containersTemp = assertDoesNotThrow( () -> dockerClient.listContainersCmd().withShowAll(true).exec(),
		 													"An error occured while trying to get a list of all containers.");
		JSONArray containers = new JSONArray(containersTemp);

		assertNotNull(containers, "Container list should not be null");
		System.out.print("# of containers:" + containers.length());
		for (int i = 0; i < containers.length(); i++) {
			org.json.JSONObject c = containers.getJSONObject(i);
			assertNotNull(c, "Element" + i + " should not be null.");
			//assertNotNull(containers.getJSONObject(i), "Element " + i + " is not a JSONObject");
			//assertTrue(containers.get(i), "Element " + i + " is not a JSONObject");

			assertTrue(c.has("Id") || c.has("id"), "Container missing Id");
			assertTrue(c.has("Names") || c.has("names"), "Container missing Names");
		}
	} */


    // The path for where the JSON file is stored.
    private static final Path containerListPath = Path.of(CURRENT_CONTAINER_PATH + CONTAINER_NAME);

	// TODO Check if containers are put in the currentContainers file.
	@Test
	void ensureAddedContainersTest() {

		try {
			String content = Files.readString(containerListPath);
			JSONObject JSONFile = new JSONObject(content);


            // Make a JSON object that contains all of the relevant information.
            JSONObject newContainer = new JSONObject();
            newContainer.put("name", "Joachim-the-container");
            newContainer.put("id", "107401j");
            newContainer.put("interval", 35);
            newContainer.put("state", "inactive");

            // Add the new container to the existing content.
            JSONFile.put("Joachim-the-container", newContainer);

			updateJSONFile(JSONFile);


		} catch (Exception e) {

		}

	}


	// TODO Check if containers we insert ourselves in the currentContainers file becomes apart of the oldContainersArray, as they should.
	@Test
	void yes() {}

	// TODO





}

