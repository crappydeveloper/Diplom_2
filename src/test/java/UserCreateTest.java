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

public class UserCreateTest {
    private String email = "vupsen@yandex.ru";
    private String password = "1qaz2wsx";
    private String name = "gusenitsa";

    @Before
    public void setUp() {
        RestAssured.baseURI = Service.BASE_URI;
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUserTest() {
        Response createUserResponse = UserClient.getCreateUserResponse(new User(email, password, name));

        createUserResponse.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void createUserDuplicateTest() {
        UserClient.getCreateUserResponse(new User(email, password, name));
        Response createUserResponse = UserClient.getCreateUserResponse(new User(email, password, name));

        createUserResponse.then().assertThat().statusCode(403)
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без заполнения поля name")
    public void createUserNoRequiredFieldTest() {
        Response createUserResponse = UserClient.getCreateUserResponse(new User(email, password));

        createUserResponse.then().assertThat().statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void tearDown() {
        UserClient.deleteUserByLoginAndPassword(new User(email, password));
    }

}
