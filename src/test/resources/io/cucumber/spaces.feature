@spaces @nooc10
Feature: Spaces

  As a user
  I want to be able to check my available spaces
  so that i can browse through and add share my content with other members

  Background: User is logged in
    Given user Alice is logged

  @listspace
  Rule: List correct spaces

    @smoke
    Scenario Outline: List space created in server
      Given the following spaces have been created in Alice account
        | name    | subtitle    |
        | <name1> | <subtitle1> |
        | <name2> | <subtitle2> |
      When Alice selects the spaces view
      Then Alice should see the following enabled spaces
        | name    | subtitle    |
        | <name1> | <subtitle1> |
        | <name2> | <subtitle2> |

      Examples:
        | name1  | subtitle1   | name2  | subtitle2    |
        | Space1 | First space | Space2 | Second space |

    Scenario Outline: Update space created in server
      Given the following spaces have been created in Alice account
        | name    | subtitle    |
        | <name1> | <subtitle1> |
      And Alice selects the spaces view
      When the following spaces have been created in Alice account
        | name    | subtitle    |
        | <name2> | <subtitle2> |
      And Alice refreshes the list
      Then Alice should see the following enabled spaces
        | name    | subtitle    |
        | <name1> | <subtitle1> |
        | <name2> | <subtitle2> |

      Examples:
        | name1  | subtitle1   | name2  | subtitle2    |
        | Space3 | Third space | Space4 | Fourth space |

    Scenario Outline: Disable a space in the server
      Given the following spaces have been created in Alice account
        | name    | subtitle    |
        | <name1> | <subtitle1> |
        | <name2> | <subtitle2> |
      And the following settings have been set
        | setting              | value |
        | show_disabled_spaces | true  |
      And Alice selects the spaces view
      When the following spaces are disabled in server
        | name    | subtitle    |
        | <name1> | <subtitle1> |
      And Alice refreshes the list
      Then Alice should see the following enabled spaces
        | name    | subtitle    |
        | <name2> | <subtitle2> |
      But Alice should see the following disabled spaces
        | name    | subtitle    |
        | <name1> | <subtitle1> |

      Examples:
        | name1  | subtitle1   | name2  | subtitle2   |
        | Space5 | Fifth space | Space6 | Sixth space |

    Scenario Outline: Filter a space
      Given the following spaces have been created in Alice account
        | name    | subtitle    |
        | <name1> | <subtitle1> |
        | <name2> | <subtitle2> |
        | <name3> | <subtitle3> |
      And Alice selects the spaces view
      When Alice filters the list using Space8
      And Alice refreshes the list
      Then Alice should see the following enabled spaces
        | name    | subtitle    |
        | <name2> | <subtitle2> |
      But Alice should not see the following spaces
        | name    | subtitle    |
        | <name1> | <subtitle1> |
        | <name3> | <subtitle3> |

      Examples:
        | name1  | subtitle1     | name2  | subtitle2    | name3  | subtitle3   |
        | Space7 | Seventh space | Space8 | Eighth space | Space9 | Ninth space |

  @createspace
  Rule: Create space (admins, space admins)

    @smoke
    Scenario Outline: Create a new space with correct name, subtitle and quota
      When Alice selects the spaces view
      And Alice creates a new space with the following fields
        | name     | <name>     |
        | subtitle | <subtitle> |
        | quota    | <quota>    |
      Then the space should be created in server with the following fields
        | name     | <name>     |
        | subtitle | <subtitle> |
        | quota    | <quota>    |
        | unit     | <unit>     |
      And Alice should see the following enabled spaces
        | name   | subtitle   |
        | <name> | <subtitle> |

      Examples:
        | name    | subtitle       | quota          | unit |
        | Space10 | Tenth space    | 0.0004         | GB   |
        | Space11 | Eleventh space | 124.75         | GB   |
        | Space12 | Twelfth space  | 1000000        | GB   |
        | Space13 | Thirdt space   | No restriction | GB   |

  @editspace
  Rule: Edit existing space (admins, space admins)

    Scenario Outline: Edit an existing space with correct name and subtitle
      Given the following spaces have been created in Alice account
        | name   | subtitle   |
        | <name> | <subtitle> |
      When Alice selects the spaces view
      And Alice edits the space <name>
      And Alice updates the space with the following fields
        | name     | <newName>     |
        | subtitle | <newSubtitle> |
        | quota    | <quota>       |
      Then Alice should see the following enabled spaces
        | name      | subtitle      |
        | <newName> | <newSubtitle> |
      But Alice should not see the following spaces
        | name   | subtitle   |
        | <name> | <subtitle> |
      And the space should be updated in server with the following fields
        | name     | <newName>     |
        | subtitle | <newSubtitle> |
        | quota    | <quota>       |
        | unit     | <unit>        |

      Examples:
        | name    | subtitle         | newName     | newSubtitle      | quota  | unit |
        | Space14 | Fourteenth space | Space14 new | Fourth space new | 100    | GB   |
        | Space15 | Fifteenth space  | Space15 new | newSub           | 125.75 | GB   |

    Scenario Outline: Edit an existing space with quota only
      Given the following spaces have been created in Alice account
        | name   | subtitle   |
        | <name> | <subtitle> |
      When Alice selects the spaces view
      And Alice edits the space <name>
      And Alice updates the space with the following fields
        | name     | <name>     |
        | subtitle | <subtitle> |
        | quota    | <quota>    |
      Then the space should be updated in server with the following fields
        | name     | <name>     |
        | subtitle | <subtitle> |
        | quota    | <newQuota> |
        | unit     | <unit>     |
      And the quota is correctly displayed
        | name  | <name>     |
        | quota | <newQuota> |
        | unit  | <unit>     |

      Examples:
        | name    | subtitle          | quota | newQuota | unit |
        | Space16 | Sixteenth space   | 2300  | 2.3      | TB   |
        | Space17 | Seventeenth space | 0.01  | 10       | MB   |

    @ignore @image
    Scenario Outline: Edit an existing space with new image
      Given the following spaces have been created in Alice account
        | name   | subtitle   |
        | <name> | <subtitle> |
      And a file <fileName> exists in the device
      When Alice selects the spaces view
      And Alice edits the image of the space <name> with the file <fileName>
      Then the space image should be updated in server with file <fileName>
        | name   | subtitle   |
        | <name> | <subtitle> |

      Examples:
        | name    | subtitle         | fileName |
        | Space18 | Eighteenth space | icon.png |

  @disablespace
  Rule: Disable/Delete existing space (admins, space admins)

    Scenario Outline: Disable an existing space
      Given the following spaces have been created in Alice account
        | name   | subtitle   |
        | <name> | <subtitle> |
      And the following settings have been set
        | setting              | value |
        | show_disabled_spaces | true  |
      When Alice selects the spaces view
      And Alice disables the space <name>
      Then Alice should see the following disabled spaces
        | name   | subtitle   |
        | <name> | <subtitle> |
      And the following spaces are disabled in server
        | name   | subtitle   |
        | <name> | <subtitle> |

      Examples:
        | name    | subtitle         |
        | Space19 | Nineteenth space |

    Scenario Outline: Enable a disabled space
      Given the following spaces have been created in Alice account
        | name   | subtitle   |
        | <name> | <subtitle> |
      And the following settings have been set
        | setting              | value |
        | show_disabled_spaces | true  |
      And the following spaces are disabled in server
        | name   | subtitle   |
        | <name> | <subtitle> |
      When Alice selects the spaces view
      And Alice enables the space <name>
      Then Alice should see the following enabled spaces
        | name   | subtitle   |
        | <name> | <subtitle> |

      Examples:
        | name    | subtitle        |
        | Space20 | Twentieth space |

    Scenario Outline: Delete a disabled space
      Given the following spaces have been created in Alice account
        | name   | subtitle   |
        | <name> | <subtitle> |
      And the following settings have been set
        | setting              | value |
        | show_disabled_spaces | true  |
      And the following spaces are disabled in server
        | name   | subtitle   |
        | <name> | <subtitle> |
      When Alice selects the spaces view
      And Alice deletes the space <name>
      Then Alice should not see the following spaces
        | name   | subtitle   |
        | <name> | <subtitle> |

      Examples:
        | name    | subtitle          |
        | Space21 | Twentyfirst space |

  @spacemembership
  Rule: Space Membership

    Scenario Outline: Add a member to a space
      Given the following spaces have been created in Alice account
        | name   | subtitle   |
        | <name> | <subtitle> |
      And Alice selects the spaces view
      And Alice adds Bob to the space <name> with
        | permission | <permissions> |
      Then Bob should be member of the space <name> with
        | permission | <permissions> |

      Examples:
        | name    | subtitle           | permissions |
        | Space22 | Twentysecond space | Can view    |
        | Space23 | Twentythird space  | Can edit    |
        | Space24 | Twentyfourth space | Can manage  |

    @expiration
    Scenario Outline: Add a member to a space with expiration date
      Given the following spaces have been created in Alice account
        | name   | subtitle   |
        | <name> | <subtitle> |
      And Alice selects the spaces view
      And Alice adds Bob to the space <name> with
        | permission     | <permissions>    |
        | expirationDate | <expirationDate> |
      Then Bob should be member of the space <name> with
        | description    | <subtitle>       |
        | permission     | <permissions>    |
        | expirationDate | <expirationDate> |

      Examples:
        | name    | subtitle          | permissions | expirationDate |
        | Space25 | Twentyfifth space | Can view    | 25             |

    Scenario Outline: Edit a member from a space
      Given the following spaces have been created in Alice account
        | name   | subtitle   |
        | <name> | <subtitle> |
      And the following users are members of the space <name>
        | user | permission          | expirationDate      |
        | Bob  | <initialPermission> | <initialExpiration> |
      When Alice selects the spaces view
      And Alice edits Bob from the space <name> with the following fields
        | permission     | <permissions>    |
        | expirationDate | <expirationDate> |
      Then Bob should be member of the space <name> with
        | permission     | <permissions>    |
        | expirationDate | <expirationDate> |

      Examples:
        | name    | subtitle            | initialPermission | initialExpiration | permissions | expirationDate |
        | Space26 | Twentyfifth space   | Can view          | 12                | Can edit    | 20             |
        | Space27 | Twentyseventh space | Can manage        |                   | Can edit    | 10             |
        | Space28 | Twentyeighth space  | Can edit          | 22                | Can manage  |                |

    Scenario: Remove a member from a space
      Given the following spaces have been created in Alice account
        | name    | subtitle          |
        | Space29 | Twentyninth space |
      And the following users are members of the space Space29
        | user    | permission |
        | Bob     | Can view   |
        | Charles | Can edit   |
      When Alice selects the spaces view
      And Alice removes Bob from the space Space29
      Then Bob should not be member of the space Space29
      And Charles should be member of the space Space29 with
        | permission | Can edit |

  @spacelinks
  Rule: Space Links

    Scenario Outline: Add a link with name and permissions to a space
      Given the following spaces have been created in Alice account
        | name   | subtitle   |
        | <name> | <subtitle> |
      When Alice selects the spaces view
      And Alice creates a new link to the space <name> with
        | name       | <linkName>    |
        | permission | <permissions> |
        | password   |               |
      Then Alice should see the link <linkName> on <name> with
        | permission | <permissions>  |

      Examples:
        | name    | subtitle           | permissions       | linkName   |
        | Space30 | Thirtyeth space    | Can view          | Link30     |
        | Space31 | Thirtyfirst space  | Can edit          | Link31     |
        | Space32 | Thirtysecond space | Secret file drop  | Link32     |

    Scenario Outline: Add a link with expiration date to a space
      Given the following spaces have been created in Alice account
        | name   | subtitle   |
        | <name> | <subtitle> |
      When Alice selects the spaces view
      And Alice creates a new link to the space <name> with
        | name           | <linkName>       |
        | permission     | <permissions>    |
        | password       |                  |
        | expirationDate | <expirationDate> |
      Then Alice should see the link <linkName> on <name> with
        | permission     | <permissions>    |
        | expirationDate | <expirationDate> |

      Examples:
        | name    | subtitle           | permissions       | linkName   | expirationDate |
        | Space33 | Thirtythird space  | Can view          | Link33     | 28             |

    Scenario Outline: Edit a exiting link
      Given the following spaces have been created in Alice account
        | name   | subtitle   |
        | <name> | <subtitle> |
      And the link <linkName> was created on the space <name> with
        | name           | permission     | expirationDate   | password |
        | <linkName>     | <permissions>  | <expirationDate> |          |
      When Alice selects the spaces view
      And Alice edits <linkName> over the space <name> with
        | name           | <newName>           |
        | permission     | <newPermissions>    |
        | password       |                     |
        | expirationDate | <newExpirationDate> |
      Then Alice should see the link <newName> on <name> with
        | permission     | <newPermissions>    |
        | expirationDate | <newExpirationDate> |

      Examples:
        | name    | subtitle            | permissions       | linkName   | expirationDate | newName   | newPermissions         | newExpirationDate |
        | Space34 | Thirtyfourth space  | Can view          | Link34     | 2              | Link34new | Can edit               | 4                 |
        | Space35 | Twentyfifth space   | Can edit          | Link35     |                | Link35new | Secret file drop       | 14                |
        | Space36 | Twentysixth space   | Secret file drop  | Link36     | 12             | Link36new | Can edit               |                   |

    @removelink
    Scenario: Remove a link from a space
      Given the following spaces have been created in Alice account
        | name    | subtitle            |
        | Space37 | Thirtyseventh space |
      And the link Link37 was created on the space Space37 with
        | name           | permission  | expirationDate | password |
        | Link37         | Can edit    | 6              |          |
      When Alice selects the spaces view
      And Alice removes Link37 over the space Space37
      Then Alice should not see the link Link37 on Space37
