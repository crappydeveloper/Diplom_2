package ru.yandex.praktikum.api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderClient {
    public static final String BASE_PATH_ORDERS = "/api/orders";

    @Step("Get response for create order with auth")
    public static Response getCreateOrderWithAuthResponse(String token, Object json) {
        return (Response) given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", token)
                .and()
                .body(json)
                .when()
                .post(BASE_PATH_ORDERS);
    }

    @Step("Get response for create order without auth")
    public static Response getCreateOrderWithoutAuthResponse(Object json) {
        return (Response) given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post(BASE_PATH_ORDERS);
    }

    @Step("Get ingredients with auth")
    public static Response getIngredientsWithAuthResponse(String token) {
        return (Response) given()
                .header("Authorization", token)
                .get(BASE_PATH_ORDERS);
    }

    @Step("Get ingredients without auth")
    public static Response getIngredientsWithoutAuthResponse() {
        return (Response) given()
                .get(BASE_PATH_ORDERS);
    }
}
