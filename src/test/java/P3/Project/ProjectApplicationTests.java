package P3.Project;

import static P3.Backend.Docker.builder.DockerClientBuilder.dockerConnection;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.dockerjava.api.DockerClient;

import P3.Project.util.TestResult;
import P3.Backend.Docker.DemoApplication;
import P3.Backend.Docker.manager.DockerClientManager;

import org.json.JSONArray;
import org.json.JSONException;

@SpringBootTest(classes = DemoApplication.class)
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

	@Test
	void ListAllContainersTest() throws JSONException {
		DockerClient dockerClient = dockerConnection();

		JSONArray containers = assertDoesNotThrow(
			() -> DockerClientManager.ListAllContainers(dockerClient),
			"An error occured while trying to get a list of all containers.");

		assertNotNull(containers, "Container list should not be null");

		for (int i = 0; i < containers.length(); i++) {
			assertTrue(containers.get(i) instanceof org.json.JSONObject, "Element " + i + " is not a JSONObject");
			org.json.JSONObject c = containers.getJSONObject(i);

			assertTrue(c.has("Id") || c.has("id"), "Container missing Id");
			assertTrue(c.has("Names") || c.has("names"), "Container missing Names");
		}
	}

	// TODO Tjek om containers man selv sætter ind på currentCotainers bliver en del af oldContainersArray.
	// TODO Tjek om containers bliver sat ind på currentContainer.



}

