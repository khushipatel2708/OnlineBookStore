package REST;

import EJB.UserSessionBeanLocal;
import Entity.*;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Path("user")
@PermitAll
public class User {

    @EJB
    private UserSessionBeanLocal userBean;

    // ================= SHIPPING =================

    @GET
    @Path("shippings")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Shipping> getAllShippings() {
        return userBean.getAllShippings();
    }

    @GET
    @Path("shippings/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Shipping getShippingById(@PathParam("id") Integer id) {
        return userBean.getShippingById(id);
    }

    @GET
    @Path("shippings/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Shipping> getShippingByUser(@PathParam("userId") Integer userId) {
        return userBean.getShippingByUser(userId);
    }

    @GET
    @Path("shippings/city/{cityId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Shipping> getShippingByCity(@PathParam("cityId") Integer cityId) {
        return userBean.getShippingByCity(cityId);
    }

    @GET
    @Path("shippings/pincode/{pincode}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Shipping> getShippingByPincode(@PathParam("pincode") String pincode) {
        return userBean.getShippingByPincode(pincode);
    }

    @POST
    @Path("shippings")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addShipping(Shipping s) {
        userBean.addShipping(
                s.getUserid().getId(),
                s.getCityid().getId(),
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
    @Path("shippings/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateShipping(@PathParam("id") Integer id, Shipping s) {
        userBean.updateShipping(
                id,
                s.getUserid().getId(),
                s.getCityid().getId(),
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
    @Path("shippings/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteShipping(@PathParam("id") Integer id) {
        userBean.removeShipping(id);
        return Response.ok("{\"status\":\"Shipping Deleted\"}").build();
    }

    @GET
    @Path("shippings/latest/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Shipping getLatestShipping(@PathParam("userId") Integer userId) {
        return userBean.getLatestShippingByUser(userId);
    }

    // ================= CART =================

    @POST
    @Path("cart/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addToCart(
            @QueryParam("userId") int userId,
            @QueryParam("bookId") int bookId
    ) {
        userBean.addToCart(userId, bookId);
        return Response.ok("{\"status\":\"Added to Cart\"}").build();
    }

    @GET
    @Path("cart/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cart> getCartItems(@PathParam("userId") int userId) {
        return userBean.getCartItems(userId);
    }

    @GET
    @Path("cart/item/{cartId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Cart getCartById(@PathParam("cartId") int cartId) {
        return userBean.getCartById(cartId);
    }

    @PUT
    @Path("cart/update/{cartId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateQuantity(
            @PathParam("cartId") int cartId,
            @QueryParam("change") int change
    ) {
        userBean.updateQuantity(cartId, change);
        return Response.ok("{\"status\":\"Cart Updated\"}").build();
    }

    @DELETE
    @Path("cart/{cartId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFromCart(@PathParam("cartId") int cartId) {
        userBean.removeFromCart(cartId);
        return Response.ok("{\"status\":\"Cart Removed\"}").build();
    }

    // ================= PAYMENT =================

    @GET
    @Path("payments")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Payment> getAllPayments() {
        return userBean.getAllPayments();
    }

    @POST
    @Path("payment/cod")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCODPayment(
            @QueryParam("userId") Integer userId,
            @QueryParam("bookId") Integer bookId,
            @QueryParam("amount") BigDecimal amount
    ) {
        userBean.addCODPayment(userId, bookId, amount);
        return Response.ok("{\"status\":\"COD Payment Added\"}").build();
    }

    @PUT
    @Path("payment/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePaymentStatus(
            @QueryParam("userId") Integer userId,
            @QueryParam("oldStatus") String oldStatus,
            @QueryParam("newStatus") String newStatus
    ) {
        userBean.updatePaymentStatus(userId, oldStatus, newStatus);
        return Response.ok("{\"status\":\"Payment Status Updated\"}").build();
    }

    // ================= ORDERS =================

//    @GET
//    @Path("orders/{userId}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<Orderlist> getOrdersByUser(@PathParam("userId") Integer userId) {
//        User user = new User();
//        user.setOrderId(userId);
//        return userBean.getOrdersByUser(user);
//    }

    @GET
    @Path("orders/pending")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Orderlist> getPendingOrders() {
        return userBean.getPendingOrders();
    }

    @GET
    @Path("orders/order/{orderId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Orderlist getOrderById(@PathParam("orderId") Integer orderId) {
        return userBean.getOrderById(orderId);
    }

    @PUT
    @Path("orders/deliver/{orderId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response markAsDelivered(@PathParam("orderId") Integer orderId) {
        userBean.markAsDelivered(orderId);
        return Response.ok("{\"status\":\"Order Delivered\"}").build();
    }

    // ================= BOOK STOCK =================

    @PUT
    @Path("books/stock")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBookStock(
            @QueryParam("bookId") Integer bookId,
            @QueryParam("available") int available
    ) {
        userBean.updateBookStock(bookId, available);
        return Response.ok("{\"status\":\"Stock Updated\"}").build();
    }

    // ================= FEEDBACK =================

    @POST
    @Path("feedback")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFeedback(
            @QueryParam("comments") String comments,
            @QueryParam("rating") int rating,
            @QueryParam("bookId") int bookId,
            @QueryParam("userId") int userId
    ) {
        userBean.addFeedback(comments, rating, bookId, userId);
        return Response.ok("{\"status\":\"Feedback Added\"}").build();
    }

    @GET
    @Path("feedbacks")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Feedback> getAllFeedbacks() {
        return userBean.getAllFeedbacks();
    }
}
