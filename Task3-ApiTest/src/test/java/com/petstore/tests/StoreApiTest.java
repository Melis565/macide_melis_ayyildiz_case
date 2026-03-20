package com.petstore.tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petstore.client.ApiClient;
import org.junit.jupiter.api.*;

import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Store API Tests")
public class StoreApiTest {

    private static ApiClient apiClient;
    private static ObjectMapper objectMapper;
    private static final long ORDER_ID = ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE);

    @BeforeAll
    static void setup() {
        apiClient = new ApiClient();
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    @DisplayName("POST /store/order - Place order with valid data should return 200")
    void placeOrderWithValidDataShouldReturn200() throws Exception {
        com.petstore.model.Order order = new com.petstore.model.Order();
        order.setId(ORDER_ID);
        order.setPetId(1L);
        order.setQuantity(2);
        order.setShipDate(Instant.now().toString());
        order.setStatus("placed");
        order.setComplete(false);

        String requestBody = objectMapper.writeValueAsString(order);
        HttpResponse<String> response = apiClient.post("/store/order", requestBody);

        assertEquals(200, response.statusCode(), "Status code should be 200");

        com.petstore.model.Order createdOrder = objectMapper.readValue(response.body(), com.petstore.model.Order.class);
        assertEquals(ORDER_ID, createdOrder.getId());
        assertEquals(1L, createdOrder.getPetId());
        assertEquals(2, createdOrder.getQuantity());
        assertEquals("placed", createdOrder.getStatus());
    }

    @Test
    @Order(2)
    @DisplayName("GET /store/order/{orderId} - Get order by valid ID should return 200")
    void getOrderByIdWithValidIdShouldReturn200() throws Exception {
        HttpResponse<String> response = apiClient.get("/store/order/" + ORDER_ID);

        assertEquals(200, response.statusCode(), "Status code should be 200");

        com.petstore.model.Order order = objectMapper.readValue(response.body(), com.petstore.model.Order.class);
        assertEquals(ORDER_ID, order.getId());
        assertNotNull(order.getStatus());
    }

    @Test
    @Order(3)
    @DisplayName("GET /store/inventory - Get inventory should return 200")
    void getInventoryShouldReturn200() throws Exception {
        HttpResponse<String> response = apiClient.get("/store/inventory");

        assertEquals(200, response.statusCode(), "Status code should be 200");

        Map<String, Integer> inventory = objectMapper.readValue(response.body(), new TypeReference<Map<String, Integer>>() {});
        assertNotNull(inventory);

    }

    @Test
    @Order(4)
    @DisplayName("DELETE /store/order/{orderId} - Delete order with valid ID should return 200")
    void deleteOrderWithValidIdShouldReturn200() throws Exception {
        HttpResponse<String> response = apiClient.delete("/store/order/" + ORDER_ID);

        assertEquals(200, response.statusCode(), "Status code should be 200");
    }

    @Test
    @Order(5)
    @DisplayName("Full CRUD Lifecycle - Place, Read, Delete, Verify Deleted")
    void fullCrudLifecycleOrder() throws Exception {
        long lifecycleOrderId = ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE);

        // Create
        com.petstore.model.Order order = new com.petstore.model.Order();
        order.setId(lifecycleOrderId);
        order.setPetId(2L);
        order.setQuantity(1);
        order.setShipDate(Instant.now().toString());
        order.setStatus("placed");
        order.setComplete(false);

        String createBody = objectMapper.writeValueAsString(order);
        HttpResponse<String> createResponse = apiClient.post("/store/order", createBody);
        assertEquals(200, createResponse.statusCode(), "Create order should return 200");

        // Read
        HttpResponse<String> readResponse = apiClient.get("/store/order/" + lifecycleOrderId);
        assertEquals(200, readResponse.statusCode(), "Read order should return 200");
        com.petstore.model.Order readOrder = objectMapper.readValue(readResponse.body(), com.petstore.model.Order.class);
        assertEquals(lifecycleOrderId, readOrder.getId());
        assertEquals("placed", readOrder.getStatus());

        // Delete
        HttpResponse<String> deleteResponse = apiClient.delete("/store/order/" + lifecycleOrderId);
        assertEquals(200, deleteResponse.statusCode(), "Delete order should return 200");

        // Verify Delete
        HttpResponse<String> verifyResponse = apiClient.get("/store/order/" + lifecycleOrderId);
        assertEquals(404, verifyResponse.statusCode(), "Deleted order should return 404");
    }

    @Test
    @Order(6)
    @DisplayName("GET /store/order/{orderId} - Get order with non-existent ID should return 404")
    void getOrderByIdWithNonExistentIdShouldReturn404() throws Exception {
        HttpResponse<String> response = apiClient.get("/store/order/99999");

        assertEquals(404, response.statusCode(), "Non-existent order should return error with status code 404");
    }

    @Test
    @Order(7)
    @DisplayName("GET /store/order/{orderId} - Get order with invalid ID format should return 404")
    void getOrderByIdWithInvalidIdFormatShouldReturn404() throws Exception {
        HttpResponse<String> response = apiClient.get("/store/order/invalidString");

        assertEquals(404,response.statusCode(),"Invalid ID format should return error with status code 404");
    }

    @Test
    @Order(8)
    @DisplayName("GET /store/order/{orderId} - Get order with negative ID should return 404")
    void getOrderByIdWithNegativeIdShouldReturn404() throws Exception {
        HttpResponse<String> response = apiClient.get("/store/order/-1");

        assertEquals(404,response.statusCode(),"Negative ID should return error with status code 404");
    }

    @Test
    @Order(9)
    @DisplayName("DELETE /store/order/{orderId} - Delete order with non-existent ID should return 404")
    void deleteOrderWithNonExistentIdShouldReturn404() throws Exception {
        HttpResponse<String> response = apiClient.delete("/store/order/99999");

        assertEquals(404, response.statusCode(), "Deleting non-existent order should return error with status code 404");
    }

    @Test
    @Order(10)
    @DisplayName("DELETE /store/order/{orderId} - Delete order with invalid ID format should return 404")
    void deleteOrderWithInvalidIdFormatShouldReturn404() throws Exception {
        HttpResponse<String> response = apiClient.delete("/store/order/invalidString");

        assertEquals(404, response.statusCode(), "Invalid ID format should return error with status code 404");
    }

    @Test
    @Order(11)
    @DisplayName("POST /store/order - Place order with invalid JSON should return 400")
    void placeOrderWithInvalidDataShouldReturn400() throws Exception {
        HttpResponse<String> response = apiClient.post("/store/order", "{invalid json}");

        assertEquals(400, response.statusCode(), "Invalid JSON should return error with status code 400");
    }

}
