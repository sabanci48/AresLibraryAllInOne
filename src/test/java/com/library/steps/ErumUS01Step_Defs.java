package com.library.steps;
import com.library.utility.ConfigurationReader;
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

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class ErumUS01Step_Defs {
    RequestSpecification request;
    Response response;
    ValidatableResponse validate;
    /**
     * US 01 RELATED STEPS
     *
     */
    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String userType) {

        request = given().log().uri()
                .header("x-library-token", LibraryAPI_Util.getToken(userType));
    }
    @Given("Accept header is {string}")
    public void accept_header_is(String contentType) {
        request.accept(contentType);
    }

    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String endpoint) {
        response = request.when().get(ConfigurationReader.getProperty("library.baseUri") + endpoint).prettyPeek();
        validate = response.then();
    }
    @Then("status code should be {int}")
    public void status_code_should_be(Integer statusCode) {
        validate.statusCode(statusCode);
    }
    @Then("Response Content type is {string}")
    public void response_content_type_is(String contentType) {
        validate.contentType(contentType);
    }
    @Then("{string} field should not be null")
    public void field_should_not_be_null(String path) {
        validate.body(path, everyItem(notNullValue()));
    }

}
