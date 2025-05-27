@conflicts @noci
Feature: Conflicts in content

  As a user
  I want to see when a file is conflicted
  So that i can decide which version i want to keep

  Background: User is logged in
    Given user Alice is logged

  @smoke
  Scenario Outline: Solve Conflict with local or remote version

    Given the following items have been created in Alice account
      | file | <fileName> |
    When Alice selects to Download the file <fileName>
    And Alice closes the preview
    And file <fileName> is modified locally adding <textLocal>
    And file <fileName> is modified remotely adding <textRemote>
    And Alice refreshes the list
    And Alice selects to Download the file <fileName>
    And Alice fixes the conflict with <fix> version
    And Alice selects to Download the file <fileName>
    Then Alice should see the file <fileName> with <textExpected>

    Examples:
      | fileName      | textLocal      | textRemote      | fix    | textExpected    |
      | conflict1.txt | "updatedLocal" | "updatedServer" | local  | updatedLocal    |
      | conflict2.txt | "updatedLocal" | "updatedServer" | server | "updatedServer" |

  Scenario: Solve Conflict with keep both version

    Given the following items have been created in Alice account
     | file | conflict3.txt |
    When Alice selects to Download the file conflict3.txt
    And Alice closes the preview
    And file conflict3.txt is modified locally adding "updatedLocal"
    And file conflict3.txt is modified remotely adding "updatedServer"
    And Alice refreshes the list
    And Alice selects to Download the file conflict3.txt
    And Alice fixes the conflict with keep both version
    Then Alice should see 'conflict3 (1).txt' in the list
