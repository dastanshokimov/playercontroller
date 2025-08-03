package tests;

import api.PlayerApi;
import base.BaseTest;
import com.github.javafaker.Faker;
import constants.Roles;
import models.PlayerCreateRequestDto;
import models.PlayerCreateResponseDto;
import models.PlayerDeleteRequestDto;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import static org.testng.Assert.*;
import io.qameta.allure.*;

@Epic("Player Management")
@Feature("Create Player")
public class CreatePlayerTests extends BaseTest {

    private final Faker faker = new Faker();

    @Test(description = "Creating a player with valid parameters")
    @Story("Positive Case")
    @Severity(SeverityLevel.CRITICAL)
    public void createPlayerPositiveTest() {
        PlayerCreateRequestDto request = PlayerCreateRequestDto.randomPlayerWithRole(Roles.USER);

        PlayerCreateResponseDto response = PlayerApi.createPlayer(Roles.SUPERVISOR.getValue(), request);
        createdPlayerId.set(response.getId());

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(response.getLogin(), request.getLogin(), "Login mismatch");
        soft.assertEquals(response.getGender(), request.getGender(), "Gender mismatch");
        soft.assertEquals(response.getAge(), request.getAge(), "Age mismatch");
        soft.assertEquals(response.getRole(), request.getRole(), "Role mismatch");
        soft.assertEquals(response.getScreenName(), request.getScreenName(), "Screen name mismatch");
        soft.assertNotNull(response.getId(), "ID должен быть сгенерирован");
        soft.assertAll();
    }

    @Test(description = "Should not allow duplicate login")
    @Story("Duplicate Login Check")
    @Severity(SeverityLevel.CRITICAL)
    public void duplicateLoginTest() {
        String duplicateLogin = faker.name().username();
        String uniqueScreenName1 = faker.name().fullName();
        String uniqueScreenName2 = faker.name().fullName();

        PlayerCreateRequestDto firstPlayer = PlayerCreateRequestDto.builder()
                .age("30")
                .gender("MALE")
                .login(duplicateLogin)
                .password("secret123")
                .role("USER")
                .screenName(uniqueScreenName1)
                .build();

        PlayerCreateResponseDto responseDto = PlayerApi.createPlayer("supervisor", firstPlayer);
        createdPlayerId.set(responseDto.getId());

        PlayerCreateRequestDto duplicatePlayer = PlayerCreateRequestDto.builder()
                .age("28")
                .gender("FEMALE")
                .login(duplicateLogin) // same login
                .password("secret456")
                .role("USER")
                .screenName(uniqueScreenName2) // different screenName
                .build();

        var response = PlayerApi.createPlayerRaw("supervisor", duplicatePlayer);
        assertTrue(response.statusCode() >= 400, "Expected 4xx error for duplicate login");
    }

    @Test(description = "Should not allow duplicate screenName")
    @Story("Duplicate ScreenName Check")
    @Severity(SeverityLevel.CRITICAL)
    public void duplicateScreenNameTest() {
        String duplicateScreenName = faker.name().fullName();
        String uniqueLogin1 = faker.name().username();
        String uniqueLogin2 = faker.name().username();

        PlayerCreateRequestDto firstPlayer = PlayerCreateRequestDto.builder()
                .age("30")
                .gender("MALE")
                .login(uniqueLogin1)
                .password("secret123")
                .role("USER")
                .screenName(duplicateScreenName)
                .build();

        PlayerCreateResponseDto responseDto = PlayerApi.createPlayer("supervisor", firstPlayer);
        createdPlayerId.set(responseDto.getId());

        PlayerCreateRequestDto duplicatePlayer = PlayerCreateRequestDto.builder()
                .age("28")
                .gender("FEMALE")
                .login(uniqueLogin2)
                .password("secret456")
                .role("USER")
                .screenName(duplicateScreenName) // same screenName
                .build();

        var response = PlayerApi.createPlayerRaw("supervisor", duplicatePlayer);
        assertTrue(response.statusCode() >= 400, "Expected 4xx error for duplicate screenName");
    }

    @Test(description = "Admin user should be able to create USER player")
    @Story("Role-based access restriction")
    @Severity(SeverityLevel.BLOCKER)
    public void createPlayerByAdminTest() {
        PlayerCreateRequestDto request = PlayerCreateRequestDto.randomPlayerWithRole(Roles.USER);
        var response = PlayerApi.createPlayer(Roles.ADMIN.getValue(), request);
        createdPlayerId.set(response.getId());

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(response.getLogin(), request.getLogin(), "Login mismatch");
        soft.assertEquals(response.getGender(), request.getGender(), "Gender mismatch");
        soft.assertEquals(response.getAge(), request.getAge(), "Age mismatch");
        soft.assertEquals(response.getRole(), request.getRole(), "Role mismatch");
        soft.assertEquals(response.getScreenName(), request.getScreenName(), "Screen name mismatch");
        soft.assertNotNull(response.getId(), "ID должен быть сгенерирован");
        soft.assertAll();
    }

    @Test(description = "User should not be able to create any player")
    @Story("Role-based access restriction")
    @Severity(SeverityLevel.BLOCKER)
    public void userCannotCreatePlayerTest() {
        PlayerCreateRequestDto request = PlayerCreateRequestDto.randomPlayerWithRole(Roles.USER);
        var response = PlayerApi.createPlayerRaw(Roles.USER.getValue(), request);
        assertEquals(response.statusCode(), 403, "User must not be allowed to create players");
    }

    @Test(description = "Negative test cases", dataProvider = "invalidPlayerData")
    @Story("Negative Cases")
    @Severity(SeverityLevel.NORMAL)
    public void createPlayerNegativeTest(String age, String gender, String login, String password, String role, String screenName, int expectedStatus, String caseName) {
        PlayerCreateRequestDto request = PlayerCreateRequestDto.builder()
                .age(age)
                .gender(gender)
                .login(login)
                .password(password)
                .role(role)
                .screenName(screenName)
                .build();

        var response = PlayerApi.createPlayerRaw("supervisor", request);
        if (response.statusCode() == 201 || response.statusCode() == 200) {
            Long id = response.jsonPath().getLong("id");
            if (id != null && id > 0) {
                PlayerApi.deletePlayer("supervisor", new PlayerDeleteRequestDto(id));
            }
        }
        assertEquals(response.statusCode(), expectedStatus, "Failed case: " + caseName);
    }

    @DataProvider(name = "invalidPlayerData")
    public Object[][] invalidPlayerData() {
        return new Object[][]{
                {"15", "MALE", "younguser", "secret123", "USER", "YoungUser", 400, "Age less than 17"},
                {"60", "FEMALE", "olduser", "secret123", "USER", "OldUser", 400, "Age 60 or older"},
                {"30", "MALE", "badroleuser", "secret123", "MODERATOR", "BadRole", 400, "Invalid role for created user"},
                {"26", "MALE", "shortpass", "abc1", "USER", "ShortPass", 400, "Password too short"},
                {"27", "MALE", "nodigitpass", "onlyletters", "USER", "NoDigit", 400, "Password without digits"},
                {"29", "OTHER", "weirdgender", "secret123", "USER", "StrangeOne", 400, "Invalid gender"},
                {"25", "MALE", "", "secret123", "USER", "NoLogin", 400, "Empty login"},
                {"25", "MALE", "noscreenuser", "secret123", "USER", "", 400, "Empty screenName"}
        };
    }
}
