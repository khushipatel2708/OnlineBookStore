package REST;

import EJB.UserSessionBeanLocal;
import Entity.GroupMaster;
import Entity.User;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/user")
public class UserResource {

    @EJB
    private UserSessionBeanLocal userSessionBean;

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "API is working!";
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(Map<String, Object> data) {
        if (data == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Request body is missing or invalid"))
                    .build();
        }

        try {
            // create new user
            User user = new User();
            user.setFullname((String) data.get("fullname"));
            user.setPhone((String) data.get("phone"));
            user.setUsername((String) data.get("username"));
            user.setEmail((String) data.get("email"));
            user.setPassword((String) data.get("password"));
            user.setStatus((String) data.get("status"));

            // set group
            int groupId = ((Number) data.get("groupId")).intValue();
            GroupMaster group = userSessionBean.getGroupById(groupId);
            user.setGroupid(group);

            boolean result = userSessionBean.register(user);

            Map<String, Object> res = new HashMap<>();
            if (result) {
                res.put("success", true);
                res.put("message", "User registered and saved successfully!");
                return Response.ok(res).build();
            } else {
                res.put("success", false);
                res.put("message", "Failed to register user.");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
}
