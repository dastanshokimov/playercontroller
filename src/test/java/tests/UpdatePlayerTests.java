package tests;

import api.PlayerApi;
import base.BaseTest;
import constants.Roles;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import models.PlayerCreateRequestDto;
import models.PlayerUpdateRequestDto;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static constants.Roles.*;

public class UpdatePlayerTests extends BaseTest {

    @DataProvider(name = "validEditorAndTarget")
    public Object[][] validEditorAndTarget() {
        return new Object[][]{
                {SUPERVISOR, SUPERVISOR},
                {SUPERVISOR, USER},
                {SUPERVISOR, ADMIN},
                {ADMIN, SUPERVISOR},
                {ADMIN, ADMIN},
                {ADMIN, USER},
                {USER, SUPERVISOR},
                {USER, ADMIN},
                {USER, USER},
        };
    }

    @Test(description = "Positive cases for update based on role model", dataProvider = "validEditorAndTarget")
    @Story("Positive Cases")
    @Severity(SeverityLevel.CRITICAL)
    public void testUpdatePlayerRoleModel(Roles editorRole, Roles targetRole) {
        SoftAssert softAssert = new SoftAssert();
        var request = PlayerCreateRequestDto.randomPlayerWithRole(targetRole);
        var response = PlayerApi.createPlayer(SUPERVISOR.getValue(), request);
        Long playerId = response.getId();
        createdPlayerId.set(playerId);

        PlayerUpdateRequestDto updateRequest = new PlayerUpdateRequestDto();
        updateRequest.setScreenName("UpdatedScreen");
        updateRequest.setLogin("UpdatedLogin");

        var updateResponse = PlayerApi.updatePlayer(editorRole, playerId, updateRequest);

        switch (editorRole) {
            case SUPERVISOR:
                if (targetRole.equals(SUPERVISOR)) {
                    softAssert.assertEquals(updateResponse.statusCode(), 403, "Expected 403 Forbidden");
                } else softAssert.assertEquals(updateResponse.statusCode(), 200, "Expected 200 OK");
                break;
            case ADMIN:
                if (targetRole.equals(SUPERVISOR)) {
                    softAssert.assertEquals(updateResponse.statusCode(), 403, "Expected 403 Forbidden");
                } else if (targetRole.equals(USER)) {
                    softAssert.assertEquals(updateResponse.statusCode(), 200, "Expected 200 OK");
                }
                break;
            case USER:
                softAssert.assertEquals(updateResponse.statusCode(), 403, "Expected 403 Forbidden");
                break;
        }


        var updated = PlayerApi.getPlayerById(playerId);
        softAssert.assertEquals(updated.getScreenName(), "UpdatedScreen");
        softAssert.assertEquals(updated.getLogin(), "UpdatedLogin");

        softAssert.assertAll();
    }

    @DataProvider(name = "invalidUpdateData")
    public Object[][] invalidUpdateData() {
        return new Object[][]{
                {"", "UpdatedLogin", 400, "Empty screen name"},
                {"UpdatedScreen", "", 400, "Empty login"},
                {null, null, 400, "Null values"},
        };
    }

    @Test(description = "Negative cases: invalid update data", dataProvider = "invalidUpdateData")
    @Story("Negative Cases")
    @Severity(SeverityLevel.NORMAL)
    public void testUpdatePlayerInvalidFields(String screenName, String login, int expectedStatus, String caseName) {
        SoftAssert softAssert = new SoftAssert();

        var request = PlayerCreateRequestDto.randomValidPlayer();
        var response = PlayerApi.createPlayer("supervisor", request);
        Long playerId = response.getId();
        createdPlayerId.set(playerId);

        PlayerUpdateRequestDto update = new PlayerUpdateRequestDto();
        update.setScreenName(screenName);
        update.setLogin(login);

        var patchResponse = PlayerApi.updatePlayer(SUPERVISOR, playerId, update);

        softAssert.assertEquals(patchResponse.statusCode(), expectedStatus, caseName);
        softAssert.assertAll();
    }

    @Test(description = "Negative case: user attempts to update another user")
    @Story("Negative Cases")
    @Severity(SeverityLevel.NORMAL)
    public void testUserCannotUpdateAnotherUser() {
        SoftAssert softAssert = new SoftAssert();

        var request = PlayerCreateRequestDto.randomValidPlayer();
        var response = PlayerApi.createPlayer("supervisor", request);
        Long playerId = response.getId();
        createdPlayerId.set(playerId);

        PlayerUpdateRequestDto update = new PlayerUpdateRequestDto();
        update.setLogin("BlockedLogin");

        var patchResponse = PlayerApi.updatePlayer(USER, playerId, update);

        softAssert.assertEquals(patchResponse.statusCode(), 403, "User should not be allowed to update another user");
        softAssert.assertAll();
    }
}
