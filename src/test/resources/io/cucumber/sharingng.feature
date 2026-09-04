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
      And Alice adds <sharee> via Sharing NG with
        | permission | <permission> |
      Then user <sharee> should have access to <item>
      Then <sharee> should be visible in Sharing NG with
        | permission | <permission> |

      Examples:
        | type   | item          | permission             | sharee  |
        | file   | ShareNG1.txt  | Can view               | Bob     |
        | file   | ShareNG2.txt  | Can edit               | Bob     |
        | folder | ShareNG3.txt  | Can edit with trashbin | Charles |
