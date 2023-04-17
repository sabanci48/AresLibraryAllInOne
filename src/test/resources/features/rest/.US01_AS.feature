Feature: As a librarian, I want to retrieve all users

  @us01_AS
  Scenario: Retrieve all users from the API endpoint

    Given I logged Library api as a "librarian" AS
    And Accept header is "application/json" AS
    When I send GET request to "/get_all_users" endpoint AS
    Then status code should be 200 AS
    And Response Content type is "application/json; charset=utf-8" AS
    And "id" field should not be null AS
    And "name" field should not be null AS
