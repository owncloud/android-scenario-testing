@linksng @nooc10
Feature: Links NG

  As a user
  I want to create public links on my files and folders via NG
  So that the content is accessible to anyone with the link

  Background: User is logged in
    Given user Alice is logged

  @createpubliclink @smoke
  Scenario Outline: Create a public link via NG
    Given the following items have been created in Alice account
      | type   | name   |
      | <type> | <item> |
    When Alice selects to share the <type> <item>
    And Alice creates public link on <type> <item> via NG with the following fields
      | name            | <link_name>       |
      | permission      | <permission>      |
      | password        | <password>        |
      | expiration days | <expiration_days> |
    Then public link should be created on <item> via NG with the following fields
      | name            | <expected_name>   |
      | permission      | <permission>      |
      | expiration days | <expiration_days> |

    Examples:
      | type   | item               | permission       | link_name  | password | expiration_days | expected_name |
      | file   | PublicLinksNG1.txt | Can view         |            | aa55AA.. |                 | Unnamed Link  |
      | file   | PublicLinksNG2.txt | Can edit         | mylink     | aa55AA.. |                 | mylink        |
      | file   | PublicLinksNG3.txt | Can view         |            | aa55AA.. | 7               | Unnamed Link  |
      | folder | PublicLinksNG4     | Can view         | folderlink | aa55AA.. | 3               | folderlink    |
      | folder | PublicLinksNG5     | Secret file drop |            | aa55AA.. |                 | Unnamed Link  |

  @createpubliclink
  Scenario Outline: Creation of public link is blocked when mandatory password is not entered
    Given the following items have been created in Alice account
      | type   | name   |
      | <type> | <item> |
    When Alice selects to share the <type> <item>
    And Alice taps to add a public link
    And Alice selects "<permission>" permission for the public link
    Then the public link confirm button is disabled

    Examples:
      | type   | item           | permission |
      | folder | PublicLinksNG6 | Can edit   |
