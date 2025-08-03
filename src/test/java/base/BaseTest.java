package base;

import api.PlayerApi;
import config.Config;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import models.PlayerDeleteRequestDto;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.util.Map;

public class BaseTest {
    @BeforeClass
    public void setup() {
        System.out.println("Running in Thread: " + Thread.currentThread().getName());
        RestAssured.baseURI = Config.getBaseUrl();
        RestAssured.filters(new AllureRestAssured());
    }


    protected static final ThreadLocal<Long> createdPlayerId = new ThreadLocal<>();

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        Long playerId = createdPlayerId.get();
        if (playerId != null) {
            PlayerApi.deletePlayer("supervisor", new PlayerDeleteRequestDto(playerId));
            System.out.println("TEAR DOWN: Deleted player " + playerId);
            createdPlayerId.remove(); // очистка ThreadLocal
        }
    }
}
