import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import ru.yandex.praktikum.Service;
import ru.yandex.praktikum.User;
import ru.yandex.praktikum.api.client.OrderClient;
import ru.yandex.praktikum.api.client.UserClient;

public class OrderGetTest {
    private String token;
    private String email = "vupsen@yandex.ru";
    private String password = "1qaz2wsx";
    private String name = "gusenitsa";

    @Before
    public void setUp() {
        RestAssured.baseURI = Service.BASE_URI;
        Response response = UserClient.getCreateUserResponse(new User(email, password, name));

        token = response.then().extract().body().path("accessToken");
    }

    @Test
    @DisplayName("Получение списка заказов пользователя после авторизации")
    public void getIngredientsWithAuthTest() {
        Response response = OrderClient.getIngredientsWithAuthResponse(token);

        response.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение списка заказов пользователя без предварительной авторизации")
    public void getIngredientsWithoutAuthTest() {
        Response response = OrderClient.getIngredientsWithoutAuthResponse();

        response.then().assertThat().statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        UserClient.deleteUserByToken(token);
    }
}
