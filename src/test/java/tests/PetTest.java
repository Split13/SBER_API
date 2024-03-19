package tests;


import io.restassured.http.ContentType;
import io.restassured.response.Response;

import jdk.jfr.Description;
import org.junit.jupiter.api.Tag;
import org.testng.Assert;
import org.testng.annotations.Test;


import java.io.File;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
public class PetTest extends BaseTest{

    private final HashMap<String,String> dataMap = new HashMap<>();
    File jsonFile = new File("src/main/resources/__files.jsonFiles/createPet.Json");
    @Test(priority = 1)
    @Description("Create pet")
    @Tag("Smoke")
    public void createPet(){
        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonFile)
                .when()
                .post(constants.pet)
                .then()
                .log()
                .all()
                .extract()
                .response();

        dataMap.put("id", response.jsonPath().getString("id"));

        Assert.assertEquals(response.jsonPath().getString("name"), "SNOOPY");
        Assert.assertEquals(response.statusCode(), 200);
    }


    @Test(priority = 2)
    @Description("Update existing pet")
    @Tag("Smoke")
    public void updatePet(){
        Response response = given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"id\": " + dataMap.get("id") + ",\n" +
                        "  \"category\": {\n" +
                        "    \"id\": 0,\n" +
                        "    \"name\": \"string\"\n" +
                        "  },\n" +
                        "  \"name\": \"SNOOPY3\",\n" +
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
                .put(constants.pet)
                .then()
                .log()
                .all()
                .extract()
                .response();


        Assert.assertEquals(response.jsonPath().getString("name"), "SNOOPY3");
        Assert.assertEquals(response.statusCode(), 200);
    }


    @Test(priority = 3)
    @Description("Get pet info dy ID")
    @Tag("Smoke")
    public void getPetInfo(){
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get(constants.pet + dataMap.get("id"))
                .then()
                .log()
                .all()
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getString("name"), "SNOOPY3");
        Assert.assertEquals(response.statusCode(), 200);
    }


    @Test(priority = 4)
    @Description("Delete pet info dy ID")
    @Tag("Smoke")
    public void deletePet(){
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .delete(constants.pet + dataMap.get("id"))
                .then()
                .log()
                .all()
                .extract()
                .response();

        Assert.assertEquals(response.statusCode(), 200);
    }
}
