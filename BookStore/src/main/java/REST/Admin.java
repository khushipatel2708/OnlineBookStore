package REST;

import EJB.AdminSessionBeanLocal;
import Entity.Book;
import Entity.Booktype;
import Entity.City;
import Entity.GroupMaster;
import Entity.User;
import jakarta.annotation.security.PermitAll;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Collection;

@Path("admin")
@PermitAll
public class Admin {

    @EJB
    private AdminSessionBeanLocal adminSessionBean;

    // ================= USER CRUD =================
    @GET
    @Path("users")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<User> getAllUsers() {
        return adminSessionBean.getAllUsers();
    }

    @GET
    @Path("users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserById(@PathParam("id") Integer id) {
        return adminSessionBean.findUserById(id);
    }

    @POST
    @Path("users")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(User user) {
        adminSessionBean.addUser(
                user.getFullname(),
                user.getPhone(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getStatus(),
                user.getGroupid().getGroupid()
        );
        return Response.ok("{\"status\":\"User Added\"}").build();
    }

    @PUT
    @Path("users/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") Integer id, User user) {
        adminSessionBean.updateUser(
                id,
                user.getFullname(),
                user.getPhone(),
                user.getUsername(),
                user.getEmail(),
                user.getStatus(),
                user.getGroupid().getGroupid()
        );
        return Response.ok("{\"status\":\"User Updated\"}").build();
    }

    @DELETE
    @Path("users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") Integer id) {
        adminSessionBean.deleteUser(id);
        return Response.ok("{\"status\":\"User Deleted\"}").build();
    }

    // ================= CITY CRUD =================
    @GET
    @Path("cities/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public City getCityById(@PathParam("id") Integer id) {
        return adminSessionBean.findCityById(id);
    }

    @POST
    @Path("cities")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCity(City city) {
        adminSessionBean.addCity(city.getName());
        return Response.ok("{\"status\":\"City Added\"}").build();
    }

    @PUT
    @Path("cities/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCity(@PathParam("id") Integer id, City city) {
        adminSessionBean.updateCity(id, city.getName());
        return Response.ok("{\"status\":\"City Updated\"}").build();
    }

    @DELETE
    @Path("cities/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCity(@PathParam("id") Integer id) {
        adminSessionBean.removeCity(id);
        return Response.ok("{\"status\":\"City Deleted\"}").build();
    }

    @GET
    @Path("cities/search/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<City> searchCity(@PathParam("name") String name) {
        return adminSessionBean.findCityByName(name);
    }

    // ================= BOOKTYPE CRUD =================
    @GET
    @Path("booktypes")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Booktype> getAllBooktypes() {
        return adminSessionBean.getAllBooktypes();
    }

    @GET
    @Path("cities")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<City> getAllCities() {
        return adminSessionBean.getAllCities();
    }

    @GET
    @Path("booktypes/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Booktype getBooktypeById(@PathParam("id") Integer id) {
        return adminSessionBean.getBooktypeById(id);
    }

    @POST
    @Path("booktypes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBooktype(Booktype bt) {
        adminSessionBean.addBooktype(bt.getType(), bt.getDescription());
        return Response.ok("{\"status\":\"Booktype Added\"}").build();
    }

    @PUT
    @Path("booktypes/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBooktype(@PathParam("id") Integer id, Booktype bt) {
        adminSessionBean.updateBooktype(id, bt.getType(), bt.getDescription());
        return Response.ok("{\"status\":\"Booktype Updated\"}").build();
    }

    @DELETE
    @Path("booktypes/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBooktype(@PathParam("id") Integer id) {
        adminSessionBean.deleteBooktype(id);
        return Response.ok("{\"status\":\"Booktype Deleted\"}").build();
    }

    // ================= BOOK CRUD =================
    @GET
    @Path("books")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Book> getAllBooks() {
        return adminSessionBean.getAllBooks();
    }

    @GET
    @Path("books/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Book getBookById(@PathParam("id") Integer id) {
        return adminSessionBean.findBookById(id);
    }

    @POST
    @Path("books")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBook(Book book) {
        adminSessionBean.addBook(
                book.getBookname(),
                book.getAuthorname(),
                book.getPrice().doubleValue(),
                book.getBooktypeId().getId(),
                book.getCoverPhoto(),
                book.getFrontPagePhoto(),
                book.getLastPagePhoto()
        );
        return Response.ok("{\"status\":\"Book Added\"}").build();
    }

    @PUT
    @Path("books/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("id") Integer id, Book book) {
        adminSessionBean.updateBook(
                id,
                book.getBookname(),
                book.getAuthorname(),
                book.getPrice().doubleValue(),
                book.getBooktypeId().getId(),
                book.getCoverPhoto(),
                book.getFrontPagePhoto(),
                book.getLastPagePhoto()
        );
        return Response.ok("{\"status\":\"Book Updated\"}").build();
    }

    @DELETE
    @Path("books/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBook(@PathParam("id") Integer id) {
        adminSessionBean.deleteBook(id);
        return Response.ok("{\"status\":\"Book Deleted\"}").build();
    }

    @GET
    @Path("books/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Book> searchBooks(
            @QueryParam("bookname") String bookname,
            @QueryParam("author") String author,
            @QueryParam("booktype") String booktype
    ) {
        return adminSessionBean.searchBooks(bookname, author, booktype);
    }

    // ================= GROUP CRUD =================
    @GET
    @Path("groups")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<GroupMaster> getAllGroups() {
        return adminSessionBean.getAllGroups();
    }

    @GET
    @Path("groups/search/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<GroupMaster> searchGroup(@PathParam("name") String name) {
        return adminSessionBean.findGroupByName(name);
    }

    @POST
    @Path("groups")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addGroup(GroupMaster group) {
        adminSessionBean.addGroup(group.getGroupname(), group.getUsername());
        return Response.ok("{\"status\":\"Group Added\"}").build();
    }

    @PUT
    @Path("groups/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateGroup(@PathParam("id") Integer id, GroupMaster group) {
        adminSessionBean.updateGroup(id, group.getGroupname(), group.getUsername());
        return Response.ok("{\"status\":\"Group Updated\"}").build();
    }

    @DELETE
    @Path("groups/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteGroup(@PathParam("id") Integer id) {
        adminSessionBean.removeGroup(id);
        return Response.ok("{\"status\":\"Group Deleted\"}").build();
    }
    
}
