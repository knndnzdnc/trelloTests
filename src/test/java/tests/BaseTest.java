package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONObject;

import java.io.IOException;

public class BaseTest {

    private final String apiKey = "apiKey";
    private final String tokenKey = "tokenKey";
    private final String baseURL = "https://api.trello.com";
    private String boardId = "board id";
    private String boardLongId = "board long id";
    private String newListName = "New Liste 2024";
    private String newCardName = "New Card 2024";
    private String cardId = "card id";
    private final String memberInviteEmail = "mailadress@gmail.com";

    /* Create a New Board */
    public void createABoard() {
        HttpResponse<String> response = null;
        try {
            response = Unirest.post(baseURL + "/1/boards/")
                    .queryString("name", "February 229")
                    .queryString("key", apiKey)
                    .queryString("token", tokenKey)
                    .asString();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
        JSONObject object = new JSONObject(response.getBody());
        String url = object.getString("url");
        boardId = url.substring(url.lastIndexOf("/b/") + 3, url.lastIndexOf("/"));

        //return boardId;
        System.out.println(object.getString("id"));
    }

    public String getBoardId() {
        return boardId;
    }

    /* Get a Board */
    public void getABoard() {
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.get(baseURL + "/1/boards/" + boardId)
                    .header("Accept", "application/json")
                    .queryString("key", apiKey)
                    .queryString("token", tokenKey)
                    .asJson();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }

        System.out.println(response.getBody());
    }

    public void updateABoard() {
        HttpResponse<String> response = null;
        try {
            response = Unirest.put(baseURL + "/1/boards/" + boardId)
                    .queryString("name", "February 16")
                    .queryString("key", apiKey)
                    .queryString("token", tokenKey)
                    .asString();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }

        System.out.println(response.getBody());

    }

    public void deleteABoard() {
        HttpResponse<String> response = null;
        try {
            response = Unirest.delete(baseURL + "/1/boards/" + boardId)
                    .queryString("key", apiKey)
                    .queryString("token", tokenKey)
                    .asString();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }

        System.out.println(response.getStatus());
    }

    public void inviteMembertoBoardviaemail() {
        // The payload definition using the Jackson library
        JsonNodeFactory jnf = JsonNodeFactory.instance;
        ObjectNode payload = jnf.objectNode();
        {
            payload.put("fullName", "<string>");
        }

// Connect Jackson ObjectMapper to Unirest
        Unirest.setObjectMapper(new ObjectMapper() {
            private final com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });

// This code sample uses the  'Unirest' library:
// http://unirest.io/java.html
        HttpResponse<String> response = null;
        try {
            response = Unirest.put(baseURL + "/1/boards/" + boardId + "/members")
                    .header("Content-Type", "application/json")
                    .queryString("email", memberInviteEmail)
                    .queryString("key", apiKey)
                    .queryString("token", tokenKey)
                    .body(payload)
                    .asString();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }

        System.out.println(response.getBody());
    }

    /* ------------------------ List ---------------- */

    public void createaNewList() {
        HttpResponse<String> response = null;
        try {
            response = Unirest.post(baseURL + "/1/lists")
                    .queryString("name", newListName)
                    .queryString("idBoard", boardLongId)
                    .queryString("key", apiKey)
                    .queryString("token", tokenKey)
                    .asString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(response.getBody());
    }

    /* ------------------------ Card ---------------- */
    public void createANewCard() {
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.post(baseURL + "/1/cards")
                    .header("Accept", "application/json")
                    .queryString("name", newCardName)
                    .queryString("idList", cardId)
                    .queryString("key", apiKey)
                    .queryString("token", tokenKey)
                    .asJson();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response.getBody());
    }
}
