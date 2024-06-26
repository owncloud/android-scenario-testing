@avoffline
Feature: Set items as available offline (downloaded and synced)

  As a user
  I want to set content as available offline
  So that the content will be always downloaded and synced

  Background: User is logged in
    Given user Alice is logged

  @setasavoffline
  Rule: Set as av.offline

  @smoke
  Scenario: Set a file as available offline
    Given the following items have been created in Alice account
      | file | av.offline.pdf |
    When Alice selects to set as av.offline the item av.offline.pdf
    Then Alice should see the file av.offline.pdf as av.offline

  Scenario: Set a folder as available offline
    Given the following items have been created in Alice account
      | folder | avOffFolder             |
      | file   | avOffFolder/example.txt |
    When Alice selects to set as av.offline the item avOffFolder
    And Alice browses into avOffFolder
    Then Alice should see the file example.txt as av.offline

  @moveavoffline
  Rule: Moving av.offline items

  Scenario: Moving an av.offline item to other location does not lose the av.offline condition
    Given the following items have been created in Alice account
      | file   | avOff1.txt   |
      | folder | avOfffolder1 |
    When Alice selects to set as av.offline the item avOff1.txt
    And Alice selects to Move the file avOff1.txt
    And Alice selects avOfffolder1 as target folder
    And Alice browses into avOfffolder1
    Then Alice should see the file avOff1.txt as av.offline

  Scenario: Moving a file inside an av.offline folder, turns the file av.offline
    Given the following items have been created in Alice account
      | file   | avoff2.pdf   |
      | folder | avOffFolder2 |
    When Alice selects to set as av.offline the item avOffFolder2
    And Alice selects to Move the file avoff2.pdf
    And Alice selects avOffFolder2 as target folder
    Then Alice browses into avOffFolder2
    And Alice should see the item avoff2.pdf as av.offline

  Scenario: Moving a file that is inside an av.offline folder to a non av.offline folder, turns not av.offline
    Given the following items have been created in Alice account
      | folder | avOffFolder3            |
      | file   | avOffFolder3/avoff3.txt |
    When Alice selects to set as av.offline the item avOffFolder3
    And Alice selects to Move the file avOffFolder3/avoff3.txt
    And Alice selects / as target folder
    And Alice browses to root folder
    Then Alice should not see the file avoff3.txt as av.offline

  @smoke
  Rule: Av.offline file modification

  Scenario: Update over an av.offline file
    Given the following items have been created in Alice account
      | file | avoff4.txt |
    When Alice selects to set as av.offline the item avoff4.txt
    And file avoff4.txt is modified externally adding "updated"
    And Alice selects to Download the item avoff4.txt
    Then Alice should see the file avoff4.txt with "updated"

  @unsetavoffline
  Rule: Unset as av.offline

  Scenario Outline: Unset an item as available offline
    Given the following items have been created in Alice account
      | <type> | <item> |
    And Alice selects to set as av.offline the item <item>
    When Alice selects to unset as av.offline the item <item>
    Then Alice should not see the <type> <item> as av.offline

    Examples:
      | type   | item       |
      | file   | avOff5.txt |
      | folder | avOff6     |

  Scenario: Not posible to unset an item as available offline if parent is av. offline
    Given the following items have been created in Alice account
      | folder | avOff7            |
      | file   | avOff7/avOff8.txt |
    When Alice selects to set as av.offline the item avOff7
    And Alice browses into avOff7
    Then Alice cannot unset as av.offline the item avOff8.txt

  @avofflineshortcut
  Rule: Av. offline shortcut

  Scenario: Available offline shortcut
    Given the following items have been created in Alice account
      | file   | avOffs1.txt |
      | folder | avOffs2     |
    And Alice selects to set as av.offline the item avOffs1.txt
    When Alice opens the available offline shortcut
    Then Alice should see avOffs1.txt in the list
    But Alice should not see avOffs2 in the offline list

  Scenario: Remove from available offline shortcut
    Given the following items have been created in Alice account
      | file | avOffs3.txt |
    And Alice selects to set as av.offline the item avOffs3.txt
    When Alice opens the available offline shortcut
    And Alice selects to unset as av.offline the item avOffs3.txt
    Then Alice should not see avOffs3.txt in the offline list
    And Alice should see the following message
      | No available offline files |

