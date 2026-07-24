@links @noocis
Feature: Links

  As an user
  I want to handle links on my files or folders
  So that the content is accessible for whom i send the link

  Background: User is logged in
    Given user Alice is logged

  @createlink
  Rule: Create a link

  @smoke @nooc10
  Scenario Outline: Create a link with name
    Given the following items have been created in Alice account
      | type   | name   |
      | <type> | <item> |
    When Alice selects to share the <type> <item>
    And Alice creates link on <type> <item> with the following fields
      | name          | <name> |
      | password-auto |        |
    Then link should be created on <item> with the following fields
      | name | <name> |

    Examples:
      | type   | item       | name  |
      | folder | Links1     | link1 |
      | file   | Links2.txt | link2 |

  @nooc10
  Scenario Outline: Create a link with custom password
    Given the following items have been created in Alice account
      | type   | name   |
      | <type> | <item> |
    When Alice selects to share the <type> <item>
    And Alice creates link on <type> <item> with the following fields
      | name     | <name>     |
      | password | <password> |
    Then link should be created on <item> with the following fields
      | name     | <name>     |
      | password | <password> |

    Examples:
      | type   | item       | name  | password |
      | folder | Links3     | link3 | aa55AA.. |
      | file   | Links4.txt | link4 | aa55AA.. |

  @nooc10
  Scenario Outline: Create a link with generated password
    Given the following items have been created in Alice account
      | type   | name   |
      | <type> | <item> |
    When Alice selects to share the <type> <item>
    And Alice creates link on <type> <item> with the following fields
      | name          | <name> |
      | password-auto |        |
    Then link should be created on <item> with the following fields
      | name          | <name> |
      | password-auto |        |

    Examples:
      | type   | item       | name  |
      | folder | Links5     | link5 |
      | file   | Links6.txt | link6 |

  @nooc10 @expiration
  Scenario Outline: Create a link with expiration date
    Given the following items have been created in Alice account
      | type   | name   |
      | <type> | <item> |
    When Alice selects to share the <type> <item>
    And Alice creates link on <type> <item> with the following fields
      | name            | <name>       |
      | expiration days | <expiration> |
      | password-auto   |              |
    Then link should be created on <item> with the following fields
      | name            | <name>       |
      | expiration days | <expiration> |

    Examples:
      | type   | item       | name  | expiration |
      | folder | Links7     | link7 | 7          |
      | file   | Links8.txt | link8 | 17         |

  @nooc10
  Scenario Outline: Create a link with permissions on a folder
    Given the following items have been created in Alice account
      | type   | name   |
      | folder | <item> |
    When Alice selects to share the folder <item>
    And Alice creates link on folder <item> with the following fields
      | name          | <name>        |
      | permission    | <permissions> |
      | password-auto |               |
    Then link should be created on <item> with the following fields
      | name       | <name>        |
      | permission | <permissions> |

    Examples:
      | item    | name   | permissions | description
      | Links9  | link9  | 15          | Download / View / Upload
      | Links10 | link10 | 4           | Upload Only (File drop)
      | Links11 | link11 | 1           | Download / View


  @editlink
  Rule: Edit a link

  Scenario Outline: Edit existing share on a folder, changing permissions
    Given the following items have been created in Alice account
      | type   | name   |
      | folder | <item> |
    And Alice has shared the folder <item> by link
    When Alice selects to share the folder <item>
    And Alice edits the link on <item> with the following fields
      | permissions | <permissions> |
      | name        | <name>        |
    Then link should be edited on <item> with the following fields
      | permissions | <permissions> |
      | name        | <name>        |

    Examples:
      | item    | name   | permissions | description
      | Links12 | link12 | 15          | Download / View / Upload
      | Links13 | link13 | 4           | Upload Only (File drop)
      | Links14 | link14 | 1           | Download / View

  @nooc10
  Scenario: Edit existing share on a folder, adding expiration date
    Given the following items have been created in Alice account
      | type   | name    |
      | folder | Links15 |
    And Alice has shared the folder Links15 by link
    When Alice selects to share the folder Links15
    And Alice edits the link on Links15 with the following fields
      | expiration days | 1       |
      | name            | links15 |
    Then link should be created on Links15 with the following fields
      | expiration days | 1 |

  @deletelink
  Rule: Delete a link

  Scenario Outline: Delete existing link
    Given the following items have been created in Alice account
      | type   | name   |
      | <type> | <item> |
    And Alice has shared the <type> <item> by link
    When Alice selects to share the <type> <item>
    And Alice deletes the link on <item>
    Then link on <item> should not exist anymore

    Examples:
      | type   | item        |
      | folder | Links16     |
      | file   | Links17.txt |

  #ignored because this feature is missing in Android app for oCIS
  @linkshortcut @ignore
  Rule: Link Shortcut

  Scenario: Link shortcut shows correct links
    Given the following items have been created in Alice account
      | type   | name        |
      | file   | Links18.txt |
      | file   | Links19.txt |
      | folder | Links20     |
      | folder | Links21     |
    And Alice has shared the file Links18.txt by link
    And Alice has shared the folder Links20 by link
    When Alice opens the link shortcut
    Then Alice should see "Links18.txt" in the list
    And Alice should see "Links20" in the list
    But Alice should not see Links19.txt in the links list
    And Alice should not see Links21 in the links list

  Scenario Outline: Remove from link shortcut
    Given the following items have been created in Alice account
      | type   | name   |
      | <type> | <item> |
    And Alice has shared the <type> <item> by link
    When Alice opens the link shortcut
    And Alice selects to share the <type> <item>
    And Alice deletes the link on <item>
    And Alice closes share view
    And Alice refreshes the list
    Then Alice should not see <item> in the offline list
    And Alice should see the following message
      | No shared links |

    Examples:
      | type | item        |
      | file | Links22.txt |
