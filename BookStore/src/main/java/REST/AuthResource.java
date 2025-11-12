package REST;

import EJB.AdminSessionBeanLocal;
import Entity.User;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jwtrest.TokenProvider;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @EJB
    private AdminSessionBeanLocal adminSessionBean;

    @Inject
    private TokenProvider tokenProvider;

    public static class LoginRequest {
        public String username;
        public String password;
    }

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        User user = adminSessionBean.findUserByUsername(request.username);

        if (user != null && user.getPassword().equals(request.password)) {
            // ✅ Get role from GroupMaster table
            String role = (user.getGroupid() != null)
                    ? user.getGroupid().getGroupname()
                    : "ROLE_USER";

            // ✅ Create token with username, password, and role
            String token = tokenProvider.createTokenWithClaims(
                    user.getUsername(),
                    user.getPassword(),
                    role,
                    false
            );

            // ✅ Return token + role + username in response
            String jsonResponse = String.format(
                    "{\"token\":\"%s\", \"username\":\"%s\", \"role\":\"%s\"}",
                    token, user.getUsername(), role
            );

            return Response.ok(jsonResponse)
                    .header("Authorization", "Bearer " + token)
                    .build();
        }

        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\":\"Invalid username or password\"}")
                .build();
    }
}
