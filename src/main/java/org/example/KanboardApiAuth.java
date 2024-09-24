package org.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.Base64;

public class KanboardApiAuth {
    private static final String URL = "http://localhost:80/jsonrpc.php";
    private final String authHeader;

    public KanboardApiAuth(String username, String password) {
        String credentials = username + ":" + password;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        this.authHeader = "Basic " + encodedCredentials;
    }

    public void deleteTask(String taskId) {
        String json = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"removeTask\",\n" +
                "    \"id\": 2,\n" +
                "    \"params\": {\n" +
                "        \"task_id\": \"" + taskId + "\"\n" +
                "    }\n" +
                "}";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", authHeader)
                .body(json)
                .post(URL);

        System.out.println("Response Status: " + response.getStatusLine());
        JsonObject jsonResponse = JsonParser.parseString(response.getBody().asString()).getAsJsonObject();
        if (jsonResponse.get("result").getAsBoolean()) {
            System.out.println("Task deleted successfully.");
        } else {
            System.out.println("Task deletion failed.");
        }
    }

    public void deleteProject(String projectId) {
        String json = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"removeProject\",\n" +
                "    \"id\": 2,\n" +
                "    \"params\": {\n" +
                "        \"project_id\": \"" + projectId + "\"\n" +
                "    }\n" +
                "}";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", authHeader)
                .body(json)
                .post(URL);

        System.out.println("Response Status: " + response.getStatusLine());
        JsonObject jsonResponse = JsonParser.parseString(response.getBody().asString()).getAsJsonObject();
        if (jsonResponse.get("result").getAsBoolean()) {
            System.out.println("Project deleted successfully.");
        } else {
            System.out.println("Project deletion failed.");
        }
    }
}
