package tests;


import constants.Constants;
import org.testng.annotations.BeforeTest;

import static io.restassured.RestAssured.baseURI;

public class BaseTest {

    Constants constants = new Constants();

    @BeforeTest
    public static void setup() {
        baseURI = "https://petstore.swagger.io/v2/";
    }
}
