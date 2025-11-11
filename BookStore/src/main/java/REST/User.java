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
    
    // ====================== CART MANAGEMENT =======================

    // ✅ ADD TO CART
    // URL: http://localhost:8080/BookStore/resources/user/cart/add
    // METHOD: POST
    // BODY:
    // {
    //   "userId": {"id": 1},
    //   "bookId": {"id": 3},
    //   "quantity": 2
    // }
    @POST
    @Path("cart/add")
    public void addToCart(Entity.Cart cart) {
        userSessionBean.addToCart(
            cart.getUserId().getId(),
            cart.getBookId().getId(),
            cart.getQuantity()
        );
    }

    // ✅ GET ALL CART ITEMS FOR USER
    // URL: http://localhost:8080/BookStore/resources/user/cart/{userId}
    // METHOD: GET
    @GET
    @Path("cart/{userId}")
    public Collection<Entity.Cart> getCartByUser(@PathParam("userId") Integer userId) {
        return userSessionBean.getCartByUser(userId);
    }

    // ✅ UPDATE CART QUANTITY
    // URL: http://localhost:8080/BookStore/resources/user/cart/update/{cartId}
    // METHOD: PUT
    // BODY:
    // {
    //   "quantity": 3
    // }
    @PUT
    @Path("cart/update/{cartId}")
    public void updateCartQuantity(@PathParam("cartId") Integer cartId, Entity.Cart cart) {
        userSessionBean.updateCartQuantity(cartId, cart.getQuantity());
    }

    // ✅ DELETE CART ITEM
    // URL: http://localhost:8080/BookStore/resources/user/cart/delete/{cartId}
    // METHOD: DELETE
    @DELETE
    @Path("cart/delete/{cartId}")
    public void deleteCartItem(@PathParam("cartId") Integer cartId) {
        userSessionBean.deleteCartItem(cartId);
    }

}
