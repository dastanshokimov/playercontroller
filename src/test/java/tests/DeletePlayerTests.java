package tests;

import api.PlayerApi;
import base.BaseTest;
import constants.Roles;
import io.qameta.allure.*;
import models.PlayerCreateRequestDto;
import models.PlayerCreateResponseDto;
import models.PlayerDeleteRequestDto;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Epic("Player Management")
@Feature("Delete Player")
public class DeletePlayerTests extends BaseTest {

    @Test(description = "Successfully delete a player with supervisor role")
    @Story("Positive Case")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeletePlayerSuccessfully() {
        PlayerCreateRequestDto request = PlayerCreateRequestDto.randomValidPlayer();
        PlayerCreateResponseDto response = PlayerApi.createPlayer(Roles.SUPERVISOR.getValue(), request);
        int playerId = Math.toIntExact(response.getId());

        var deleteResponse = PlayerApi.deletePlayer(Roles.SUPERVISOR.getValue(), new PlayerDeleteRequestDto(playerId));

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(deleteResponse.statusCode(), 204, "Expected status 204 when deleting a valid player");
        soft.assertAll();
    }

    @Test(description = "Attempt to delete a non-existing player")
    @Story("Negative Case")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteNonExistingPlayer() {
        var response = PlayerApi.deletePlayer(Roles.SUPERVISOR.getValue(), new PlayerDeleteRequestDto(-1));

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(response.statusCode(), 404, "Expected 404 for non-existent player ID");
        soft.assertAll();
    }

    @Test(description = "Deletion attempt by unauthorized editor")
    @Story("Negative Case")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteWithInvalidEditor() {
        PlayerCreateRequestDto request = PlayerCreateRequestDto.randomValidPlayer();
        PlayerCreateResponseDto response = PlayerApi.createPlayer(Roles.SUPERVISOR.getValue(), request);
        int playerId = Math.toIntExact(response.getId());
        createdPlayerId.set(response.getId());

        var deleteResponse = PlayerApi.deletePlayer(Roles.USER.getValue(), new PlayerDeleteRequestDto(playerId));

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(deleteResponse.statusCode(), 403, "Expected 403 when USER tries to delete");
        soft.assertAll();
    }

    @Test(description = "Attempt to delete system user 'supervisor'")
    @Story("Negative Case")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeleteSupervisorIsForbidden() {
        var deleteResponse = PlayerApi.deletePlayer(Roles.SUPERVISOR.getValue(), new PlayerDeleteRequestDto(1));

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(deleteResponse.statusCode(), 403, "Expected 403 when trying to delete supervisor");
        soft.assertAll();
    }
}
