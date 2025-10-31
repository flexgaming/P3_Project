package P3.Backend;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
	public static void main(String[] args) {
        // Run main using Spring Boot
		// SpringApplication.run(App.class, args);

        Database db = new Database();
        ArrayList<Region> regions = db.getRegions();

        for (Region region : regions) {
            System.out.print(region.getRegionID() + " ");
            System.out.println(region.getRegionName());
        }
	}

}
