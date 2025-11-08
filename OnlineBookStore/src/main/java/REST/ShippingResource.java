package REST;

import EJB.UserSessionBeanLocal;
import Entity.Shipping;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/shipping")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShippingResource{

    @EJB
    private UserSessionBeanLocal shippingBean;

    // ✅ Add Shipping
    @POST
    @Path("/add")
    public String addShipping(Shipping s) {
        try {
            shippingBean.addShipping(s);
            return "{\"message\":\"Shipping added successfully\"}";
        } catch (Exception e) {
            return "{\"error\":\"" + e.getMessage() + "\"}";
        }
    }

    // ✅ Update Shipping
    @PUT
    @Path("/update")
    public String updateShipping(Shipping s) {
        try {
            shippingBean.updateShipping(s);
            return "{\"message\":\"Shipping updated successfully\"}";
        } catch (Exception e) {
            return "{\"error\":\"" + e.getMessage() + "\"}";
        }
    }

    // ✅ Delete Shipping by ID
    @DELETE
    @Path("/delete/{id}")
    public String deleteShipping(@PathParam("id") int id) {
        try {
            shippingBean.deleteShipping(id);
            return "{\"message\":\"Shipping deleted successfully\"}";
        } catch (Exception e) {
            return "{\"error\":\"" + e.getMessage() + "\"}";
        }
    }

    // ✅ Get Shipping by ID
    @GET
    @Path("/{id}")
    public Shipping getShippingById(@PathParam("id") int id) {
        return shippingBean.getShippingById(id);
    }

    // ✅ Get Shipping by User ID
    @GET
    @Path("/user/{userid}")
    public List<Shipping> getShippingByUser(@PathParam("userid") int userid) {
        return shippingBean.getShippingByUser(userid);
    }
}
