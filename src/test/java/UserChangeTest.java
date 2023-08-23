import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import ru.yandex.praktikum.User;
import ru.yandex.praktikum.Service;
import ru.yandex.praktikum.api.client.UserClient;

public class UserChangeTest {
    private String token;
    private String email = "vupsen@yandex.ru";
    private String password = "1qaz2wsx";
    private String name = "gusenitsa";
    private String newEmail = "vupsen322@yandex.ru";
    private String newPassword = "1qaz2wsx3edc";
    private String newName= "gusenitsa2";

    @Before
    public void setUp() {
        RestAssured.baseURI = Service.BASE_URI;
        Response response = UserClient.getCreateUserResponse(new User(email, password, name));

        token = response.then().extract().body().path("accessToken");
    }

    @Test
    @DisplayName("Изменение пользователя после авторизации")
    public void changeUserAfterAuthTest() {
        Response createUserResponse = UserClient.getUserChangeWithAuthResponse(token, new User(newEmail, newPassword, newName));

        createUserResponse.then().assertThat().statusCode(200)
                .and()
                .body("user.email", equalTo(newEmail))
                .and()
                .body("user.name", equalTo(newName));
    }

    @Test
    @DisplayName("Изменение пользователя без предварительной авторизации")
    public void changeUserWithoutAuthTest() {
        Response createUserResponse = UserClient.getUserChangeWithoutAuthResponse(new User(newEmail, newPassword, newName));

        createUserResponse.then().assertThat().statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        UserClient.deleteUserByToken(token);
    }
}
