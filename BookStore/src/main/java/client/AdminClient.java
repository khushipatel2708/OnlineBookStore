package client;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

public class AdminClient {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "https://localhost:8181/BookStore/resources";

    public AdminClient() {
        client = jakarta.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("admin");
    }

    // ============================================================
    // ======================= USER CRUD ===========================
    // ============================================================

    public <T> T getAllUsers(Class<T> responseType) {
        return webTarget.path("users")
                .request(MediaType.APPLICATION_JSON)
                .get(responseType);
    }

    public <T> T getUserById(Class<T> responseType, Integer id) {
        return webTarget.path("users/" + id)
                .request(MediaType.APPLICATION_JSON)
                .get(responseType);
    }

    public <T> T findUserByUsername(Class<T> responseType, String username) {
        return webTarget.path("users/username/" + username)
                .request(MediaType.APPLICATION_JSON)
                .get(responseType);
    }

    public void addUser(Object requestEntity) {
        webTarget.path("users")
                .request(MediaType.APPLICATION_JSON)
                .post(jakarta.ws.rs.client.Entity.json(requestEntity));
    }

    public void updateUser(Integer id, Object requestEntity) {
        webTarget.path("users/" + id)
                .request(MediaType.APPLICATION_JSON)
                .put(jakarta.ws.rs.client.Entity.json(requestEntity));
    }

    public void deleteUser(Integer id) {
        webTarget.path("users/" + id)
                .request()
                .delete();
    }

    // ============================================================
    // ===================== BOOKTYPE CRUD =========================
    // ============================================================

    public <T> T getAllBooktypes(Class<T> responseType) {
        return webTarget.path("booktypes")
                .request(MediaType.APPLICATION_JSON)
                .get(responseType);
    }

    public <T> T getBooktypeById(Class<T> responseType, Integer id) {
        return webTarget.path("booktypes/" + id)
                .request(MediaType.APPLICATION_JSON)
                .get(responseType);
    }

    public void addBooktype(Object requestEntity) {
        webTarget.path("booktypes")
                .request(MediaType.APPLICATION_JSON)
                .post(jakarta.ws.rs.client.Entity.json(requestEntity));
    }

    public void updateBooktype(Integer id, Object requestEntity) {
        webTarget.path("booktypes/" + id)
                .request(MediaType.APPLICATION_JSON)
                .put(jakarta.ws.rs.client.Entity.json(requestEntity));
    }

    public void deleteBooktype(Integer id) {
        webTarget.path("booktypes/" + id)
                .request()
                .delete();
    }

    // ============================================================
    // ========================= BOOK CRUD =========================
    // ============================================================

    public <T> T getAllBooks(Class<T> responseType) {
        return webTarget.path("books")
                .request(MediaType.APPLICATION_JSON)
                .get(responseType);
    }

    public <T> T getBookById(Class<T> responseType, Integer id) {
        return webTarget.path("books/" + id)
                .request(MediaType.APPLICATION_JSON)
                .get(responseType);
    }

    public void addBook(Object requestEntity) {
        webTarget.path("books")
                .request(MediaType.APPLICATION_JSON)
                .post(jakarta.ws.rs.client.Entity.json(requestEntity));
    }

    public void updateBook(Integer id, Object requestEntity) {
        webTarget.path("books/" + id)
                .request(MediaType.APPLICATION_JSON)
                .put(jakarta.ws.rs.client.Entity.json(requestEntity));
    }

    public void deleteBook(Integer id) {
        webTarget.path("books/" + id)
                .request()
                .delete();
    }

    // ============================================================
    // ========================= CITY CRUD =========================
    // ============================================================

    public <T> T getAllCities(Class<T> responseType) {
        return webTarget.path("cities")
                .request(MediaType.APPLICATION_JSON)
                .get(responseType);
    }

    public void addCity(Object requestEntity) {
        webTarget.path("cities")
                .request(MediaType.APPLICATION_JSON)
                .post(jakarta.ws.rs.client.Entity.json(requestEntity));
    }

    public void updateCity(Integer id, Object requestEntity) {
        webTarget.path("cities/" + id)
                .request(MediaType.APPLICATION_JSON)
                .put(jakarta.ws.rs.client.Entity.json(requestEntity));
    }

    public void deleteCity(Integer id) {
        webTarget.path("cities/" + id)
                .request()
                .delete();
    }

    public <T> T getCityById(Class<T> responseType, Integer id) {
        return webTarget.path("cities/" + id)
                .request(MediaType.APPLICATION_JSON)
                .get(responseType);
    }

    public <T> T findCityByName(Class<T> responseType, String name) {
        return webTarget.path("cities/name/" + name)
                .request(MediaType.APPLICATION_JSON)
                .get(responseType);
    }

    // ============================================================
    // ======================== GROUP CRUD =========================
    // ============================================================

    public <T> T getAllGroups(Class<T> responseType) {
        return webTarget.path("groups")
                .request(MediaType.APPLICATION_JSON)
                .get(responseType);
    }

    public <T> T getGroupById(Class<T> responseType, Integer id) {
        return webTarget.path("groups/" + id)
                .request(MediaType.APPLICATION_JSON)
                .get(responseType);
    }

    public <T> T findGroupByName(Class<T> responseType, String name) {
        return webTarget.path("groups/name/" + name)
                .request(MediaType.APPLICATION_JSON)
                .get(responseType);
    }

    public void addGroup(Object requestEntity) {
        webTarget.path("groups")
                .request(MediaType.APPLICATION_JSON)
                .post(jakarta.ws.rs.client.Entity.json(requestEntity));
    }

    public void updateGroup(Integer id, Object requestEntity) {
        webTarget.path("groups/" + id)
                .request(MediaType.APPLICATION_JSON)
                .put(jakarta.ws.rs.client.Entity.json(requestEntity));
    }

    public void deleteGroup(Integer id) {
        webTarget.path("groups/" + id)
                .request()
                .delete();
    }

    // ============================================================

    public void close() {
        client.close();
    }
}
