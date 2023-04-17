Feature: Default

  @B28G7-307
  Scenario: US04 Test case
  @us04 @B28G7-307
  Scenario: Create a new user API
    Given I logged Library api as a "librarian" AN
    And Accept header is "application/json" AN
    And Request Content Type header is "application/x-www-form-urlencoded" AN
		    # application/x-www-form-urlencoded
    And I create a random "user" as request body AN
    When I send POST request to "/add_user" endpoint AN
    Then status code should be 200 AN
    And Response Content type is "application/json; charset=utf-8" AN
    And the field value for "message" path should be equal to "The user has been created." AN
    And "user_id" field should not be null AN


  @us04 @db @ui @B28G7-307
  Scenario: Create a new user all layers
    Given I logged Library api as a "librarian" AN
    And Accept header is "application/json" AN
    And Request Content Type header is "application/x-www-form-urlencoded" AN
    And I create a random "user" as request body AN
    When I send POST request to "/add_user" endpoint AN
    Then status code should be 200 AN
    And Response Content type is "application/json; charset=utf-8" AN
    And the field value for "message" path should be equal to "The user has been created." AN
    And "user_id" field should not be null AN
    And created user information should match with Database AN
    And created user should be able to login Library UI AN
    And created user name should appear in Dashboard Page AN