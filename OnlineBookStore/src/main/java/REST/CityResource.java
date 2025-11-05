package REST;

import EJB.CitySessionBeanLocal;
import Entity.City;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/city")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CityResource {

    @EJB
    private CitySessionBeanLocal citySessionBean;

    // ✅ Get all cities
    @GET
    @Path("/list")
    public Response getAllCities() {
        List<City> cities = citySessionBean.getAllCities();
        return Response.ok(cities).build();
    }

    // ✅ Get city by ID
    @GET
    @Path("/{id}")
    public Response getCityById(@PathParam("id") int id) {
        City city = citySessionBean.getCityById(id);
        if (city == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\": \"City not found\"}")
                    .build();
        }
        return Response.ok(city).build();
    }

    // ✅ Add new city
    @POST
    @Path("/add")
    public Response addCity(City city) {
        try {
            citySessionBean.addCity(city);
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"City added successfully\"}")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    // ✅ Update existing city
    @PUT
    @Path("/update/{id}")
    public Response updateCity(@PathParam("id") int id, City city) {
        try {
            City existing = citySessionBean.getCityById(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"message\": \"City not found\"}")
                        .build();
            }

            city.setCityid(id); // ensure ID consistency
            citySessionBean.updateCity(city);

            return Response.ok("{\"message\": \"City updated successfully\"}").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    // ✅ Delete city
    @DELETE
    @Path("/delete/{id}")
    public Response deleteCity(@PathParam("id") int id) {
        try {
            citySessionBean.deleteCity(id);
            return Response.ok("{\"message\": \"City deleted successfully\"}").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
