package P3.Backend.Docker;

import org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi.ecCVCDSA224;
import org.json.JSONArray;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import P3.Backend.Docker.application.IntervalApplications;
import P3.Backend.Docker.application.SetupApplications;
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

import static P3.Backend.Docker.Persistent.CURRENT_CONTAINER_PATH;


@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		DockerClient dockerClient = DockerClientBuilder.dockerConnection();

		dockerClient.pingCmd().exec();

		DockerStatsService dockerStatsService = new DockerStatsService(dockerClient);
		String containerId = "4a32465d120ec154152b061db52ce9a31ba83e92e8a8c8515d6fa6e8f3be0400";
		try {
			ContainerStats stats = dockerStatsService.getContainerStats(containerId);

            System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            System.out.println(dockerClient.inspectContainerCmd(containerId).withSize(true).exec().toString());
            System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");


			// Print stats only when successfully retrieved (stats is in-scope here)
			System.out.println("CPU Total Usage: " + stats.getCpuTotalUsage()); // Total accumulated CPU time (in nanosec) used by the container since startup.
			System.out.println("Memory Usage: " + stats.getMemoryUsage()); // Current RAM usage of the container
			System.out.println("Memory Limit: " + stats.getMemoryLimit()); // Maximum amount of RAM the container can use.
			System.out.println("PIDs: " + stats.getPids()); // The number of active processes currently running (processes and threads)
            
		} catch (InterruptedException e) {
			System.err.println("Failed to get container stats: " + e.getMessage());
			Thread.currentThread().interrupt();
		}

        // Decide what part of the application user want to use:
    
        //IntervalApplications.Initiation(dockerClient);
        SetupApplications.Initiation(dockerClient);


        //////////////////////////////////////////////////////////////
        //  Containers slettet fra JSON, skal kommunikeres videre   //
        //////////////////////////////////////////////////////////////

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
