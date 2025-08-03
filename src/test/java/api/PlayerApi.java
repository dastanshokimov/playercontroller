package api;

import config.Config;
import constants.Endpoints;
import constants.Roles;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AllureAttachments;

import java.util.List;
import java.util.Map;
import static io.restassured.RestAssured.given;


public class PlayerApi {
    private static final Logger log = LoggerFactory.getLogger(PlayerApi.class);

    private static RequestSpecification withQueryParams(PlayerCreateRequestDto request) {
        return given()
                .baseUri(Config.getBaseUrl())
                .queryParam("age", request.getAge())
                .queryParam("gender", request.getGender())
                .queryParam("login", request.getLogin())
                .queryParam("password", request.getPassword())
                .queryParam("role", request.getRole())
                .queryParam("screenName", request.getScreenName());
    }

    public static Response createPlayerRaw(String editor, PlayerCreateRequestDto request) {
        AllureAttachments.attachRequestBody(request);

        Response response = withQueryParams(request)
                .when()
                .get(Endpoints.CREATE_PLAYER, editor);

        AllureAttachments.attachResponseBody(response);
        AllureAttachments.attachResponseStatus(response);

        log.info("[POST] /player/create/{} | Status: {}\nResponse: {}", editor, response.statusCode(), response.asPrettyString());
        return response;
    }

    public static PlayerCreateResponseDto createPlayer(String editor, PlayerCreateRequestDto request) {
        AllureAttachments.attachRequestBody(request);
        return createPlayerRaw(editor, request)
                .then()
                .statusCode(200)
                .extract()
                .as(PlayerCreateResponseDto.class);
    }

    public static Response deletePlayer(String editor, PlayerDeleteRequestDto requestDto) {
        AllureAttachments.attachRequestBody(requestDto);
        Response response =  given()
                .baseUri(Config.getBaseUrl())
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when()
                .delete(Endpoints.DELETE_PLAYER, editor);

        AllureAttachments.attachResponseBody(response);
        AllureAttachments.attachResponseStatus(response);

        log.info("[DELETE] /player/delete/{}/{} | Status: {}\nResponse: {}", editor, requestDto.getPlayerId(), response.statusCode(), response.asPrettyString());
        return response;
    }

    public static PlayerCreateResponseDto getPlayerById(long id) {
        PlayerGetRequestDto body = new PlayerGetRequestDto(id);
        return given()
                .baseUri(Config.getBaseUrl())
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(Endpoints.GET_PLAYER_BY_ID)
                .then()
                .statusCode(200)
                .extract()
                .as(PlayerCreateResponseDto.class);
    }

    public static Response getPlayerByIdRaw(Long id) {
        Map<String, Object> requestBody = Map.of("playerId", id);

        Response response =  given()
                .baseUri(Config.getBaseUrl())
                .contentType(ContentType.JSON)
                .body(id == null ? "{}" : requestBody)
                .when()
                .post(Endpoints.GET_PLAYER_BY_ID);

        AllureAttachments.attachResponseBody(response);
        AllureAttachments.attachResponseStatus(response);

        log.info("[POST] /player/get | Body: {} | Status: {}\nResponse: {}", requestBody, response.statusCode(), response.asPrettyString());
        return response;
    }

    public static List<PlayerItem> getAllPlayers() {
        Response response = getAllPlayersRaw();

        AllureAttachments.attachResponseBody(response);
        AllureAttachments.attachResponseStatus(response);

        log.info("[GET] /player/get/all | Status: {}\nResponse: {}", response.statusCode(), response.asPrettyString());
        return response.jsonPath().getList("players", PlayerItem.class);
    }

    public static Response getAllPlayersWithWrongMethod() {
        Response response =  given()
                .baseUri(Config.getBaseUrl())
                .when()
                .post(Endpoints.GET_ALL_PLAYERS);

        AllureAttachments.attachResponseBody(response);
        AllureAttachments.attachResponseStatus(response);

        log.info("[WRONG METHOD] POST /player/create/{} | Status: {}\nResponse: {}", response.statusCode(), response.asPrettyString());
        return response;
    }

    public static Response getAllPlayersRaw() {
        return given()
                .baseUri(Config.getBaseUrl())
                .when()
                .get(Endpoints.GET_ALL_PLAYERS);
    }

    public static Response updatePlayer(Roles editorRole, long playerId, PlayerUpdateRequestDto requestDto) {
        AllureAttachments.attachRequestBody(requestDto);
        Response response =  given()
                .baseUri(Config.getBaseUrl())
                .contentType(ContentType.JSON)
                .pathParam("editor", editorRole)
                .pathParam("id", playerId)
                .body(requestDto)
                .when()
                .patch(Endpoints.UPDATE_PLAYER);

        AllureAttachments.attachResponseBody(response);
        AllureAttachments.attachResponseStatus(response);

        log.info("[PATCH] /player/update/{}/{} | Status: {}\nBody: {}\nResponse: {}", editorRole, playerId, response.statusCode(), requestDto, response.asPrettyString());
        return response;
    }
}
