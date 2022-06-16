package ru.netology.registration;

import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import ru.netology.data.UserInfo;

import static io.restassured.RestAssured.given;

public class UserRegistration {
    private static RequestSpecification spec = new RequestSpecBuilder()
            .setBaseUri("http://localhost").setPort(9999).setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON).log(LogDetail.ALL).build();

    public static void registration(UserInfo userInfo) {
        Gson gson = new Gson();
        String user = gson.toJson(userInfo);
        given().spec(spec).body(user).when().post("/api/system/users").then().statusCode(200);
    }
}
