package com.library.steps;

import com.library.pages.BookPage;
import com.library.pages.LoginPage;
import com.library.utility.BrowserUtil;
import com.library.utility.ConfigurationReader;
import com.library.utility.DB_Util;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.internal.common.assertion.Assertion;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;


import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class US04_AN_StepDefs {

    RequestSpecification givenPart;
    Response response;
    ValidatableResponse thenPart;
    int idAPI;
    Map<String, Object> randomUserMapInfo;
    String expectedFullNameAPI;
    String expectedEmailAPI;
    String expectedPasswordAPI;
    Integer expectedUserGroupAPI;


    @Given("I logged Library api as a {string} AN")
    public void i_logged_library_api_as_a_an(String userType) {

        givenPart = given().log().uri()
                .header("x-library-token", LibraryAPI_Util.getToken(userType));


    }
    @Given("Accept header is {string} AN")
    public void accept_header_is_an(String acceptType) {
         givenPart.accept(acceptType);

    }
    @Given("Request Content Type header is {string} AN")
    public void request_content_type_header_is_an(String contentType) {

         givenPart.contentType(contentType);


    }
    @Given("I create a random {string} as request body AN")
    public void i_create_a_random_as_request_body_an(String userType) {

        randomUserMapInfo = LibraryAPI_Util.getRandomUserMap();
        System.out.println("randomUserMapInfo = " + randomUserMapInfo);

        if(userType.equals("user")){
            givenPart.formParams(randomUserMapInfo);
            expectedFullNameAPI = (String) randomUserMapInfo.get("full_name");
            expectedEmailAPI = (String) randomUserMapInfo.get("email");
            expectedPasswordAPI = (String) randomUserMapInfo.get("password");
            System.out.println("expectedPasswordAPI = " + expectedPasswordAPI);
            expectedUserGroupAPI = (Integer) randomUserMapInfo.get("user_group_id");

        }


    }
    @When("I send POST request to {string} endpoint AN")
    public void i_send_post_request_to_endpoint_an(String endPoint) {

        response = givenPart.when().post(ConfigurationReader.getProperty("library.baseUri")+endPoint).prettyPeek();
        thenPart = response.then();


    }
    @Then("status code should be {int} AN")
    public void status_code_should_be_an(Integer expectedStatusCode) {

        thenPart.statusCode(expectedStatusCode);


    }
    @Then("Response Content type is {string} AN")
    public void response_content_type_is_an(String expectedContentType) {
        thenPart.contentType(expectedContentType);

    }
    @Then("the field value for {string} path should be equal to {string} AN")
    public void the_field_value_for_path_should_be_equal_to_an(String message, String expectedText) {

        String actualText = response.jsonPath().getString(message);
        assertThat(actualText, is(expectedText));
        idAPI = response.jsonPath().getInt("user_id");
        System.out.println("idAPI = " + idAPI);


    }
    @Then("{string} field should not be null AN")
    public void field_should_not_be_null_an(String userId) {

        String userIdField = response.jsonPath().getString(userId);
        assertThat(userIdField, is(notNullValue()));

    }


    @Then("created user information should match with Database AN")
    public void created_user_information_should_match_with_database_an() {

        String requestQuery = "select * from users\n" +
                "where id = "+idAPI+"";

        DB_Util.runQuery(requestQuery);

        Map<String,Object> dbMap = DB_Util.getRowMap(1);
        System.out.println("dbMap = " + dbMap);

        String actualFullNameDB = (String) dbMap.get("full_name");
        String actualEmailDB = (String) dbMap.get("email");
        String actualPasswordDB = (String) dbMap.get("password");
        System.out.println("actualPasswordDB = " + actualPasswordDB);
        String actualUserGroupDB = (String) dbMap.get("user_group_id");

        assertThat(expectedFullNameAPI, is(actualFullNameDB));
        assertThat(expectedEmailAPI, is(actualEmailDB));

        assertThat(expectedUserGroupAPI, is(Integer.parseInt(actualUserGroupDB)));


    }
    @Then("created user should be able to login Library UI AN")
    public void created_user_should_be_able_to_login_library_ui_an() {
        BrowserUtil.waitForVisibility(new LoginPage().loginButton, 5);
         new LoginPage().login(expectedEmailAPI, expectedPasswordAPI);



    }
    @Then("created user name should appear in Dashboard Page AN")
    public void created_user_name_should_appear_in_dashboard_page_an() {

        BrowserUtil.waitForVisibility(new BookPage().accountHolderName,5);
        String actualNameUI = new BookPage().accountHolderName.getText();
        assertTrue(expectedFullNameAPI.contains(actualNameUI));

    }







}
