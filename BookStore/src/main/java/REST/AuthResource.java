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

    // --- DTO CLASS ---
    public static class LoginRequest {
        public String username;
        public String password;
    }

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {

        // Find user from DB
        User user = adminSessionBean.findUserByUsername(request.username);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"Invalid username or password\"}")
                    .build();
        }

        // 🔥 HASH input password using EJB method
        String hashedInput = adminSessionBean.hashPassword(request.password);

        // 🔥 Compare hashed values
        if (!user.getPassword().equals(hashedInput)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"Invalid username or password\"}")
                    .build();
        }

        // SUCCESS — generate JWT
        String role = (user.getGroupid() != null)
                ? user.getGroupid().getGroupname()
                : "ROLE_USER";

        String token = tokenProvider.createTokenWithClaims(
                user.getUsername(),
                "",      // ❌ DO NOT PUT PASSWORD IN TOKEN
                role,
                false
        );

        String jsonResponse = String.format(
                "{\"token\":\"%s\", \"username\":\"%s\", \"role\":\"%s\"}",
                token, user.getUsername(), role
        );

        return Response.ok(jsonResponse)
                .header("Authorization", "Bearer " + token)
                .build();
    }
}
