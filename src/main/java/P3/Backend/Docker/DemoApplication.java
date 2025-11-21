package P3.Backend.Docker;

import org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi.ecCVCDSA224;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.reactive.function.client.WebClient;

import P3.Backend.Docker.application.IntervalApplications;
import P3.Backend.Docker.application.SetupApplications;
import P3.Backend.Docker.builder.DockerClientBuilder;
import P3.Backend.Docker.classes.ContainerClass;
import P3.Backend.Docker.manager.DockerClientManager;
import P3.Backend.Docker.manager.DockerStatsService;
import P3.Backend.Docker.manager.DockerStatsService.ContainerStats;
import reactor.core.publisher.Mono;

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

import static P3.Backend.Docker.Persistent.CURRENT_CONTAINER_PATH;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        DockerClient dockerClient = DockerClientBuilder.dockerConnection();
        

        dockerClient.pingCmd().exec();



        DockerStatsService dockerStatsService = new DockerStatsService(dockerClient);

        // Get the Spring-managed bean
        IntervalApplications intervalApp = context.getBean(IntervalApplications.class);
        intervalApp.fetchSpringActuatorStats("http://localhost");

        // String containerId = "4a32465d120ec154152b061db52ce9a31ba83e92e8a8c8515d6fa6e8f3be0400";
        // try {
        // 	ContainerStats stats = dockerStatsService.getContainerStats(containerId);

        //     System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        //     System.out.println(dockerClient.inspectContainerCmd(containerId).exec().toString());
        //     System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        // 	// Print stats only when successfully retrieved (stats is in-scope here)
        // 	System.out.println("CPU Total Usage: " + stats.getCpuTotalUsage()); // Total accumulated CPU time (in nanosec) used by the container since startup.
        // 	System.out.println("Memory Usage: " + stats.getMemoryUsage()); // Current RAM usage of the container
        // 	System.out.println("Memory Limit: " + stats.getMemoryLimit()); // Maximum amount of RAM the container can use.
        // 	System.out.println("PIDs: " + stats.getPids()); // The number of active processes currently running (processes and threads)
        // } catch (InterruptedException e) {
        // 	System.err.println("Failed to get container stats: " + e.getMessage());
        // 	Thread.currentThread().interrupt();
        // }
        // Decide what part of the application user want to use:
        //IntervalApplications.Initiation(dockerClient);
        SetupApplications.Initiation(dockerClient);
		
		
	}


}
