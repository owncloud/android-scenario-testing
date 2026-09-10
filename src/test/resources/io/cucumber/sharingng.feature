@sharingng @nooc10
Feature: Sharing NG

  As a user
  I want to share my files and folders with other users via Sharing NG
  So that they can access my content with the appropriate permissions

  Background: User is logged in
    Given user Alice is logged

  @addshareng
  Rule: Add a share

    @smoke
    Scenario Outline: Share a file with a user and permission
      Given the following items have been created in Alice account
        | type | name   |
        | <type> | <item> |
      When Alice selects to share the file <item>
      And Alice adds <shareeType> <sharee> via Sharing NG with
        | permission      | <permission>     |
        | expirationDate  | <expirationDate> |
      Then <shareeType> <sharee> should have access to <item>
      Then <sharee> should be visible in Sharing NG with
        | permission      | <permission>     |
        | expirationDate  | <expirationDate> |

      Examples:
        | type   | item          | permission             | expirationDate  | sharee  | shareeType |
        | file   | ShareNG1.txt  | Can view               |                 | Bob     | user       |
        | file   | ShareNG2.txt  | Can edit               |  5              | Bob     | user       |
        | folder | ShareNG3      | Can edit with trashbin |  10             | Charles | user       |
        | folder | ShareNG4      | Can edit               |                 | test    | group      |
