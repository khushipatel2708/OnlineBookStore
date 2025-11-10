package REST;

import EJB.AdminSessionBeanLocal;
import Entity.Book;
import Entity.Booktype;
import Entity.City;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminResource {

    @EJB
    private AdminSessionBeanLocal adminBean;

    // ================================
    // ✅ BOOKTYPE API
    // ================================

    @POST
    @Path("/booktype/add")
    public Response addBooktype(Booktype booktype) {
        boolean created = adminBean.create(booktype);
        if (created) {
            return Response.status(Response.Status.CREATED).entity("{\"message\":\"Booktype added successfully!\"}").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\":\"Failed to add booktype.\"}").build();
        }
    }

    @GET
    @Path("/booktype/list")
    public Response getAllBooktypes() {
        List<Booktype> list = adminBean.findAll();
        return Response.ok(list).build();
    }

    @GET
    @Path("/booktype/{id}")
    public Response getBooktypeById(@PathParam("id") Integer id) {
        Booktype b = adminBean.find(id);
        if (b != null) {
            return Response.ok(b).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"message\":\"Booktype not found\"}").build();
        }
    }

    @PUT
    @Path("/booktype/update/{id}")
    public Response updateBooktype(@PathParam("id") Integer id, Booktype booktype) {
        Booktype existing = adminBean.find(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"message\":\"Booktype not found\"}").build();
        }

        booktype.setId(id);
        boolean updated = adminBean.update(booktype);
        if (updated) {
            return Response.ok("{\"message\":\"Booktype updated successfully!\"}").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\":\"Failed to update booktype.\"}").build();
        }
    }

    @DELETE
    @Path("/booktype/delete/{id}")
    public Response deleteBooktype(@PathParam("id") Integer id) {
        boolean deleted = adminBean.delete(id);
        if (deleted) {
            return Response.ok("{\"message\":\"Booktype deleted successfully!\"}").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"message\":\"Booktype not found or could not be deleted.\"}").build();
        }
    }

    // ================================
    // ✅ BOOK API
    // ================================

    @GET
    @Path("/book/list")
    public Response getAllBooks() {
        List<Book> books = adminBean.getAllBooks();
        return Response.ok(books).build();
    }

    @GET
    @Path("/book/{id}")
    public Response getBookById(@PathParam("id") int id) {
        Book book = adminBean.getBookById(id);
        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"Book not found\"}")
                    .build();
        }
        return Response.ok(book).build();
    }

    @POST
    @Path("/book/add")
    public Response addBook(Book book) {
        try {
            if (book.getBooktype() != null && book.getBooktype().getId() != 0) {
                Booktype bt = new Booktype();
                bt.setId(book.getBooktype().getId());
                book.setBooktype(bt);
            }
            adminBean.addBook(book);
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\":\"Book added successfully\"}")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @PUT
    @Path("/book/update/{id}")
    public Response updateBook(@PathParam("id") int id, Book book) {
        try {
            Book existing = adminBean.getBookById(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"message\":\"Book not found\"}")
                        .build();
            }

            book.setId(id);
            adminBean.updateBook(book);
            return Response.ok("{\"message\":\"Book updated successfully\"}").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/book/delete/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        try {
            adminBean.deleteBook(id);
            return Response.ok("{\"message\":\"Book deleted successfully\"}").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/book/byType/{typeId}")
    public Response getBooksByType(@PathParam("typeId") int typeId) {
        List<Book> books = adminBean.getBooksByType(typeId);
        return Response.ok(books).build();
    }

    // ================================
    // ✅ CITY API
    // ================================

    @GET
    @Path("/city/list")
    public Response getAllCities() {
        List<City> cities = adminBean.getAllCities();
        return Response.ok(cities).build();
    }

    @GET
    @Path("/city/{id}")
    public Response getCityById(@PathParam("id") int id) {
        City city = adminBean.getCityById(id);
        if (city == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"City not found\"}")
                    .build();
        }
        return Response.ok(city).build();
    }

    @POST
    @Path("/city/add")
    public Response addCity(City city) {
        try {
            adminBean.addCity(city);
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\":\"City added successfully\"}")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @PUT
    @Path("/city/update/{id}")
    public Response updateCity(@PathParam("id") int id, City city) {
        try {
            City existing = adminBean.getCityById(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"message\":\"City not found\"}")
                        .build();
            }

            city.setCityid(id);
            adminBean.updateCity(city);

            return Response.ok("{\"message\":\"City updated successfully\"}").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/city/delete/{id}")
    public Response deleteCity(@PathParam("id") int id) {
        try {
            adminBean.deleteCity(id);
            return Response.ok("{\"message\":\"City deleted successfully\"}").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
