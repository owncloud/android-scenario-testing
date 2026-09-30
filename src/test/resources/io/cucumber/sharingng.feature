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
        | type   | name   |
        | <type> | <item> |
      When Alice selects to share the file <item>
      And Alice adds <shareeType> <sharee> via Sharing NG with
        | permission      | <permission>     |
        | expirationDate  | <expirationDate> |
      Then <sharee> should be visible in Sharing NG with
        | permission      | <permission>     |
        | expirationDate  | <expirationDate> |
      And <shareeType> <sharee> should have access via NG to <item>

      Examples:
        | type   | item          | permission             | expirationDate  | sharee  | shareeType |
        | file   | ShareNG1.txt  | Can view               |                 | Bob     | user       |
        | file   | ShareNG2.txt  | Can edit               |  5              | Bob     | user       |
        | folder | ShareNG3      | Can edit with trashbin |  10             | Charles | user       |
        | folder | ShareNG4      | Can edit               |                 | test    | group      |

  @removeshareng
  Rule: Remove a share

    Scenario Outline: Remove an existing share
      Given the following items have been created in Alice account
        | type   | name   |
        | <type> | <item> |
      And Alice has shared <type> <item> with <sharee> with
        | permission      | <permission>   |
        | expirationDate  |                |
      When Alice selects to share the <type> <item>
      And Alice removes the share on <type> <item> for user <sharee>
      Then user <sharee> should not have access via NG to <item>

      Examples:
        | type | item          | permission | sharee |
        | file | ShareNG5.txt  | Can view   | Bob    |

  @editshareng
  Rule: Edit a share

    Scenario Outline: Edit an existing share
      Given the following items have been created in Alice account
        | type   | name   |
        | <type> | <item> |
      And Alice has shared <type> <item> with <sharee> with
        | permission      | <permission>     |
        | expirationDate  | <expirationDate> |
        | shareeType      | <shareeType>     |
      When Alice selects to share the <type> <item>
      And Alice edits the share on <type> <item> for user <sharee> with
        | permission      | <newPermission>     |
        | expirationDate  | <newExpirationDate> |
      Then <sharee> should be visible in Sharing NG with
        | permission      | <newPermission>     |
        | expirationDate  | <newExpirationDate> |
      And <shareeType> <sharee> should have access via NG to <item>

      Examples:
        | type   | item          | sharee | shareeType | permission             | expirationDate | newPermission          | newExpirationDate |
        | file   | ShareNG6.txt  | Bob    | user       | Can view               |                | Can edit               | 10                |
        | folder | ShareNG7      | Bob    | user       | Can edit               | 10             | Can edit with trashbin | 20                |
        | folder | ShareNG8      | test   | group      | Can edit with trashbin | 10             | Can view               |                   |
        | file   | ShareNG9.txt  | Bob    | user       | Can view               | 10             | Can edit               | 10                |
        | folder | ShareNG10     | Bob    | user       | Can edit               | 10             | Can edit               | 20                |
