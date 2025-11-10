package REST;

import EJB.AdminSessionBeanLocal;
import Entity.Book;
import Entity.Booktype;
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
    
    // ==================== BOOKTYPE CRUD ====================
    //get :- http://localhost:8080/BookStore/resources/admin/booktypes
    @GET
    @Path("booktypes")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Booktype> getAllBooktypes() {
        return adminSessionBean.getAllBooktypes();
    }

    //get:- http://localhost:8080/BookStore/resources/admin/booktypes/1
    @GET
    @Path("booktypes/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Booktype getBooktypeById(@PathParam("id") Integer id) {
        return adminSessionBean.getBooktypeById(id);
    }

    //Post :- http://localhost:8080/BookStore/resources/admin/booktypes
    @POST
    @Path("booktypes")
    public void addBooktype(Booktype bt) {
        adminSessionBean.addBooktype(bt.getType(), bt.getDescription());
    }

    //put:- http://localhost:8080/BookStore/resources/admin/booktypes/1
    @PUT
    @Path("booktypes/{id}")
    public void updateBooktype(@PathParam("id") Integer id, Booktype bt) {
        adminSessionBean.updateBooktype(id, bt.getType(), bt.getDescription());
    }

    //delete :- http://localhost:8080/BookStore/resources/admin/booktypes/1
    @DELETE
    @Path("booktypes/{id}")
    public void deleteBooktype(@PathParam("id") Integer id) {
        adminSessionBean.deleteBooktype(id);
    }

    // ==================== BOOK CRUD ====================

    //get:-http://localhost:8080/BookStore/resources/admin/books
    @GET
    @Path("books")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Book> getAllBooks() {
        return adminSessionBean.getAllBooks();
    }

    //get:-http://localhost:8080/BookStore/resources/admin/books/1
    @GET
    @Path("books/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Book getBookById(@PathParam("id") Integer id) {
        return adminSessionBean.findBookById(id);
    }

    //post:-http://localhost:8080/BookStore/resources/admin/books
    @POST
    @Path("books")
    public void addBook(Book book) {
        adminSessionBean.addBook(
            book.getBookname(),
            book.getAuthorname(),
            book.getPrice().doubleValue(),
            book.getBooktypeId().getId()
        );
    }

    //put:-http://localhost:8080/BookStore/resources/admin/books/1
    @PUT
    @Path("books/{id}")
    public void updateBook(@PathParam("id") Integer id, Book book) {
        adminSessionBean.updateBook(
            id,
            book.getBookname(),
            book.getAuthorname(),
            book.getPrice().doubleValue(),
            book.getBooktypeId().getId()
        );
    }

    //delete:-http://localhost:8080/BookStore/resources/admin/books/1
    @DELETE
    @Path("books/{id}")
    public void deleteBook(@PathParam("id") Integer id) {
        adminSessionBean.deleteBook(id);
    }
}
