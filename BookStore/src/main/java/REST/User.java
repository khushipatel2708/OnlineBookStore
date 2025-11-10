package REST;

import EJB.UserSessionBeanLocal;
import Entity.Shipping;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.Collection;

@Path("user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class User {

    @EJB
    private UserSessionBeanLocal userSessionBean;

    // ✅ GET all shipping records
    // URL: http://localhost:8080/BookStore/resources/user/shipping
    @GET
    @Path("shipping")
    public Collection<Shipping> getAllShipping() {
        return userSessionBean.getAllShippings();
    }

    // ✅ GET single shipping by ID
    // URL: http://localhost:8080/BookStore/resources/user/shipping/{id}
    @GET
    @Path("shipping/{id}")
    public Shipping getShippingById(@PathParam("id") int id) {
        return userSessionBean.getShippingById(id);
    }

    // ✅ ADD new shipping
    // URL: http://localhost:8080/BookStore/resources/user/shipping/add
    @POST
    @Path("shipping/add")
    public void addShipping(Shipping s) {
        userSessionBean.addShipping(
            s.getUserid().getId(),       // assuming getUserid() returns a User entity
            s.getCityid() != null ? s.getCityid().getId() : null,
            s.getName(),
            s.getPhone(),
            s.getAddress1(),
            s.getAddress2(),
            s.getLandmark(),
            s.getPincode()
        );
    }

    // ✅ UPDATE existing shipping
    // URL: http://localhost:8080/BookStore/resources/user/shipping/update/{id}
    @PUT
    @Path("shipping/update/{id}")
    public void updateShipping(@PathParam("id") int id, Shipping s) {
        userSessionBean.updateShipping(
            id,
            s.getUserid().getId(),
            s.getCityid() != null ? s.getCityid().getId() : null,
            s.getName(),
            s.getPhone(),
            s.getAddress1(),
            s.getAddress2(),
            s.getLandmark(),
            s.getPincode()
        );
    }

    // ✅ DELETE shipping
    // URL: http://localhost:8080/BookStore/resources/user/shipping/delete/{id}
    @DELETE
    @Path("shipping/delete/{id}")
    public void deleteShipping(@PathParam("id") int id) {
        userSessionBean.removeShipping(id);
    }
}
