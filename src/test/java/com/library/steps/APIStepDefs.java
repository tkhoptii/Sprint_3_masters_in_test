package com.library.steps;

import com.library.pages.BookPage;
import com.library.pages.LoginPage;
import com.library.utility.DB_Util;
import com.library.utility.DatabaseHelper;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class APIStepDefs {
    RequestSpecification givenPart = given().log().all();
    Response response;
    JsonPath jp;
    ValidatableResponse thenPart;

    Map<String, Object> randomData = new HashMap<>();
    Map<String, String> expectedIdValue = new HashMap<>();

//    /**
//     * US01
//     */
//
//    RequestSpecification givenPart = RestAssured.given().log().uri();
//    Response response;
//    ValidatableResponse thenPart;
//    JsonPath jp;
//
//    String expectedID;
//
//    Map<String, Object> randomData=new HashMap<>();
//
//    @Given("I logged Library api as a {string}")
//    public void i_logged_library_api_as_a(String role) {
//        //OPT1
//        //givenPart.header("x-library-token", LibraryAPI_Util.getToken("librarian10@library","libraryUser"));
//
//        //OPT2
//        //givenPart.header("x-library-token", LibraryAPI_Util.getToken(ConfigurationReader.getProperty("librarian_username"),ConfigurationReader.getProperty("librarian_password")));
//
//        //OPT3
//        givenPart.header("x-library-token", LibraryAPI_Util.getToken(role));
//
//        givenPart.log().all();
//    }
//
//    @Given("Accept header is {string}")
//    public void accept_header_is(String acceptHeader) {
//        givenPart.accept(acceptHeader);
//
//    }
//
//    @When("I send GET request to {string} endpoint")
//    public void i_send_get_request_to_endpoint(String endpoint) {
//
//        //declare globally at class level:
//        response = givenPart.when().get(endpoint);
//
//        thenPart = response.then();
//
//        jp = response.jsonPath();
//
//    }
//
//    @Then("status code should be {int}")
//    public void status_code_should_be(int expectedStatusCode) {
//
//        // OPT - 1
//        Assert.assertEquals(expectedStatusCode, response.statusCode());
//
//        // OPT - 2
//        thenPart.statusCode(expectedStatusCode);
//
//
//    }
//
//    @Then("Response Content type is {string}")
//    public void response_content_type_is(String expectedContentType) {
//
//        // OPT - 1
//        Assert.assertEquals(expectedContentType, response.contentType());
//
//        // OPT - 2
//        thenPart.contentType(expectedContentType);
//    }
//
//    @Then("Each {string} field should not be null")
//    public void each_field_should_not_be_null(String path) {
//
//        // OPT - 1
//        thenPart.body(path, Matchers.everyItem(Matchers.notNullValue()));
//
//        // OPT - 2
//        List<String> allData = jp.getList(path);
//        for (String eachData : allData) {
//            Assert.assertNotNull(eachData);
//        }
//    }
//
//
//
//    /**
//     * US02
//     */
//    @Given("Path param {string} is {string}")
//    public void path_param_is(String pathParam, String value) {
//        givenPart.pathParam(pathParam, value);
//        expectedID = value; //declare globally
//    }
//
//    @Then("{string} field should be same with path param")
//    public void field_should_be_same_with_path_param(String path) {
//        String actualID = jp.getString(path);
//        Assert.assertEquals(expectedID, actualID);
//    }
//
//    @Then("following fields should not be null")
//    public void following_fields_should_not_be_null(List<String> allPaths) {
//
//        for (String eachPath : allPaths) {
//            thenPart.body(eachPath, Matchers.notNullValue());
//        }
//
//    }

    @Then("created user information should match with Database")
    public void created_user_information_should_match_with_database() {

        int id = jp.getInt("user_id");
        String query = DatabaseHelper.getUserByIdQuery(id);
        DB_Util.runQuery(query);
        Map<String, Object> actualUserDataDB = DB_Util.getRowMap(1);
        String password = (String) randomData.remove("password");
        Assert.assertEquals(randomData, actualUserDataDB);
        randomData.put("password", password);
    }

    @Then("created user should be able to login Library UI")
    public void created_user_should_be_able_to_login_library_ui() {

        LoginPage loginPage = new LoginPage();
        String email = (String) randomData.get("email");
        String password = (String) randomData.get("password");
        loginPage.login(email, password);

    }

    @Then("created user name should appear in Dashboard Page")
    public void created_user_name_should_appear_in_dashboard_page() {

        BookPage bookPage = new BookPage();
        String fullNameFromUI = bookPage.accountHolderName.getText();
        String fullNameAPI = (String) randomData.get("full_name");

        Assert.assertEquals(fullNameAPI, fullNameFromUI);


    }

}
