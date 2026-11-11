@linksng @nooc10
Feature: Links NG

  As a user
  I want to manage public links on my files and folders via NG
  So that I can create, edit, and eventually remove shared links

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

  @editpubliclink
  Scenario Outline: User edits an existing public link
    Given the following items have been created in Alice account
      | type   | name   |
      | <type> | <item> |
    And Alice has a public link on <type> <item> via NG with the following fields
      | name            | <initial_name>  |
      | permission      | <initial_perm>  |
      | password        | aa55AA..        |
      | expiration days | <initial_expiry>|
    When Alice selects to share the <type> <item>
    And Alice edits the public link "<initial_name>" on <type> <item> via NG with the following fields
      | name            | <new_name>     |
      | permission      | <new_perm>     |
      | password        | <new_password> |
      | expiration days | <new_expiry>   |
    Then public link should be edited on <item> via NG with the following fields
      | name            | <expected_name>  |
      | permission      | <expected_perm>  |
      | expiration days | <expected_expiry>|

    Examples:
      | type   | item             | initial_name | initial_perm     | initial_expiry | new_name | new_perm         | new_password | new_expiry | expected_name | expected_perm    | expected_expiry |
      | file   | EditLinksNG1.txt | FileLink     | Can view         | 3              | NewName  | Can edit         | bb66BB..     | 10         | NewName       | Can edit         | 10              |
      | folder | EditLinksNG2     | FolderLink   | Can edit         |                |          | Can view         |              | 7          | FolderLink    | Can view         | 7               |
      | folder | EditLinksNG3     | DropLink     | Can view         |                | (none)   | Secret file drop |              |            | Unnamed Link  | Secret file drop |                 |
      | file   | EditLinksNG4.txt | ExpiryLink   | Can edit         | 7              |          |                  |              | (none)     | ExpiryLink    | Can edit         |                 |

  @editpassword
  Scenario Outline: Removing the password of a link is blocked when the server mandates passwords
    Given the following items have been created in Alice account
      | type   | name   |
      | <type> | <item> |
    And Alice has a public link on <type> <item> via NG with the following fields
      | name       | <link_name>  |
      | permission | <permission> |
      | password   | <password>   |
    When Alice selects to share the <type> <item>
    And Alice taps to edit the public link "<link_name>"
    And Alice removes the password on the public link edit
    Then the public link save button is disabled

    Examples:
      | type | item             | link_name     | permission | password |
      | file | EditLinksNG5.txt | ProtectedLink | Can view   | aa55AA.. |
