package REST;

import EJB.UserSessionBeanLocal;
import Entity.GroupMaster;
import Entity.Shipping;
import Entity.User;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.*;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @EJB
    private UserSessionBeanLocal userSessionBean;

    // ------------------------- USER APIs -------------------------

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "API is working!";
    }

    // ✅ Register user
    @POST
    @Path("/register")
    public Response register(Map<String, Object> data) {
        if (data == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Request body is missing or invalid"))
                    .build();
        }

        try {
            User user = new User();
            user.setFullname((String) data.get("fullname"));
            user.setPhone((String) data.get("phone"));
            user.setUsername((String) data.get("username"));
            user.setEmail((String) data.get("email"));
            user.setPassword((String) data.get("password"));
            user.setStatus((String) data.get("status"));

            int groupId = ((Number) data.get("groupId")).intValue();
            GroupMaster group = userSessionBean.getGroupById(groupId);
            user.setGroupid(group);

            boolean result = userSessionBean.register(user);

            if (result)
                return Response.ok(Map.of("success", true, "message", "User registered successfully")).build();
            else
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(Map.of("success", false, "message", "Failed to register user")).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(Map.of("success", false, "error", e.getMessage())).build();
        }
    }

    // ✅ Login
    @POST
    @Path("/login")
    public Response login(Map<String, String> data) {
        try {
            String username = data.get("username");
            String password = data.get("password");

            User user = userSessionBean.login(username, password);
            if (user != null) {
                return Response.ok(Map.of(
                        "success", true,
                        "message", "Login successful",
                        "userId", user.getId(),
                        "fullname", user.getFullname(),
                        "email", user.getEmail(),
                        "group", user.getGroupid().getGroupname()
                )).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(Map.of("success", false, "message", "Invalid username or password"))
                        .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(Map.of("success", false, "error", e.getMessage())).build();
        }
    }

    // ✅ Get all users
    @GET
    @Path("/list")
    public Response getAllUsers() {
        try {
            List<User> users = userSessionBean.getAllUsers();
            List<Map<String, Object>> userList = new ArrayList<>();

            for (User u : users) {
                userList.add(Map.of(
                        "id", u.getId(),
                        "fullname", u.getFullname(),
                        "username", u.getUsername(),
                        "email", u.getEmail(),
                        "phone", u.getPhone(),
                        "status", u.getStatus(),
                        "group", (u.getGroupid() != null) ? u.getGroupid().getGroupname() : null
                ));
            }

            return Response.ok(Map.of("success", true, "users", userList)).build();
        } catch (Exception e) {
            return Response.serverError().entity(Map.of("success", false, "error", e.getMessage())).build();
        }
    }

    // ✅ Get user by ID
    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") int id) {
        try {
            User user = userSessionBean.getUserById(id);
            if (user == null)
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("success", false, "message", "User not found"))
                        .build();

            Map<String, Object> map = Map.of(
                    "id", user.getId(),
                    "fullname", user.getFullname(),
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "phone", user.getPhone(),
                    "status", user.getStatus(),
                    "groupId", (user.getGroupid() != null) ? user.getGroupid().getGroupid() : null,
                    "groupName", (user.getGroupid() != null) ? user.getGroupid().getGroupname() : null
            );

            return Response.ok(Map.of("success", true, "user", map)).build();

        } catch (Exception e) {
            return Response.serverError().entity(Map.of("success", false, "error", e.getMessage())).build();
        }
    }

    // ✅ Update user
    @PUT
    @Path("/update/{id}")
    public Response updateUser(@PathParam("id") int id, Map<String, Object> data) {
        try {
            User user = userSessionBean.getUserById(id);
            if (user == null)
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("success", false, "message", "User not found"))
                        .build();

            if (data.containsKey("fullname")) user.setFullname((String) data.get("fullname"));
            if (data.containsKey("phone")) user.setPhone((String) data.get("phone"));
            if (data.containsKey("email")) user.setEmail((String) data.get("email"));
            if (data.containsKey("password")) user.setPassword((String) data.get("password"));
            if (data.containsKey("status")) user.setStatus((String) data.get("status"));

            if (data.containsKey("groupId")) {
                int groupId = ((Number) data.get("groupId")).intValue();
                GroupMaster group = userSessionBean.getGroupById(groupId);
                user.setGroupid(group);
            }

            boolean updated = userSessionBean.updateProfile(user);

            if (updated)
                return Response.ok(Map.of("success", true, "message", "User updated successfully")).build();
            else
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(Map.of("success", false, "message", "Failed to update user")).build();

        } catch (Exception e) {
            return Response.serverError().entity(Map.of("success", false, "error", e.getMessage())).build();
        }
    }

    // ------------------------- SHIPPING APIs -------------------------

    // ✅ Add Shipping
    @POST
    @Path("/shipping/add")
    public Response addShipping(Shipping s) {
        try {
            userSessionBean.addShipping(s);
            return Response.ok(Map.of("success", true, "message", "Shipping added successfully")).build();
        } catch (Exception e) {
            return Response.serverError().entity(Map.of("success", false, "error", e.getMessage())).build();
        }
    }

    // ✅ Update Shipping
    @PUT
    @Path("/shipping/update")
    public Response updateShipping(Shipping s) {
        try {
            userSessionBean.updateShipping(s);
            return Response.ok(Map.of("success", true, "message", "Shipping updated successfully")).build();
        } catch (Exception e) {
            return Response.serverError().entity(Map.of("success", false, "error", e.getMessage())).build();
        }
    }

    // ✅ Delete Shipping
    @DELETE
    @Path("/shipping/delete/{id}")
    public Response deleteShipping(@PathParam("id") int id) {
        try {
            userSessionBean.deleteShipping(id);
            return Response.ok(Map.of("success", true, "message", "Shipping deleted successfully")).build();
        } catch (Exception e) {
            return Response.serverError().entity(Map.of("success", false, "error", e.getMessage())).build();
        }
    }

    // ✅ Get Shipping by ID
    @GET
    @Path("/shipping/{id}")
    public Response getShippingById(@PathParam("id") int id) {
        try {
            Shipping s = userSessionBean.getShippingById(id);
            if (s == null)
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("success", false, "message", "Shipping not found")).build();
            return Response.ok(s).build();
        } catch (Exception e) {
            return Response.serverError().entity(Map.of("success", false, "error", e.getMessage())).build();
        }
    }

    // ✅ Get Shipping by User ID
    @GET
    @Path("/shipping/user/{userid}")
    public Response getShippingByUser(@PathParam("userid") int userid) {
        try {
            List<Shipping> list = userSessionBean.getShippingByUser(userid);
            return Response.ok(list).build();
        } catch (Exception e) {
            return Response.serverError().entity(Map.of("success", false, "error", e.getMessage())).build();
        }
    }

    // ------------------------- PASSWORD APIs -------------------------

    @POST
    @Path("/changePassword")
    public Response changePassword(ChangePasswordRequest request) {
        boolean updated = userSessionBean.changePassword(
                request.getUsername(), request.getOldPassword(), request.getNewPassword());

        if (updated)
            return Response.ok(Map.of("message", "Password changed successfully")).build();
        else
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message", "Invalid old password or user not found"))
                    .build();
    }

    @POST
    @Path("/resetPassword")
    public Response resetPassword(ResetPasswordRequest request) {
        boolean reset = userSessionBean.resetPasswordByEmail(
                request.getEmail(), request.getNewPassword());

        if (reset)
            return Response.ok(Map.of("message", "Password reset successfully")).build();
        else
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message", "Email not found"))
                    .build();
    }

    // Inner classes for request objects
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

    public static class ResetPasswordRequest {
        private String email;
        private String newPassword;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}
