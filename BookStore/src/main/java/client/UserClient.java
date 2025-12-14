/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/JerseyClient.java to edit this template
 */
package client;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

/**
 * Jersey REST client generated for REST resource:User [user]<br>
 * USAGE:
 * <pre>
 *        UserClient client = new UserClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author KHUSHI PC
 */
public class UserClient {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "https://localhost:8181/BookStore/resources";

    public UserClient() {
        client = jakarta.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("user");
    }

    public <T> T getShippingByUser(Class<T> responseType, String userId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("shippings/user/{0}", new Object[]{userId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getCartItems(Class<T> responseType, String userId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("cart/{0}", new Object[]{userId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getOrderById(Class<T> responseType, String orderId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("orders/order/{0}", new Object[]{orderId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public Response deleteShipping(String id) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("shippings/{0}", new Object[]{id})).request().delete(Response.class);
    }

    public <T> T getAllPayments(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("payments");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public Response updateBookStock() throws ClientErrorException {
        return webTarget.path("books/stock").request().put(null, Response.class);
    }

    public Response addFeedback() throws ClientErrorException {
        return webTarget.path("feedback").request().post(null, Response.class);
    }

    public Response addShipping(Object requestEntity) throws ClientErrorException {
        return webTarget.path("shippings").request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).post(jakarta.ws.rs.client.Entity.entity(requestEntity, jakarta.ws.rs.core.MediaType.APPLICATION_JSON), Response.class);
    }

    public Response updatePaymentStatus() throws ClientErrorException {
        return webTarget.path("payment/status").request().put(null, Response.class);
    }

    public Response updateShipping(Object requestEntity, String id) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("shippings/{0}", new Object[]{id})).request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).put(jakarta.ws.rs.client.Entity.entity(requestEntity, jakarta.ws.rs.core.MediaType.APPLICATION_JSON), Response.class);
    }

    public Response removeFromCart(String cartId) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("cart/{0}", new Object[]{cartId})).request().delete(Response.class);
    }

    public <T> T getPendingOrders(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("orders/pending");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getAllFeedbacks(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("feedbacks");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public Response updateQuantity(String cartId) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("cart/update/{0}", new Object[]{cartId})).request().put(null, Response.class);
    }

    public Response addCODPayment() throws ClientErrorException {
        return webTarget.path("payment/cod").request().post(null, Response.class);
    }

    public <T> T getAllShippings(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("shippings");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getLatestShipping(Class<T> responseType, String userId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("shippings/latest/{0}", new Object[]{userId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getShippingByCity(Class<T> responseType, String cityId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("shippings/city/{0}", new Object[]{cityId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public Response addToCart() throws ClientErrorException {
        return webTarget.path("cart/add").request().post(null, Response.class);
    }

    public <T> T getShippingByPincode(Class<T> responseType, String pincode) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("shippings/pincode/{0}", new Object[]{pincode}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public Response markAsDelivered(String orderId) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("orders/deliver/{0}", new Object[]{orderId})).request().put(null, Response.class);
    }

    public <T> T getShippingById(Class<T> responseType, String id) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("shippings/{0}", new Object[]{id}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getCartById(Class<T> responseType, String cartId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("cart/item/{0}", new Object[]{cartId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void close() {
        client.close();
    }
    
}
