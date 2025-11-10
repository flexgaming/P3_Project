package P3.Project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SetupApplicationsTests {
    @Test
	void contextLoads() {
	}
}

// TODO Send POST/PUT request with JSONArray of containers, Parameters: CompanyID, ContainerIDs, ContainerName, ContainerRunning.
// TODO Send POST/PUT when JSON file is updated, send (in)active containers when removed.