package REST;

import EJB.UserSessionBeanLocal;
import Entity.Cart;
import Entity.Shipping;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Collection;

@Path("user")
public class User {

    @EJB
    private UserSessionBeanLocal userSessionBean;

    // ===================== SHIPPING CRUD =====================

    @GET
    @Path("shipping")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Shipping> getAllShipping() {
        return userSessionBean.getAllShippings();
    }

    @GET
    @Path("shipping/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Shipping getShippingById(@PathParam("id") Integer id) {
        return userSessionBean.getShippingById(id);
    }

    @POST
    @Path("shipping")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addShipping(Shipping s) {

        userSessionBean.addShipping(
                s.getUserid().getId(),
                s.getCityid() != null ? s.getCityid().getId() : null,
                s.getName(),
                s.getPhone(),
                s.getAddress1(),
                s.getAddress2(),
                s.getLandmark(),
                s.getPincode()
        );

        return Response.ok("{\"status\":\"Shipping Added\"}").build();
    }

    @PUT
    @Path("shipping/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateShipping(@PathParam("id") Integer id, Shipping s) {

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

        return Response.ok("{\"status\":\"Shipping Updated\"}").build();
    }

    @DELETE
    @Path("shipping/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteShipping(@PathParam("id") Integer id) {

        userSessionBean.removeShipping(id);
        return Response.ok("{\"status\":\"Shipping Deleted\"}").build();
    }

    // ===================== CART CRUD =====================

    @POST
    @Path("cart")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addToCart(Cart cart) {

        userSessionBean.addToCart(
                cart.getUserId().getId(),
                cart.getBookId().getId(),
                cart.getQuantity()
        );

        return Response.ok("{\"status\":\"Added to Cart\"}").build();
    }

    @GET
    @Path("cart/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Cart> getCartByUser(@PathParam("userId") Integer userId) {
        return userSessionBean.getCartByUser(userId);
    }

    @PUT
    @Path("cart/{cartId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCart(@PathParam("cartId") Integer cartId, Cart cart) {

        userSessionBean.updateCartQuantity(cartId, cart.getQuantity());
        return Response.ok("{\"status\":\"Cart Updated\"}").build();
    }

    @DELETE
    @Path("cart/{cartId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCartItem(@PathParam("cartId") Integer cartId) {

        userSessionBean.deleteCartItem(cartId);
        return Response.ok("{\"status\":\"Cart Item Deleted\"}").build();
    }
}
