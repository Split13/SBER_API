package tests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.Assert;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@RunWith(Parameterized.class)
public class ParameterizedTest {
    static List<Object> list = new ArrayList<>();

    @Description("Creates token, url, name and passes it to the board creation test")
    private static Stream<Arguments> stringProvider() {
        return Stream.of(
                arguments("https://petstore.swagger.io/v2/pet", "ParamTest DOGGIE1"),
                arguments("https://petstore.swagger.io/v2/pet", "ParamTest DOGGIE2")
        );

    }


    @org.junit.jupiter.params.ParameterizedTest
    @MethodSource("stringProvider")
    @Description("Create bord")
    void testWithMultiArgMethodSource(String url, String name) {

        Response response = given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"id\": 0,\n" +
                        "  \"category\": {\n" +
                        "    \"id\": 0,\n" +
                        "    \"name\": \"string\"\n" +
                        "  },\n" +
                        "  \"name\": \"" + name + "\",\n" +
                        "  \"photoUrls\": [\n" +
                        "    \"string\"\n" +
                        "  ],\n" +
                        "  \"tags\": [\n" +
                        "    {\n" +
                        "      \"id\": 0,\n" +
                        "      \"name\": \"string\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"status\": \"available\"\n" +
                        "}")
                .when()
                .post(url)
                .then()
                .log()
                .all()
                .extract()
                .response();

        list.add(response.jsonPath().getString("id"));

        Assert.assertEquals(response.jsonPath().getString("name"), name);
        Assert.assertEquals(response.statusCode(), 200);
        System.out.println("Номер ID " + list);

    }


    @Description("Passes id to the board removal test")
    private static Stream<Arguments> dataMapProvider() {
        return Stream.of(
                arguments("https://petstore.swagger.io/v2/pet/" + list.get(0)),
                arguments("https://petstore.swagger.io/v2/pet/" + list.get(1))
        );

    }


    @org.junit.jupiter.params.ParameterizedTest
    @MethodSource("dataMapProvider")
    @Description("Delete bord")
    void testWithMethodSource(String url) {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .delete(url)
                .then()
                .log()
                .all()
                .extract()
                .response();


        Assert.assertEquals(response.statusCode(), 200);
    }

}
