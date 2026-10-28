@permalink @nooc10
Feature: Permalink

  As a user
  I want to copy or share the private link of a file or folder
  So that I can send it to others for direct access inside the app

  Background: User is logged in
    Given user Alice is logged

  Scenario Outline: Permalink button is visible in Share header
    Given the following items have been created in Alice account
      | type   | name   |
      | <type> | <item> |
    When Alice selects to share the <type> <item>
    Then the permalink button is shown

    Examples:
      | type   | item            |
      | file   | Permalink1.txt  |

  @ignore
  Scenario Outline: Tapping permalink button copies or sends the private link
    Given the following items have been created in Alice account
      | type   | name   |
      | <type> | <item> |
    When Alice selects to share the <type> <item>
    And Alice taps the permalink button
    Then the private link action is triggered

    Examples:
      | type   | item            |
      | folder | Permalink2      |
