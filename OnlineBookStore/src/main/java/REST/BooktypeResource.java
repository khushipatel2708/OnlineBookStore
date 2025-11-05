package REST;

import EJB.BooktypeSessionBeanLocal;
import Entity.Booktype;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/booktype")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BooktypeResource {

    @EJB
    private BooktypeSessionBeanLocal booktypeBean;

    // ✅ CREATE
    @POST
    @Path("/add")
    public Response addBooktype(Booktype booktype) {
        boolean created = booktypeBean.create(booktype);
        if (created) {
            return Response.status(Response.Status.CREATED).entity("Booktype added successfully!").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to add booktype.").build();
        }
    }

    // ✅ READ ALL
    @GET
    @Path("/list")
    public Response getAllBooktypes() {
        List<Booktype> list = booktypeBean.findAll();
        return Response.ok(list).build();
    }

    // ✅ READ BY ID
    @GET
    @Path("/{id}")
    public Response getBooktypeById(@PathParam("id") Integer id) {
        Booktype b = booktypeBean.find(id);
        if (b != null) {
            return Response.ok(b).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Booktype not found").build();
        }
    }

    // ✅ UPDATE
    @PUT
    @Path("/update/{id}")
    public Response updateBooktype(@PathParam("id") Integer id, Booktype booktype) {
        Booktype existing = booktypeBean.find(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Booktype not found").build();
        }

        booktype.setId(id);
        boolean updated = booktypeBean.update(booktype);
        if (updated) {
            return Response.ok("Booktype updated successfully!").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to update booktype.").build();
        }
    }

    // ✅ DELETE
    @DELETE
    @Path("/delete/{id}")
    public Response deleteBooktype(@PathParam("id") Integer id) {
        boolean deleted = booktypeBean.delete(id);
        if (deleted) {
            return Response.ok("Booktype deleted successfully!").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Booktype not found or could not be deleted").build();
        }
    }
}
