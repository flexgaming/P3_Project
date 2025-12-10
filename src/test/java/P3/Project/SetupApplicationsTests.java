package P3.Project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import P3.Backend.ExternalServer.Docker.application.SetupApplications;
import P3.Project.util.TestResult;

@SpringBootTest(classes = SetupApplications.class)
@ExtendWith(TestResult.class)
public class SetupApplicationsTests {

    @Test
    void contextLoads() {
    }
}

// TODO Send POST/PUT request with JSONArray of containers, Parameters: CompanyID, ContainerIDs, ContainerName, ContainerRunning.
// TODO Send POST/PUT when JSON file is updated, send (in)active containers when removed.
