package tests;

import io.restassured.response.Response;

import jdk.jfr.Description;
import org.junit.jupiter.api.Tag;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.CreatePet;
import pojo.SucCrePet;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
public class PetTest extends BaseTest{
    private final HashMap<String,String> dataMap = new HashMap<>();

    @Test(priority = 1)
    @Description("Create pet")
    @Tag("Smoke")
    public void createPet(){
        Specifications.installSpecification(Specifications.requestSpec(),Specifications.responseSpecOK200());
        String name = "KITTY";
        CreatePet pet = new CreatePet();
        pet.setName("KITTY");
        SucCrePet sucCrePet = given()
                .body(pet)
                .when()
                .post(constants.pet)
                .then()
                .log()
                .all()
                .extract().as(SucCrePet.class);

        dataMap.put("id", sucCrePet.getId());
        Assert.assertEquals(name, sucCrePet.getName());
    }


    @Test(priority = 2)
    @Description("Update existing pet")
    @Tag("Smoke")
    public void updatePet(){
        Specifications.installSpecification(Specifications.requestSpec(),Specifications.responseSpecOK200());
        String name = "SNOOPDOGG";
        CreatePet pet = new CreatePet();
        pet.setId(dataMap.get("id"));
        pet.setName("SNOOPDOGG");
        SucCrePet sucCrePet = given()
                .body(pet)
                .when()
                .put(constants.pet)
                .then()
                .log()
                .all()
                .extract().as(SucCrePet.class);

        Assert.assertEquals(name, sucCrePet.getName());
    }


    @Test(priority = 3)
    @Description("Get pet info dy ID")
    @Tag("Smoke")
    public void getPetInfo(){
        Specifications.installSpecification(Specifications.requestSpec(),Specifications.responseSpecOK200());
        Response response = given()
                .when()
                .get(constants.pet + dataMap.get("id"))
                .then()
                .log()
                .all()
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getString("name"), "SNOOPDOGG");
    }


    @Test(priority = 4)
    @Description("Delete pet info dy ID")
    @Tag("Smoke")
    public void deletePet(){
        Specifications.installSpecification(Specifications.requestSpec(),Specifications.responseSpecOK200());
        Response response = given()
                .when()
                .delete(constants.pet + dataMap.get("id"))
                .then()
                .log()
                .all()
                .extract()
                .response();
    }
}
