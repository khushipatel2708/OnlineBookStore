package REST;

import EJB.AdminSessionBeanLocal;
import Entity.Book;
import Entity.Booktype;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/book")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    @EJB
    private AdminSessionBeanLocal bookSessionBean;

    // ✅ Get all books
    @GET
    @Path("/list")
    public Response getAllBooks() {
        List<Book> books = bookSessionBean.getAllBooks();
        return Response.ok(books).build();
    }

    // ✅ Get single book by ID
    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") int id) {
        Book book = bookSessionBean.getBookById(id);
        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\": \"Book not found\"}")
                    .build();
        }
        return Response.ok(book).build();
    }

    // ✅ Add new book
    @POST
    @Path("/add")
    public Response addBook(Book book) {
        try {
            // ensure valid booktype
            if (book.getBooktype() != null && book.getBooktype().getId() != 0) {
                Booktype bt = new Booktype();
                bt.setId(book.getBooktype().getId());
                book.setBooktype(bt);
            }
            bookSessionBean.addBook(book);
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Book added successfully\"}")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    // ✅ Update book by ID
    @PUT
    @Path("/update/{id}")
    public Response updateBook(@PathParam("id") int id, Book book) {
        try {
            Book existing = bookSessionBean.getBookById(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"message\": \"Book not found\"}")
                        .build();
            }

            book.setId(id);
            bookSessionBean.updateBook(book);
            return Response.ok("{\"message\": \"Book updated successfully\"}").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    // ✅ Delete book
    @DELETE
    @Path("/delete/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        try {
            bookSessionBean.deleteBook(id);
            return Response.ok("{\"message\": \"Book deleted successfully\"}").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    // ✅ Get books by type (filter)
    @GET
    @Path("/byType/{typeId}")
    public Response getBooksByType(@PathParam("typeId") int typeId) {
        List<Book> books = bookSessionBean.getBooksByType(typeId);
        return Response.ok(books).build();
    }
}