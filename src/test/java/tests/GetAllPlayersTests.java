package tests;

import api.PlayerApi;
import base.BaseTest;
import io.qameta.allure.*;
import models.PlayerItem;
import org.testng.annotations.Test;
import org.testng.asserts.*;
import static org.testng.Assert.*;

import java.util.List;

@Epic("Player Management")
@Feature("Get All Players")
public class GetAllPlayersTests extends BaseTest {

    @Test(description = "Successfully retrieve all players")
    @Story("Positive Case")
    @Severity(SeverityLevel.NORMAL)
    public void testGetAllPlayersSuccess() {
        List<PlayerItem> players = PlayerApi.getAllPlayers();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertNotNull(players, "Players list should not be null");
        softAssert.assertTrue(players.size() > 0, "Players list should not be empty");

        for (PlayerItem player : players) {
            softAssert.assertNotNull(player.getId(), "Player ID should not be null");
            softAssert.assertNotNull(player.getScreenName(), "Player screenName should not be null");
            softAssert.assertNotNull(player.getGender(), "Player gender should not be null");
            softAssert.assertTrue(player.getAge() > 0, "Player age should be positive");
        }

        softAssert.assertAll();
    }

    @Test(description = "Attempt to call GET /player/get/all with wrong HTTP method")
    @Story("Negative Case")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetAllPlayersWrongMethod() {
        var response = PlayerApi.getAllPlayersWithWrongMethod();
        assertEquals(response.statusCode(), 405, "Expected 405 Method Not Allowed");
    }
}
