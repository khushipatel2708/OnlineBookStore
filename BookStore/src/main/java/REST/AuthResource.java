package REST;

import EJB.AdminSessionBeanLocal;
import Entity.User;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jwtrest.TokenProvider;
import java.util.Set;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @EJB
    private AdminSessionBeanLocal adminSessionBean;  // ✅ Use your existing EJB

    @Inject
    private TokenProvider tokenProvider;

    // ✅ Class for JSON input
    public static class LoginRequest {
        public String username;
        public String password;
    }

    //post :- http://localhost:8080/BookStore/resources/auth/login
    //{"username": "khushi","password": "123456"}
    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        // 1️⃣ Find user by username
        User user = adminSessionBean.findUserByUsername(request.username);

        // 2️⃣ Validate password
        if (user != null && user.getPassword().equals(request.password)) {
            // 3️⃣ Create JWT token with user role
            Set<String> roles = Set.of("ROLE_USER"); // or use user.getGroupid().getGroupname()
            String token = tokenProvider.createToken(request.username, roles, false);

            // 4️⃣ Return response
            return Response.ok("{\"token\": \"" + token + "\"}")
                    .header("Authorization", "Bearer " + token)
                    .build();
        }

        // 5️⃣ Invalid credentials
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\": \"Invalid username or password\"}")
                .build();
    }
}
