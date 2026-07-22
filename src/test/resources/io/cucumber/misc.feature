@misc @smoke
Feature: Other miscellaneous tests

  Background: User is logged in
  Given user Alice is logged

  @noci #Chrome versions break this test
  Scenario: Check Help view is displayed
    When Alice opens the Help web
    Then Alice should see the Help web

  Scenario: Check Privacy Policy view is displayed
    When Alice opens the Privacy Policy web
    Then Alice should see the Privacy Policy web