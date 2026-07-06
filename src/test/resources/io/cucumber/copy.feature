@copy
Feature: Copy item

  As a user
  I want to copy files between different locations of my account
  So that i have different copies in different places that i can modify separately

  Background: User is logged in
    Given user Alice is logged

  Rule: Copy to regular location

    @smoke
    Scenario Outline: Copy an existent item to another location
      Given the following items have been created in Alice account
        | type   | name     |
        | <type> | <name>   |
        | folder | <target> |
      When Alice selects to Copy the <type> <name>
      And Alice selects <space> as space
      And Alice selects <target> as target folder
      Then Alice should see '<name>' in the filelist as original
      And Alice should see '<name>' inside the folder <target>

      Examples:
        | type   | name      | target    | space    |
        | folder | copy1     | Documents | Personal |
        | file   | copy2.txt | Documents | Personal |

    Scenario Outline: Copy an existent item to a new created folder in the picker
      Given the following items have been created in Alice account
        | type   | name   |
        | <type> | <name> |
      When Alice selects to Copy the <type> <name>
      And Alice selects <space> as space
      And Alice creates new folder <target> in the folder picker
      And Alice selects <target> as target folder
      Then Alice should see '<name>' inside the folder <target>

      Examples:
        | type | name      | target | space    |
        | file | copy3.txt | copy4  | Personal |

    @nooc10
    Scenario Outline: Copy an existent item to another space (root folder)
      Given the following items have been created in Alice account
        | type   | name   |
        | <type> | <name> |
      And the following spaces have been created in Alice account
        | name    | subtitle   |
        | <space> | <subtitle> |
      When Alice selects to Copy the <type> <name>
      And Alice selects <space> as space
      And Alice selects <target> as target folder
      Then Alice should see <name> inside the space <space>

      Examples:
        | type | name      | target | space  | subtitle |
        | file | copy5.txt | /      | Space1 |          |

    Scenario Outline: Copy and jumping to target folder
      Given the following items have been created in Alice account
        | type    | name       |
        | <type>  | <name>     |
        | folder  | <target>   |
      When Alice selects to Copy the <type> <name>
      And Alice selects Personal as space
      And Alice selects <target> as target folder
      And Alice navigates to the target folder
      Then Alice should see '<name>' inside the folder <target>

      Examples:
        | type | name      | target         |
        | file | copy6.txt | targetFolder  |

    @nooc10 @copyconflicts
    Scenario Outline: Copy a file to same place (duplication)
      Given the following items have been created in Alice account
        | type   | name   |
        | <type> | <name> |
      When Alice selects to Copy the <type> <name>
      And Alice selects <space> as space
      And Alice selects <target> as target folder
      And Alice fixes the conflict with keep both
      Then Alice should see <result> in the filelist as original

      Examples:
        | type | name      | target | space    | result          |
        | file | copy7.txt | /      | Personal | 'copy7 (1).txt' |

    @copyconflicts
    Rule: Copy with conflicts

      Scenario: Copy a folder to another place with same item name, fixing conflict with keep both
        Given the following items have been created in Alice account
          | type   | name        |
          | folder | copy8       |
          | folder | copy9       |
          | folder | copy9/copy8 |
        When Alice selects to Copy the folder copy8
        And Alice selects Personal as space
        And Alice selects copy9 as target folder
        And Alice fixes the conflict with keep both
        Then Alice should see 'copy8 (1)' inside the folder copy9

      Scenario: Copy a folder to another place with same item name, fixing conflict with replace
        Given the following items have been created in Alice account
          | type   | name             |
          | file   | copy10.txt        |
          | folder | copy11           |
          | file   | copy11/copy10.txt |
        When Alice selects to Copy the file copy10.txt
        And Alice selects Personal as space
        And Alice selects copy11 as target folder
        And Alice fixes the conflict with replace
        Then Alice should see 'copy10.txt' inside the folder copy11

      @nooc10
      Scenario: More than one conflict at the time
        Given the following items have been created in Alice account
          | type   | name              |
          | folder | test12            |
          | file   | copy12.txt        |
          | file   | copy13.txt        |
          | file   | copy14.txt        |
          | file   | test12/copy12.txt |
          | file   | test12/copy13.txt |
          | file   | test12/copy14.txt |
        When Alice long presses over copy12.txt
        And Alice multiselects the following items
          | copy13.txt |
          | copy14.txt |
        And Alice selects to Copy
        And Alice selects Personal as space
        And Alice selects test12 as target folder
        Then Alice should see the conflict dialog with the following message
          | Apply to all 3 conflicts |

  Rule: Copy negative cases

    Scenario: Copy a folder to itself
      Given the following items have been created in Alice account
        | type   | name   |
        | folder | copy15 |
      When Alice selects to Copy the folder copy15
      And Alice selects Personal as space
      And Alice selects copy15 as target folder
      Then Alice should see the following error
        | It is not possible to copy a folder into a descendant |

    Scenario: Copy a folder to descendant
      Given the following items have been created in Alice account
        | type   | name          |
        | folder | copy16        |
        | folder | copy16/copy17 |
      When Alice selects to Copy the folder copy16
      And Alice selects Personal as space
      And Alice selects copy16/copy17 as target folder
      Then Alice should see the following error
        | It is not possible to copy a folder into a descendant |

    @offline @ignore
    Scenario Outline: Copy a file with no connection
      Given the following items have been created in Alice account
        | type   | name   |
        | <type> | <name> |
      When Alice selects to Copy the <type> <name>
      And the device has no connection
      And Alice selects <space> as space
      And Alice selects <target> as target folder
      Then Alice should see the following error
        | Device is not connected to a network |

      Examples:
        | type | name      | target | space    |
        | file | copy18.txt | /      | Personal |
