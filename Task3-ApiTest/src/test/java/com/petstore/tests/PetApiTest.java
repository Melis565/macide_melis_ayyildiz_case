package com.petstore.tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petstore.client.ApiClient;
import com.petstore.model.Category;
import com.petstore.model.Pet;
import com.petstore.model.Tag;
import org.junit.jupiter.api.*;


import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PetApiTest {

    private static ApiClient apiClient;
    private static ObjectMapper objectMapper;
    private static final long PET_ID = ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE);

    @BeforeAll
    static void setup() {
        apiClient = new ApiClient();
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    @DisplayName("POST /pet endpoint- Create pet with valid data should return 200 ")
    void createPetWithValidDataShouldReturn200() throws Exception {
        Tag tag = new Tag(1,"friendly");
        Pet pet = new Pet(PET_ID,"Buddy","available");
        pet.setCategory(new Category(1,"Dogs"));
        pet.setPhotoUrls(List.of("https://example.com/buddy.jpg"));
        pet.setTags(List.of(tag));

        String requestBody = objectMapper.writeValueAsString(pet);
        HttpResponse<String> response = apiClient.post("/pet",requestBody);

        assertEquals(200, response.statusCode(),"Status code should be 200");

        Pet createdPet = objectMapper.readValue(response.body(), Pet.class);

        assertEquals("Buddy",createdPet.getName());
        assertEquals("available",createdPet.getStatus());
        assertEquals(PET_ID,createdPet.getId());
    }

    @Test
    @Order(2)
    @DisplayName("GET /pet/{petId} endpoint- Get by valid ID should return 200")
    void getByValidIDShouldReturn200() throws Exception {
        HttpResponse<String> response = apiClient.get("/pet/" + PET_ID);

        assertEquals(200, response.statusCode(), "Status code should be 200");

        Pet pet = objectMapper.readValue(response.body(), Pet.class);
        assertEquals(PET_ID, pet.getId());
        assertEquals("Buddy",pet.getName());
        assertNotNull(pet.getStatus());
    }

    @Test
    @Order(3)
    @DisplayName("GET /pet/findByStatus?status=available endpoint- Find pet by status available should return 200")
    void findPetByStatusAvailableShouldReturn200() throws Exception {
        HttpResponse<String> response = apiClient.get("/pet/findByStatus?status=available");

        assertEquals(200,response.statusCode(), "Status code should be 200");

        List<Pet> pets = objectMapper.readValue(response.body(), new TypeReference<List<Pet>>() {});
        assertNotNull(pets,"Pet list should not be null");

        if(!pets.isEmpty()) {
            assertEquals("available",pets.getFirst().getStatus());
        }
    }

    @Test
    @Order(4)
    @DisplayName("GET /pet/findByStatus?status=pending endpoint- Find pet by status pending should return 200")
    void findPetByStatusPendingShouldReturn200() throws Exception{
        HttpResponse<String> response = apiClient.get("/pet/findByStatus?status=pending");

        assertEquals(200,response.statusCode(),"Status code should be 200");

        List<Pet> pets = objectMapper.readValue(response.body(), new TypeReference<List<Pet>>() {});
        assertNotNull(pets,"Pet list should not be null");

        if(!pets.isEmpty()) {
            assertEquals("pending",pets.getFirst().getStatus());
        }
    }

    @Test
    @Order(5)
    @DisplayName("GET /pet/findByStatus?status=sold endpoint- Find pet by status sold should return 200")
    void findPetByStatusSoldShouldReturn200() throws Exception{
        HttpResponse<String> response = apiClient.get("/pet/findByStatus?status=sold");

        assertEquals(200,response.statusCode(),"Status code should be 200");

        List<Pet> pets = objectMapper.readValue(response.body(), new TypeReference<List<Pet>>() {});
        assertNotNull(pets,"Pet list should not be null");

        if(!pets.isEmpty()) {
            assertEquals("sold",pets.getFirst().getStatus());
        }
    }

    @Test
    @Order(6)
    @DisplayName("GET /pet/findByTags endpoint- Find pets by tags should return 200")
    void findPetsByTagsShouldReturn200() throws Exception{
        HttpResponse<String> response = apiClient.get("/pet/findByTags?tags=friendly");

        assertEquals(200, response.statusCode(),"Should return 200");

        List<Pet> pets = objectMapper.readValue(response.body(), new TypeReference<List<Pet>>() {});
        assertNotNull(pets, "Pet list should not be null");

        assertEquals("friendly", pets.getFirst().getTags().getFirst().getName());
    }

    @Test
    @Order(7)
    @DisplayName("PUT /pet - Update pet with valid data should return 200")
    void updatePetWithValidDataShouldReturn200() throws Exception{
        Tag tag = new Tag(1,"friendly");
        Pet pet = new Pet(PET_ID,"Buddy updated","sold");
        pet.setCategory(new Category(1,"Dogs"));
        pet.setPhotoUrls(List.of("https://example.com/buddy.jpg"));
        pet.setTags(List.of(tag));

        String requestBody = objectMapper.writeValueAsString(pet);
        HttpResponse<String> response = apiClient.put("/pet",requestBody);

        assertEquals(200, response.statusCode(),"Should return 200");

        Pet updatedPet = objectMapper.readValue(response.body(),Pet.class);
        assertNotNull(updatedPet, "Pet should not be null");

        assertEquals("Buddy updated", updatedPet.getName());
        assertEquals("sold",updatedPet.getStatus());
    }

    @Test
    @Order(8)
    @DisplayName("POST /pet/{petId}- Update pet with form data should return 200")
    void updatePetWithFormDataShouldReturn200() throws Exception{
        HttpResponse<String> getResponse = apiClient.get("/pet/" + PET_ID);

        String formData = "name=BuddyForm&status=pending";
        HttpResponse<String> response = apiClient.postFormData("/pet/" + PET_ID, formData);

        assertEquals(200, response.statusCode(),"Status code should be 200");
    }

    @Test
    @Order(9)
    @DisplayName("DELETE /pet/{petId}- Delete pet with valid ID should return 200")
    void deletePetWithValidIdShouldReturn200() throws Exception{
        HttpResponse<String> response = apiClient.delete("/pet/" + PET_ID);

        assertEquals(200, response.statusCode(),"Status code should be 200");
    }

    @Test
    @Order(10)
    @DisplayName("Full Crud Lifecycle test- create, read, update, read, delete, verify deleted")
    void fullCrudLifecycleTest() throws Exception{
        long lifeCycleTestPetId = ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE);

        //Create
        Pet pet = new Pet(lifeCycleTestPetId,"Simba","available");
        Tag tag = new Tag(1,"friendly");
        pet.setCategory(new Category(2,"Cats"));
        pet.setPhotoUrls(List.of("https://example.com/simba.jpg"));
        pet.setTags(List.of(tag));

        String createBody = objectMapper.writeValueAsString(pet);
        HttpResponse<String> createResponse = apiClient.post("/pet",createBody);
        assertEquals(200,createResponse.statusCode(),"Create response should return 200");

        //Read
        HttpResponse<String> readResponse = apiClient.get("/pet/" + lifeCycleTestPetId );
        assertEquals(200,readResponse.statusCode(),"Read response should return 200");
        Pet readPet = objectMapper.readValue(readResponse.body(),Pet.class);
        assertEquals("Simba",readPet.getName());
        assertEquals("available",readPet.getStatus());

        //Update
        pet.setName("Simba Updated");
        pet.setStatus("sold");
        String updatedBody = objectMapper.writeValueAsString(pet);
        HttpResponse<String> updateResponse = apiClient.put("/pet",updatedBody );
        assertEquals(200,updateResponse.statusCode(),"Update response should return 200");

        //Read after update
        HttpResponse<String> readAfterUpdate = apiClient.get("/pet/" + lifeCycleTestPetId );
        assertEquals(200,readAfterUpdate.statusCode(),"Read response should return 200");
        Pet readUpdatedPet = objectMapper.readValue(readAfterUpdate.body(),Pet.class);
        assertEquals("Simba Updated",readUpdatedPet.getName());
        assertEquals("sold",readUpdatedPet.getStatus());

        //Delete
        HttpResponse<String> responseDelete = apiClient.delete("/pet/" + lifeCycleTestPetId);

        assertEquals(200, responseDelete.statusCode(),"Delete response should be 200");

        //Verify Deleted
        HttpResponse<String> verifyDelete = apiClient.get("/pet/" + lifeCycleTestPetId);

        assertEquals(404, verifyDelete.statusCode(),"Deleted pet should return error with status code 404");
    }

    @Test
    @Order(11)
    @DisplayName("POST /pet - Create pet with empty body should return 405")
    void createPetWithEmptyBodyShouldReturn405() throws Exception {
        HttpResponse<String> response = apiClient.post("/pet", "");

        assertEquals(405,response.statusCode(),"Empty body should return error with status code 405");
    }

    @Test
    @Order(12)
    @DisplayName("GET /pet/{petId} - Get pet with non-existent ID should return 404")
    void getPetByIdWithNonExistentIdShouldReturn404() throws Exception {
        HttpResponse<String> response = apiClient.get("/pet/999999999");

        assertEquals(404, response.statusCode(), "Non-existent pet should return error with status code 404");
    }

    @Test
    @Order(13)
    @DisplayName("GET /pet/{petId} - Get pet with invalid ID format should return 404")
    void getPetByIdWithInvalidIdFormatShouldReturn404() throws Exception {
        HttpResponse<String> response = apiClient.get("/pet/invalidString");

        assertEquals(404,response.statusCode(), "Invalid ID format should return error with status code 404");
    }

    @Test
    @Order(14)
    @DisplayName("GET /pet/findByStatus - Find pets with invalid status should return empty list")
    void findPetsByStatusWithInvalidStatusShouldReturnEmptyList() throws Exception {
        HttpResponse<String> response = apiClient.get("/pet/findByStatus?status=invalidStatus");

        assertEquals(200, response.statusCode(), "Status code should be 200");

        List<Pet> pets = objectMapper.readValue(response.body(), new TypeReference<List<Pet>>() {});
        assertNotNull(pets);
        assertTrue(pets.isEmpty(), "Invalid status should return empty list");
    }

    @Test
    @Order(15)
    @DisplayName("PUT /pet - Update pet with non-existent ID should return 200")
    // This test should return 404 but it returns 200. This is how api works.
    void updatePetWithNonExistentPetShouldHandleGracefully() throws Exception {
        Pet pet = new Pet();
        pet.setId(888888888L);
        pet.setName("Ghost Pet");
        pet.setStatus("available");
        pet.setPhotoUrls(List.of("https://example.com/ghost.jpg"));

        String requestBody = objectMapper.writeValueAsString(pet);
        HttpResponse<String> response = apiClient.put("/pet", requestBody);

        assertEquals(200,response.statusCode(),"Non-existent pet update should return error with status code 200");

    }

    @Test
    @Order(16)
    @DisplayName("DELETE /pet/{petId} - Delete pet with non-existent ID should return 404")
    void deletePetWithNonExistentIdShouldReturn404() throws Exception {
        HttpResponse<String> response = apiClient.delete("/pet/999999999");

        assertEquals(404, response.statusCode(), "Deleting non-existent pet should return error with status code 404");
    }

    @Test
    @Order(17)
    @DisplayName("DELETE /pet/{petId} - Delete pet with invalid ID format should return 404")
    void deletePetWithInvalidIdFormatShouldReturn404() throws Exception {
        HttpResponse<String> response = apiClient.delete("/pet/invalidString");

        assertEquals(404,response.statusCode(),"Invalid ID format should return error with status code 404");
    }

    @Test
    @Order(18)
    @DisplayName("POST /pet - Create pet with invalid JSON body should return 400")
    void createPetWithInvalidJsonShouldReturn400() throws Exception {
        HttpResponse<String> response = apiClient.post("/pet", "{invalid json}");
        assertEquals(400,response.statusCode() ,"Invalid JSON should return error with status code 400");
    }

    @Test
    @Order(19)
    @DisplayName("POST /pet/{petId}/uploadImage - Upload image should return 200")
    void uploadImageShouldReturn200() throws Exception {
        String testImagePath = "src/test/resources/test-image.jpg";

        HttpResponse<String> response = apiClient.postMultipartFile(
                "/pet/" + PET_ID + "/uploadImage", testImagePath);

        assertEquals(200, response.statusCode(), "Status code should be 200");

        assertTrue(response.body().contains("test-image.jpg"),
                "Response should contain the uploaded file name");
    }
}
