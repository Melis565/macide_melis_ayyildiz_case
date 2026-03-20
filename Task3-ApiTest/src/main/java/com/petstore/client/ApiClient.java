package com.petstore.client;

import com.petstore.config.ApiConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ApiClient {

    private final HttpClient httpClient;

    public ApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public HttpResponse<String> get(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiConfig.BASE_URL + path))
                .header("Accept", ApiConfig.ACCEPT_JSON)
                .GET()
                .timeout(Duration.ofSeconds(30))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> post(String path, String jsonBody) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiConfig.BASE_URL + path))
                .header("Content-Type", ApiConfig.CONTENT_TYPE_JSON)
                .header("Accept", ApiConfig.ACCEPT_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .timeout(Duration.ofSeconds(30))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> put(String path, String jsonBody) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiConfig.BASE_URL + path))
                .header("Content-Type", ApiConfig.CONTENT_TYPE_JSON)
                .header("Accept", ApiConfig.ACCEPT_JSON)
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .timeout(Duration.ofSeconds(30))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> delete(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiConfig.BASE_URL + path))
                .header("Accept", ApiConfig.ACCEPT_JSON)
                .DELETE()
                .timeout(Duration.ofSeconds(30))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> postFormData(String path, String formData) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiConfig.BASE_URL + path))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", ApiConfig.ACCEPT_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .timeout(Duration.ofSeconds(30))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> postMultipartFile(String path, String filePath) throws Exception {
        String boundary = "----FormBoundary" + System.currentTimeMillis();
        Path file = Path.of(filePath);
        String fileName = file.getFileName().toString();
        byte[] fileBytes = Files.readAllBytes(file);

        byte[] body = buildMultipartBody(boundary, fileName, fileBytes);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiConfig.BASE_URL + path))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .header("Accept", ApiConfig.ACCEPT_JSON)
                .POST(HttpRequest.BodyPublishers.ofByteArray(body))
                .timeout(Duration.ofSeconds(30))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private byte[] buildMultipartBody(String boundary, String fileName, byte[] fileBytes) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String CRLF = "\r\n";

        outputStream.write(("--" + boundary + CRLF).getBytes());
        outputStream.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"" + CRLF).getBytes());
        outputStream.write(("Content-Type: image/jpeg" + CRLF).getBytes());
        outputStream.write(CRLF.getBytes());
        outputStream.write(fileBytes);
        outputStream.write(CRLF.getBytes());

        outputStream.write(("--" + boundary + "--" + CRLF).getBytes());

        return outputStream.toByteArray();
    }
}
