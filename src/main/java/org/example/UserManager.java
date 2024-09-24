package org.example;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.security.SecureRandom;
import java.util.Base64;

public class UserManager {
    private static final SecureRandom random = new SecureRandom();
    private static final String URL = "http://localhost:80/jsonrpc.php";
    private static final String AUTHORIZATION_HEADER = "Basic YWRtaW46YWRtaW4=";
    private String username;
    private String password;
    private String userId;

    private static String generateRandomString(int length) {
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getEncoder().withoutPadding().encodeToString(bytes).substring(0, length);
    }

    public String createUser() {
        username = generateRandomString(8);
        password = generateRandomString(12);

        System.out.println("Generated Username: " + username);
        System.out.println("Generated Password: " + password);

        String json = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"createUser\",\n" +
                "    \"id\": 2,\n" +
                "    \"params\": {\n" +
                "        \"username\": \"" + username + "\",\n" +
                "        \"password\": \"" + password + "\"\n" +
                "    }\n" +
                "}";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", AUTHORIZATION_HEADER)
                .body(json)
                .post(URL);

        System.out.println("Response Status: " + response.getStatusLine());
        JsonObject jsonResponse = JsonParser.parseString(response.getBody().asString()).getAsJsonObject();
        JsonElement resultElement = jsonResponse.get("result");

        if (resultElement != null) {
            userId = resultElement.getAsString();
            System.out.println("User created successfully: " + userId);
            return userId;
        } else {
            System.out.println("User creation failed.");
            return null;
        }
    }

    public void deleteUser() {
        if (userId == null) {
            System.out.println("No userId available to delete.");
            return;
        }

        String json = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"removeUser\",\n" +
                "    \"id\": 2,\n" +
                "    \"params\": {\n" +
                "        \"user_id\": \"" + userId + "\"\n" +
                "    }\n" +
                "}";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", AUTHORIZATION_HEADER)
                .body(json)
                .post(URL);

        System.out.println("Response Status: " + response.getStatusLine());
        JsonObject jsonResponse = JsonParser.parseString(response.getBody().asString()).getAsJsonObject();
        JsonElement resultElement = jsonResponse.get("result");

        if (resultElement != null && resultElement.getAsBoolean()) {
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("User deletion failed.");
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
    }
}
