package P3.Project;

import java.nio.file.Files;
import java.nio.file.Path;

import static P3.Backend.ExternalServer.Docker.builder.DockerClientBuilder.dockerConnection;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import org.json.JSONObject;

import com.github.dockerjava.api.DockerClient;

import P3.Project.util.TestResult;
import P3.Backend.ExternalServer.Docker.ExternalApplication;

import static P3.Backend.ExternalServer.Docker.Persistent.CONTAINER_NAME;
import static P3.Backend.ExternalServer.Docker.Persistent.CURRENT_CONTAINER_PATH;
import static P3.Backend.ExternalServer.Docker.application.SetupApplications.updateJSONFile;


@SpringBootTest(classes = ExternalApplication.class)
@ExtendWith(TestResult.class)
class ProjectApplicationTests {
	@Test
	void DockerConnectionTest() {
		DockerClient dockerClient = dockerConnection();
		assertNotNull(dockerClient, "Could not connect to Docker - dockerClient is null");

		assertDoesNotThrow(()->dockerClient.pingCmd().exec(), "Could not ping to Docker");
		
	}

	
	


    
    private static final Path containerListPath = Path.of(CURRENT_CONTAINER_PATH + CONTAINER_NAME);

	
	@Test
	void ensureAddedContainersTest() {

		try {
			String content = Files.readString(containerListPath);
			JSONObject JSONFile = new JSONObject(content);


            
            JSONObject newContainer = new JSONObject();
            newContainer.put("name", "Joachim-the-container");
            newContainer.put("id", "107401j");
            newContainer.put("interval", 35);
            newContainer.put("state", "inactive");

            
            JSONFile.put("Joachim-the-container", newContainer);

			updateJSONFile(JSONFile);


		} catch (Exception e) {

		}

	}


	
	@Test
	void yes() {}

	





}

