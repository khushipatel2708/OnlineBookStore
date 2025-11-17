package REST;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("resources")
public class ApplicationConfig extends Application {
    // This automatically registers all @Path classes in your project
}
