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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
   // --- ✅ LOGIN API ---
    @POST
    @Path("/login")
    public Response login(Map<String, String> data) {
        try {
            String username = data.get("username");
            String password = data.get("password");

            User user = userSessionBean.login(username, password);
            if (user != null) {
                Map<String, Object> res = new HashMap<>();
                res.put("success", true);
                res.put("message", "Login successful");
                res.put("userId", user.getId());
                res.put("fullname", user.getFullname());
                res.put("email", user.getEmail());
                res.put("group", user.getGroupid().getGroupname());
                return Response.ok(res).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(Map.of("success", false, "message", "Invalid username or password"))
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity(Map.of("success", false, "error", e.getMessage()))
                    .build();
        }
    }
    
     @GET
    @Path("/list")
    public Response getAllUsers() {
        try {
            List<User> users = userSessionBean.getAllUsers();

            // convert to JSON-safe map (avoid infinite recursion with entities)
            List<Map<String, Object>> userList = new ArrayList<>();
            for (User u : users) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", u.getId());
                map.put("fullname", u.getFullname());
                map.put("username", u.getUsername());
                map.put("email", u.getEmail());
                map.put("phone", u.getPhone());
                map.put("status", u.getStatus());
                map.put("group", (u.getGroupid() != null) ? u.getGroupid().getGroupname() : null);
                userList.add(map);
            }

            return Response.ok(Map.of("success", true, "users", userList)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity(Map.of("success", false, "error", e.getMessage()))
                    .build();
        }
    }
    // ✅ Get User By ID (for Profile Page)
    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") int id) {
        try {
            User user = userSessionBean.getUserById(id);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("success", false, "message", "User not found"))
                        .build();
            }

            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getId());
            map.put("fullname", user.getFullname());
            map.put("username", user.getUsername());
            map.put("email", user.getEmail());
            map.put("phone", user.getPhone());
            map.put("status", user.getStatus());
            map.put("groupId", (user.getGroupid() != null) ? user.getGroupid().getGroupid() : null);
            map.put("groupName", (user.getGroupid() != null) ? user.getGroupid().getGroupname() : null);

            return Response.ok(Map.of("success", true, "user", map)).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity(Map.of("success", false, "error", e.getMessage()))
                    .build();
        }
    }

    // ✅ Update User Profile
    @PUT
    @Path("/update/{id}")
    public Response updateUser(@PathParam("id") int id, Map<String, Object> data) {
        try {
            User user = userSessionBean.getUserById(id);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("success", false, "message", "User not found"))
                        .build();
            }

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

            if (updated) {
                return Response.ok(Map.of("success", true, "message", "User updated successfully")).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(Map.of("success", false, "message", "Failed to update user"))
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity(Map.of("success", false, "error", e.getMessage()))
                    .build();
        }
    }
    
    // ✅ 1. Change Password API
    @POST
    @Path("/changePassword")
    public Response changePassword(ChangePasswordRequest request) {
        boolean updated = userSessionBean.changePassword(
                request.getUsername(),
                request.getOldPassword(),
                request.getNewPassword()
        );

        if (updated) {
            return Response.ok("{\"message\":\"Password changed successfully\"}").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"message\":\"Invalid old password or user not found\"}")
                           .build();
        }
    }

    // ✅ 2. Reset Password (By Email)
    @POST
    @Path("/resetPassword")
    public Response resetPassword(ResetPasswordRequest request) {
        boolean reset = userSessionBean.resetPasswordByEmail(
                request.getEmail(),
                request.getNewPassword()
        );

        if (reset) {
            return Response.ok("{\"message\":\"Password reset successfully\"}").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"message\":\"Email not found\"}")
                           .build();
        }
    }

   

    // --- Inner static classes for request bodies ---
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
