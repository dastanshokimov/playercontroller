package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Attachment;
import io.restassured.response.Response;

public class AllureAttachments {

    @Attachment(value = "Response Body", type = "application/json")
    public static String attachResponseBody(Response response) {
        return response == null ? "null" : response.asPrettyString();
    }

    @Attachment(value = "Response Status", type = "text/plain")
    public static String attachResponseStatus(Response response) {
        return String.valueOf(response.getStatusCode());
    }

    @Attachment(value = "Request Body", type = "application/json")
    public static String attachRequestBody(Object request) {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(request);
        } catch (Exception e) {
            return "Failed to serialize request: " + e.getMessage();
        }
    }
}
