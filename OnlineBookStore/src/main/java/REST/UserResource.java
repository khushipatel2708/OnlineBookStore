package REST;

import EJB.UserSessionBeanLocal;
import Entity.GroupMaster;
import Entity.User;
import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.json.JsonObject;
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
    } @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") int id) {
        User user = userSessionBean.getUserById(id);
        if (user != null)
            return Response.ok(user).build();
        else
            return Response.status(Response.Status.NOT_FOUND).entity("{\"message\":\"User not found\"}").build();
    }

    @PUT
    @Path("/update")
    public Response updateProfile(User user) {
        boolean result = userSessionBean.updateProfile(user);
        if (result)
            return Response.ok("{\"success\": true, \"message\": \"Profile updated successfully\"}").build();
        else
            return Response.serverError().entity("{\"success\": false, \"message\": \"Update failed\"}").build();
    }
    // POST: /api/user/changePassword
    @POST
    @Path("/changePassword")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePassword(ChangePasswordRequest req) {
        try {
            boolean success = userSessionBean.changePassword(req.getUsername(), req.getOldPassword(), req.getNewPassword());
            if (success) {
                return Response.ok("{\"success\": true, \"message\": \"Password updated successfully\"}").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"success\": false, \"message\": \"Invalid old password\"}").build();
            }
        } catch (Exception e) {
            return Response.serverError().entity("{\"success\": false, \"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    // DTO class
    public static class ChangePasswordRequest {
        private String username;
        private String oldPassword;
        private String newPassword;
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getOldPassword() { return oldPassword; }
        public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}
