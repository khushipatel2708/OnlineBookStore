package REST;

import EJB.AdminSessionBeanLocal;
import Entity.Book;
import Entity.City;
import jakarta.ejb.EJB;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.Collection;

@Path("admin") // URL path: http://localhost:8080/bookstore/resources/admin
public class Admin {

    @EJB
    private AdminSessionBeanLocal adminSessionBean;

    @GET
    @Path("books") // Final URL: http://localhost:8080/BookStore/resources/admin/books
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Book> getAllBooks() {
        return adminSessionBean.getAllBook();
    }
    
    @GET
    @Path("cities") // GET http://localhost:8080/BookStore/resources/admin/cities
    public Collection<City> getAllCities() {
        return adminSessionBean.getAllCities();
    }

    @POST
    @Path("cities") // POST http://localhost:8080/BookStore/resources/admin/cities
    public void addCity(City city) {
        adminSessionBean.addCity(city.getName());
    }

    @PUT
    @Path("cities/{id}") // PUT http://localhost:8080/BookStore/resources/admin/cities/1
    public void updateCity(@PathParam("id") Integer id, City city) {
        adminSessionBean.updateCity(id, city.getName());
    }

    @DELETE
    @Path("cities/{id}") // DELETE http://localhost:8080/BookStore/resources/admin/cities/1
    public void removeCity(@PathParam("id") Integer id) {
        adminSessionBean.removeCity(id);
    }

    @GET
    @Path("cities/{id}") // GET http://localhost:8080/BookStore/resources/admin/cities/1
    public City getCityById(@PathParam("id") Integer id) {
        return adminSessionBean.findCityById(id);
    }

    @GET
    @Path("cities/search/{name}") // GET http://localhost:8080/BookStore/resources/admin/cities/search/Surat
    public Collection<City> getCityByName(@PathParam("name") String name) {
        return adminSessionBean.findCityByName(name);
    }
}
