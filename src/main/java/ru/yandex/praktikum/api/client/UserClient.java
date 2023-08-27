package ru.yandex.praktikum.api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class UserClient {
    public static final String BASE_PATH_USER_REGISTER = "/api/auth/register";
    public static final String BASE_PATH_USER_LOGIN = "/api/auth/login";
    public static final String BASE_PATH_USER_CHANGE = "/api/auth/user";
    public static final String BASE_PATH_USER_DELETE = "/api/auth/user";

    @Step("Get response for create user")
    public static Response getCreateUserResponse(Object json) {
        return (Response) given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post(BASE_PATH_USER_REGISTER);
    }

    @Step("Get response for login user")
    public static Response getUserLoginByEmailAndPasswordResponse(Object json) {
        return (Response) given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post(BASE_PATH_USER_LOGIN);
    }

    @Step("Get response for change user")
    public static Response getUserChangeWithAuthResponse(String token, Object json) {
        return (Response) given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", token)
                .and()
                .body(json)
                .when()
                .patch(BASE_PATH_USER_CHANGE);
    }

    @Step("Get response for change user")
    public static Response getUserChangeWithoutAuthResponse(Object json) {
        return (Response) given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .patch(BASE_PATH_USER_CHANGE);
    }

    @Step("Delete user using login and password")
    public static void deleteUserByLoginAndPassword(Object json) {
        String accessToken = getUserLoginByEmailAndPasswordResponse(json)
                .then().extract().body().path("accessToken");

        if (accessToken != null) {
            given()
                    .header("Authorization", accessToken)
                    .delete(BASE_PATH_USER_DELETE);
        }
    }

    @Step("Delete user using token")
    public static void deleteUserByToken(String token) {
            given()
                    .header("Authorization", token)
                    .delete(BASE_PATH_USER_DELETE);
    }
}