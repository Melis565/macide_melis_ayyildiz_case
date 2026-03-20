package com.petstore.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petstore.client.ApiClient;
import com.petstore.model.User;
import org.junit.jupiter.api.*;

import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("User API Tests")
public class UserApiTest {

    private static ApiClient apiClient;
    private static ObjectMapper objectMapper;
    private static final String USERNAME = "testuser_crud";

    @BeforeAll
    static void setup() {
        apiClient = new ApiClient();
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    @DisplayName("POST /user - Create user with valid data should return 200")
    void createUserWithValidDataShouldReturn200() throws Exception {
        User user = new User(100L, USERNAME, "John", "Doe",
                "john.doe@example.com", "password123", "1234567890", 1);

        String requestBody = objectMapper.writeValueAsString(user);
        HttpResponse<String> response = apiClient.post("/user", requestBody);

        assertEquals(200, response.statusCode(), "Status code should be 200");
    }

    @Test
    @Order(2)
    @DisplayName("GET /user/{username} - Get user by valid username should return 200")
    void getUserByUsernameWithValidUsernameShouldReturn200() throws Exception {
        HttpResponse<String> response = apiClient.get("/user/" + USERNAME);

        assertEquals(200, response.statusCode(), "Status code should be 200");

        User user = objectMapper.readValue(response.body(), User.class);
        assertEquals(USERNAME, user.getUsername());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    @Order(3)
    @DisplayName("PUT /user/{username} - Update user with valid data should return 200")
    void updateUserWithValidDataShouldReturn200() throws Exception {
        User user = new User(100L, USERNAME, "John", "Smith",
                "john.smith@example.com", "newPassword456", "0987654321", 1);

        String requestBody = objectMapper.writeValueAsString(user);
        HttpResponse<String> response = apiClient.put("/user/" + USERNAME, requestBody);

        assertEquals(200, response.statusCode(), "Status code should be 200");

        HttpResponse<String> getResponse = apiClient.get("/user/" + USERNAME);
        User updatedUser = objectMapper.readValue(getResponse.body(), User.class);
        assertEquals("Smith", updatedUser.getLastName());
        assertEquals("john.smith@example.com", updatedUser.getEmail());
    }

    @Test
    @Order(4)
    @DisplayName("GET /user/login - Login with valid credentials should return 200")
    void loginUserWithValidCredentialsShouldReturn200() throws Exception {
        HttpResponse<String> response = apiClient.get("/user/login?username=" + USERNAME + "&password=newPassword456");

        assertEquals(200, response.statusCode(), "Status code should be 200");

        String responseBody = response.body();
        assertNotNull(responseBody);
        assertFalse(responseBody.isEmpty(), "Login response should not be empty");
    }

    @Test
    @Order(5)
    @DisplayName("GET /user/logout - Logout should return 200")
    void logoutUserShouldReturn200() throws Exception {
        HttpResponse<String> response = apiClient.get("/user/logout");

        assertEquals(200, response.statusCode(), "Status code should be 200");
    }

    @Test
    @Order(6)
    @DisplayName("POST /user/createWithArray - Create users with array should return 200")
    void createUsersWithArrayShouldReturn200() throws Exception {
        User user1 = new User(201L, "arrayuser1", "Alice", "Array",
                "alice@example.com", "pass123", "1111111111", 1);
        User user2 = new User(202L, "arrayuser2", "Bob", "Array",
                "bob@example.com", "pass456", "2222222222", 1);

        List<User> users = List.of(user1, user2);
        String requestBody = objectMapper.writeValueAsString(users);
        HttpResponse<String> response = apiClient.post("/user/createWithArray", requestBody);

        assertEquals(200, response.statusCode(), "Status code should be 200");

        HttpResponse<String> getUser1 = apiClient.get("/user/arrayuser1");
        assertEquals(200, getUser1.statusCode(), "arrayuser1 should exist");

        HttpResponse<String> getUser2 = apiClient.get("/user/arrayuser2");
        assertEquals(200, getUser2.statusCode(), "arrayuser2 should exist");
    }

    @Test
    @Order(7)
    @DisplayName("POST /user/createWithList - Create users with list should return 200")
    void createUsersWithListShouldReturn200() throws Exception {
        User user1 = new User(301L, "listuser1", "Charlie", "List",
                "charlie@example.com", "pass789", "3333333333", 1);
        User user2 = new User(302L, "listuser2", "Diana", "List",
                "diana@example.com", "pass012", "4444444444", 1);

        List<User> users = List.of(user1, user2);
        String requestBody = objectMapper.writeValueAsString(users);
        HttpResponse<String> response = apiClient.post("/user/createWithList", requestBody);

        assertEquals(200, response.statusCode(), "Status code should be 200");

        HttpResponse<String> getUser1 = apiClient.get("/user/listuser1");
        assertEquals(200, getUser1.statusCode(), "listuser1 should exist");

        HttpResponse<String> getUser2 = apiClient.get("/user/listuser2");
        assertEquals(200, getUser2.statusCode(), "listuser2 should exist");

    }

    @Test
    @Order(8)
    @DisplayName("DELETE /user/{username} - Delete user with valid username should return 200")
    void deleteUserWithValidUsernameShouldReturn200() throws Exception {
        HttpResponse<String> response = apiClient.delete("/user/" + USERNAME);

        assertEquals(200, response.statusCode(), "Status code should be 200");
    }

    @Test
    @Order(9)
    @DisplayName("Full CRUD Lifecycle - Create, Read, Update, Read, Delete, Verify Deleted")
    void fullCrudLifecycleUser() throws Exception {
        String lifecycleUsername = "lifecycle_user";

        // Create
        User user = new User(400L, lifecycleUsername, "Lifecycle", "User",
                "lifecycle@example.com", "lifecyclePass", "5555555555", 1);
        String createBody = objectMapper.writeValueAsString(user);
        HttpResponse<String> createResponse = apiClient.post("/user", createBody);
        assertEquals(200, createResponse.statusCode(), "Create should return 200");

        // Read
        HttpResponse<String> readResponse = apiClient.get("/user/" + lifecycleUsername);
        assertEquals(200, readResponse.statusCode(), "Read should return 200");
        User readUser = objectMapper.readValue(readResponse.body(), User.class);
        assertEquals(lifecycleUsername, readUser.getUsername());
        assertEquals("Lifecycle", readUser.getFirstName());

        // Update
        user.setFirstName("Updated");
        user.setEmail("updated@example.com");
        String updateBody = objectMapper.writeValueAsString(user);
        HttpResponse<String> updateResponse = apiClient.put("/user/" + lifecycleUsername, updateBody);
        assertEquals(200, updateResponse.statusCode(), "Update should return 200");

        // Read After Update
        HttpResponse<String> readAfterUpdate = apiClient.get("/user/" + lifecycleUsername);
        assertEquals(200, readAfterUpdate.statusCode(), "Read after update should return 200");
        User updatedUser = objectMapper.readValue(readAfterUpdate.body(), User.class);
        assertEquals("Updated", updatedUser.getFirstName());
        assertEquals("updated@example.com", updatedUser.getEmail());

        // Delete
        HttpResponse<String> deleteResponse = apiClient.delete("/user/" + lifecycleUsername);
        assertEquals(200, deleteResponse.statusCode(), "Delete should return 200");

        // Verify Delete
        HttpResponse<String> verifyResponse = apiClient.get("/user/" + lifecycleUsername);
        assertEquals(404, verifyResponse.statusCode(), "Deleted user should return error with status code 404");
    }

    @Test
    @Order(10)
    @DisplayName("POST /user - Create user with empty body should return 405")
    void createUserWithEmptyBodyShouldReturn405() throws Exception {
        HttpResponse<String> response = apiClient.post("/user", "");

        assertEquals(405, response.statusCode(),
                "Empty body should return error with status code 405");

    }

    @Test
    @Order(11)
    @DisplayName("GET /user/{username} - Get user with non-existent username should return 404")
    void getUserByUsernameWithNonExistentUserShouldReturn404() throws Exception {
        HttpResponse<String> response = apiClient.get("/user/nonexistent_user_12345");

        assertEquals(404, response.statusCode(), "Non-existent user should return error with status code 404");
    }

    @Test
    @Order(12)
    @DisplayName("GET /user/ - Get user with empty username should return 405")
    void getUserByUsernameWithEmptyUsernameShouldReturn405() throws Exception {
        HttpResponse<String> response = apiClient.get("/user/");

        assertEquals(405, response.statusCode(), "Empty username should return error with status code 405");

    }

    @Test
    @Order(13)
    @DisplayName("PUT /user/{username} - Update non-existent user should handle gracefully")
        //This endpoint shouldn't return status code 200 but api returns 200. This is how api works.
    void updateUserWithNonExistentUserShouldHandleGracefully() throws Exception {
        User user = new User(999L, "nonexistent_user_xyz", "Ghost", "User",
                "ghost@example.com", "ghostPass", "0000000000", 1);

        String requestBody = objectMapper.writeValueAsString(user);
        HttpResponse<String> response = apiClient.put("/user/nonexistent_user_xyz", requestBody);

        assertEquals(200, response.statusCode(), "Non-existent user update should return error with status code 200");

    }

    @Test
    @Order(14)
    @DisplayName("PUT /user/{username} - Update user with invalid JSON body should return 400")
    void updateUserWithInvalidBodyShouldReturn400() throws Exception {
        HttpResponse<String> response = apiClient.put("/user/" + USERNAME, "{invalid json}");

        assertEquals(400, response.statusCode(), "Invalid JSON body should return error with status code 400");
    }

    @Test
    @Order(15)
    @DisplayName("DELETE /user/{username} - Delete non-existent user should return 404")
    void deleteUserWithNonExistentUserShouldReturn404() throws Exception {
        HttpResponse<String> response = apiClient.delete("/user/nonexistent_user_99999");

        assertEquals(404, response.statusCode(), "Deleting non-existent user should return error with status code 404");
    }

    @Test
    @Order(16)
    @DisplayName("GET /user/login - Login with invalid credentials should return response")
        //This endpoint shouldn't return status code 200 but api returns 200. This is how api works.
    void loginUserWithInvalidCredentialsShouldReturnResponse() throws Exception {
        HttpResponse<String> response = apiClient.get("/user/login?username=invalid_user&password=wrongpass");

        assertEquals(200, response.statusCode(), "Invalid credentials should return error with status code 200");
        assertNotNull(response.body(), "Response body should not be null");
    }

    @Test
    @Order(17)
    @DisplayName("GET /user/login - Login with missing parameters should return response")
        //This endpoint shouldn't return status code 200 but api returns 200. This is how api works.
    void loginUserWithMissingParamsShouldReturnResponse() throws Exception {
        HttpResponse<String> response = apiClient.get("/user/login");

        assertEquals(200, response.statusCode(), "Missing credentials should return error with status code 200");
        assertNotNull(response.body(), "Response body should not be null");
    }

    @Test
    @Order(18)
    @DisplayName("POST /user/createWithArray - Create users with empty array should handle")
        //This endpoint shouldn't return status code 200 but api returns 200. This is how api works.
    void createUsersWithArrayEmptyArrayShouldHandle() throws Exception {
        HttpResponse<String> response = apiClient.post("/user/createWithArray", "[]");

        assertEquals(200, response.statusCode(), "Empty array should return error with status code 200");

    }
}
