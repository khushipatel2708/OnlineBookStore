package REST;

import EJB.AdminSessionBeanLocal;
import Entity.Book;
import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
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
}
