@move
Feature: Move item

  As a user
  I want to move content to other location in my account
  so that the files and folders are place where i want

  Background: User is logged in
    Given user Alice is logged

  Rule: Move to regular location

    @smoke
    Scenario Outline: Move an existent non-downloaded item to another location
      Given the following items have been created in Alice account
        | type   | name     |
        | <type> | <name>   |
        | folder | <target> |
      When Alice selects to Move the <type> <name>
      And Alice selects <target> as target folder
      Then Alice should not see "<name>" in the filelist anymore
      But Alice should see '<name>' inside the folder <target>

      Examples:
        | type   | name      | target    |
        | folder | move1     | Documents |
        | file   | move2.txt | Documents |

    @smoke
    Scenario Outline: Move an existent downloaded file to another location
      Given the following items have been created in Alice account
        | type   | name     |
        | <type> | <name>   |
        | folder | <target> |
      When Alice selects to Download the <type> <name>
      And Alice closes the preview
      And Alice selects to Move the <type> <name>
      And Alice selects <target> as target folder
      Then Alice should not see "<name>" in the filelist anymore
      But Alice should see '<name>' inside the folder <target>
      And file <target>/<name> should be stored in device
      But file <name> should not be stored in device

      Examples:
        | type | name      | target    |
        | file | move3.txt | Documents |

    Scenario Outline: Move an existent item to a new created folder in the picker
      Given the following items have been created in Alice account
        | type   | name   |
        | <type> | <name> |
      When Alice selects to Move the <type> <name>
      And Alice creates new folder <target> in the folder picker
      And Alice selects <target> as target folder
      Then Alice should not see "<name>" in the filelist anymore
      But Alice should see '<name>' inside the folder <target>

      Examples:
        | type | name      | target |
        | file | move4.txt | move5  |

    Scenario Outline: Move and jumping to target folder
      Given the following items have been created in Alice account
        | type    | name       |
        | <type>  | <name>     |
        | folder  | <target>   |
      When Alice selects to Move the <type> <name>
      And Alice selects <target> as target folder
      And Alice navigates to the target folder
      Then Alice should see '<name>' inside the folder <target>

      Examples:
        | type | name      | target         |
        | file | move6.txt | targetFolder  |

  @moveconflicts
  Rule: Move with conflicts

    Scenario Outline: Move a folder to another place with same item name, keeping both
      Given the following items have been created in Alice account
        | type   | name            |
        | <type> | <name>          |
        | <type> | <target>        |
        | <type> | <target>/<name> |
      When Alice selects to Move the <type> <name>
      And Alice selects <target> as target folder
      And Alice fixes the conflict with keep both
      Then Alice should see '<name> (1)' inside the folder <target>

      Examples:
        | type   | name  | target |
        | folder | move7 | move8  |

    Scenario Outline: Move a folder to another place with same item name, replacing
      Given the following items have been created in Alice account
        | type   | name            |
        | <type> | <name>          |
        | <type> | <target>        |
        | <type> | <target>/<name> |
      When Alice selects to Move the <type> <name>
      And Alice selects <target> as target folder
      And Alice fixes the conflict with replace
      Then Alice should see '<name>' inside the folder <target>

      Examples:
        | type   | name  | target |
        | folder | move9 | move10  |

  Rule: Move negative cases

    Scenario Outline: Move a folder to itself
      Given the following items have been created in Alice account
        | type   | name   |
        | <type> | <name> |
      When Alice selects to Move the <type> <name>
      And Alice selects <target> as target folder
      Then Alice should see the following error
        | It is not possible to move a folder into a descendant |

      Examples:
        | type   | name   | target |
        | folder | move11 | move11 |

    Scenario Outline: Move a folder to same location
      Given the following items have been created in Alice account
        | type   | name   |
        | <type> | <name> |
      When Alice selects to Move the file <name>
      And Alice selects / as target folder
      Then Alice should see the following error
        | The file exists already in the destination folder |

      Examples:
        | type | name       |
        | file | move12.txt |

    Scenario Outline: Move a folder to descendant
      Given the following items have been created in Alice account
        | type   | name            |
        | <type> | <name>          |
        | <type> | <name>/<target> |
      When Alice selects to Move the folder <name>
      And Alice selects <name>/<target> as target folder
      Then Alice should see the following error
        | It is not possible to move a folder into a descendant |

      Examples:
        | type   | name   | target |
        | folder | move13 | move14 |
