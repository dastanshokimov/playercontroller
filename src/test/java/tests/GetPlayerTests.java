package tests;

import api.PlayerApi;
import base.BaseTest;
import constants.Roles;
import io.qameta.allure.*;
import models.PlayerCreateRequestDto;
import models.PlayerCreateResponseDto;
import models.PlayerResponseDto;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static org.testng.Assert.*;

@Epic("Player Management")
@Feature("Get Player")
public class GetPlayerTests extends BaseTest {

    @Test(description = "Successfully fetch supervisor by ID = 1")
    @Story("Positive Case")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetSupervisorById() {
        var response = PlayerApi.getPlayerByIdRaw(1L);
        assertEquals(response.statusCode(), 200);

        var player = response.as(PlayerResponseDto.class);
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(player.getId(), 1L, "ID");
        soft.assertEquals(player.getRole(), Roles.SUPERVISOR.getValue(), "Role");
        soft.assertEquals(player.getLogin(), "supervisor", "Login");
        soft.assertNotNull(player.getAge(), "Age");
        soft.assertNotNull(player.getGender(), "Gender");
        soft.assertNotNull(player.getScreenName(), "ScreenName");
        soft.assertAll();
    }

    @Test(description = "Admin fetches their own data")
    @Story("Positive Case")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetAdminSelf() {
        var adminRequest = PlayerCreateRequestDto.randomPlayerWithRole(Roles.ADMIN);
        PlayerCreateResponseDto adminResponse = PlayerApi.createPlayer("supervisor", adminRequest);
        createdPlayerId.set(adminResponse.getId());

        var response = PlayerApi.getPlayerByIdRaw(adminResponse.getId());
        assertEquals(response.statusCode(), 200);
        var player = response.as(PlayerResponseDto.class);

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(player.getId(), adminResponse.getId());
        soft.assertEquals(player.getRole(), Roles.ADMIN.getValue());
        soft.assertEquals(player.getLogin(), adminRequest.getLogin());
        soft.assertNotNull(player.getScreenName());
        soft.assertAll();
    }

    @Test(description = "Get player with invalid ID", dataProvider = "invalidPlayerIds")
    @Story("Negative Case")
    @Severity(SeverityLevel.NORMAL)
    public void getPlayerWithInvalidIdTest(Long id, int expectedStatus, String caseName) {
        var response = PlayerApi.getPlayerByIdRaw(id);
        assertEquals(response.statusCode(), expectedStatus, "Failed case: " + caseName);
    }

    @DataProvider(name = "invalidPlayerIds")
    public Object[][] invalidPlayerIds() {
        return new Object[][]{
                {999999L, 404, "Non-existing ID"},
                {0L, 400, "Zero ID"}
        };
    }
}
