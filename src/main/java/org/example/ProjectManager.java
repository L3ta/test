package org.example;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ProjectManager {
    private static final String URL = "http://localhost:80/jsonrpc.php";
    private static final String AUTHORIZATION_HEADER = "Basic YWRtaW46YWRtaW4=";

    public String createProject(String ownerId) {
        String projectName = "TEST";
        String projectId = null;

        String json = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"createProject\",\n" +
                "    \"id\": 2,\n" +
                "    \"params\": {\n" +
                "        \"name\": \"" + projectName + "\",\n" +
                "        \"owner_id\": \"" + ownerId + "\"\n" +
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
            projectId = resultElement.getAsString();
            System.out.println("Project created successfully: " + projectId);
        } else {
            System.out.println("Project creation failed.");
        }

        return projectId;
    }
}
