/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.assertions;

import static org.assertj.core.api.Assertions.assertThat;

import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import e2e.model.OCSpace;
import e2e.model.OCSpaceLink;
import e2e.model.OCSpaceMember;
import e2e.support.date.DateUtils;
import e2e.support.log.Log;
import e2e.world.World;

public class SpacesAssertions {

    private final World world;

    public SpacesAssertions(World world) {
        this.world = world;
    }

    public void assertSpacesVisibility(String sense, String status, List<Map<String, String>> rows)
            throws IOException {
        List<OCSpace> serverSpaces = world.graphAPI().getMySpaces();
        for (Map<String, String> row : rows) {
            String name = getRequiredTrimmedValue(row, "name");
            String subtitle = getOptionalTrimmedValue(row, "subtitle", "");
            Log.log(Level.FINE, "Checking sense: " + sense
                    + " for space: " + name + " " + subtitle);
            if (isPositiveSense(sense)) {
                assertSpaceIsVisibleLocally(name, subtitle, status);
                assertSpaceExistsInServer(name, subtitle, serverSpaces);
            } else if (isNegativeSense(sense)) {
                assertSpaceIsNotVisibleLocally(name, subtitle);
            }
        }
    }

    public void assertSpaceCreatedOrUpdatedInServer(Map<String, String> fields)
            throws IOException {
        String expectedName = getRequiredValue(fields, "name");
        String expectedSubtitle = getOptionalValue(fields, "subtitle", "");
        String expectedQuota = getRequiredValue(fields, "quota");
        String unit = getRequiredValue(fields, "unit");
        Log.log(Level.FINE, "Space from scenario: "
                + expectedName + " " + expectedSubtitle + " " + expectedQuota);
        List<OCSpace> serverSpaces = world.graphAPI().getMySpaces();
        boolean found = false;
        for (OCSpace space : serverSpaces) {
            Log.log(Level.FINE, "Space in server: " + space.getName() + " " + space.getDescription() + " "
                    + space.getQuota(unit));
            if (space.getName().equals(expectedName) && space.getDescription().equals(expectedSubtitle)
                    && space.getQuota(unit).equals(expectedQuota)) {
                found = true;
                break;
            }
        }
        assertThat(found)
                .as("space '%s' should exist on server with correct name, subtitle, and quota", expectedName)
                .isTrue();
    }

    public void assertSpaceImageUpdatedInServer(String fileName, List<Map<String, String>> rows)
            throws IOException {
        for (Map<String, String> row : rows) {
            String name = getRequiredValue(row, "name");
            String subtitle = getOptionalValue(row, "subtitle", "");
            String spaceId = world.graphAPI().getSpaceIdFromNameAndDescription(name, subtitle);
            assertThat(world.filesAPI().itemExist(spaceId, "/.space/" + fileName))
                    .as("space image '%s' should exist on server", fileName)
                    .isTrue();
        }
    }

    public void assertQuotaIsCorrectlyDisplayed(Map<String, String> values) {
        String expectedQuota = getRequiredValue(values, "quota");
        String expectedUnit = getRequiredValue(values, "unit");
        Log.log(Level.FINE, "Assert quota is correctly displayed."
                + " - Expected quota: " + expectedQuota + " - Expected unit: " + expectedUnit);
        assertQuotaDisplayed(expectedQuota, expectedUnit);
    }

    public void assertUserIsMemberOfSpace(String userName, String spaceName, Map<String, String> fields)
            throws IOException {
        OCSpaceMember member = world.graphAPI().getMemberOfSpace(spaceName, userName);
        Log.log(Level.FINE, "Member from backend: " + member.getDisplayName() + " " + member.getPermission() + " "
                + member.getExpirationDate());
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            assertSpaceMemberField(userName, member, entry.getKey(), entry.getValue());
        }
    }

    private void assertSpaceIsVisibleLocally(String name, String subtitle, String status) {
        assertThat(isSpaceDisplayed(name, subtitle, status))
                .as("space '%s' should be visible with status '%s'", name, status)
                .isTrue();
    }

    private void assertSpaceIsNotVisibleLocally(String name, String subtitle) {
        assertThat(isSpaceDisplayed(name, subtitle, ""))
                .as("space '%s' should not be visible", name)
                .isFalse();
    }

    private void assertSpaceExistsInServer(String expectedName, String expectedSubtitle, List<OCSpace> spaces) {
        Log.log(Level.FINE, "Checking the remote contains the local");
        boolean found = false;
        for (OCSpace space : spaces) {
            Log.log(Level.FINE, "Local: " + expectedName + " " + expectedSubtitle);
            Log.log(Level.FINE, "Checking space: " + space.getName() + " " + space.getDescription());
            if (space.getName().trim().equals(expectedName)
                    && space.getDescription().trim().equals(expectedSubtitle)) {
                Log.log(Level.FINE, "Found!!");
                found = true;
                break;
            }
        }
        assertThat(found)
                .as("space '%s' should exist on server", expectedName)
                .isTrue();
    }

    private void assertLinkExpirationDateMatches(OCSpaceLink backendLink, String expectedExpirationDays) {
        if (expectedExpirationDays == null || expectedExpirationDays.isEmpty()) {
            assertThat(backendLink.getExpirationDate())
                    .as("link expiration date should be null")
                    .isNull();
            return;
        }
        String expectedDatePrefix = DateUtils.dateInDaysWithServerFormat(expectedExpirationDays).substring(0, 10);
        assertThat(backendLink.getExpirationDate().startsWith(expectedDatePrefix))
                .as("link expiration date should start with '%s'", expectedDatePrefix)
                .isTrue();
    }

    private String getLinkPermissionDisplayName(String permission) {
        return switch (permission) {
            case "Can view" -> "view";
            case "Can edit" -> "edit";
            case "Secret file drop" -> "createOnly";
            default -> "";
        };
    }

    private boolean isPositiveSense(String sense) {
        return sense == null || sense.isEmpty();
    }

    private boolean isNegativeSense(String sense) {
        return "not".equals(sense);
    }

    private String getRequiredValue(Map<String, String> fields, String key) {
        return fields.get(key);
    }

    private String getRequiredTrimmedValue(Map<String, String> fields, String key) {
        return getRequiredValue(fields, key).trim();
    }

    private String getOptionalValue(Map<String, String> fields, String key, String defaultValue) {
        String value = fields.get(key);
        return value == null ? defaultValue : value;
    }

    private String getOptionalTrimmedValue(Map<String, String> fields, String key, String defaultValue) {
        String value = fields.get(key);
        return value == null ? defaultValue : value.trim();
    }

    private void assertSpaceMemberField(String userName, OCSpaceMember member, String key, String value) {
        switch (key) {
            case "permission" -> assertMemberPermission(userName, member, value);
            case "expirationDate" -> assertMemberExpirationDate(member, value);
        }
    }

    public void assertUserIsNotMemberOfSpace(String userName) {
        assertThat(world.spacesMembersPage().isMemberDisplayed(userName))
                .as("'%s' should not be a member of the space", userName)
                .isFalse();
    }

    private void assertMemberPermission(String userName, OCSpaceMember member, String expectedPermission) {
        assertThat(world.spacesMembersPage().isMemberDisplayedWithPermission(userName, expectedPermission))
                .as("'%s' should be displayed with permission '%s'", userName, expectedPermission)
                .isTrue();
        assertThat(member.getPermission().contains(expectedPermission))
                .as("member '%s' should have permission '%s' on backend", userName, expectedPermission)
                .isTrue();
    }

    private void assertMemberExpirationDate(OCSpaceMember member, String expectedExpirationDays) {
        String expectedLocalDate = expectedExpirationDays == null ? null
                : DateUtils.formatDate(expectedExpirationDays, DateUtils.DateFormatType.NUMERIC);
        assertThat(world.spacesMembersPage().isExpirationDateDisplayed(expectedLocalDate))
                .as("expiration date '%s' should be displayed for member", expectedLocalDate)
                .isTrue();
        Log.log(Level.FINE, "Remote date: " + member.getExpirationDate());
        if (expectedExpirationDays != null) {
            String remoteDate = member.getExpirationDate().substring(0, 10) + " 23:59:59";
            String expectedDate = DateUtils.dateInDaysWithServerFormat(expectedExpirationDays);
            Log.log(Level.FINE, "Days: " + expectedExpirationDays);
            Log.log(Level.FINE, "Date in server: " + remoteDate);
            Log.log(Level.FINE, "Date in local: " + expectedDate);
            assertThat(remoteDate)
                    .as("member expiration date on server should be '%s'", expectedDate)
                    .isEqualTo(expectedDate);
        } else {
            assertThat(member.getExpirationDate())
                    .as("member expiration date should be null on server")
                    .isNull();
        }
    }

    public void assertLinkIsVisibleOnSpace(String linkName, String spaceName, Map<String, String> fields)
            throws IOException {
        OCSpaceLink backendLink = world.graphAPI().getLinkOfSpace(spaceName, linkName);
        assertThat(backendLink)
                .as("link '%s' should exist on server for space '%s'", linkName, spaceName)
                .isNotNull();
        Log.log(Level.FINE, "Link in backend: name: " + backendLink.getLinkName() + " permission: " + backendLink.getPermission()
                + " expirationDate: " + backendLink.getExpirationDate());
        String expectedPermission = getOptionalValue(fields, "permission", "");
        String expectedExpirationDate = getOptionalValue(fields, "expirationDate", "");
        Log.log(Level.FINE, "Link from scenario: name: " + linkName + " permission: " + expectedPermission
                + " expirationDate: " + expectedExpirationDate);
        assertPublicLinkIsDisplayedLocally(linkName, expectedPermission, expectedExpirationDate);
        assertThat(backendLink.getLinkName())
                .as("link name should be '%s'", linkName)
                .isEqualTo(linkName);
        assertThat(backendLink.getPermission())
                .as("link permission should match '%s'", expectedPermission)
                .isEqualTo(getLinkPermissionDisplayName(expectedPermission));
        assertLinkExpirationDateMatches(backendLink, expectedExpirationDate);
    }

    public void assertLinkIsNotVisibleOnSpace(String linkName, String spaceName) throws IOException {
        assertThat(world.spacesMembersPage().isLinkDisplayed(linkName))
                .as("link '%s' should not be visible on space", linkName)
                .isFalse();
        OCSpaceLink backendLink = world.graphAPI().getLinkOfSpace(spaceName, linkName);
        assertThat(backendLink)
                .as("link '%s' should not exist on server", linkName)
                .isNull();
    }

    private void assertPublicLinkIsDisplayedLocally(String linkName, String expectedPermission, String expectedExpirationDays) {
        assertThat(world.spacesMembersPage().isLinkDisplayed(linkName))
                .as("link '%s' should be visible on space", linkName)
                .isTrue();
        if (expectedPermission != null && !expectedPermission.isEmpty()) {
            assertThat(world.spacesMembersPage().isLinkDisplayedWithPermission(linkName, expectedPermission))
                    .as("link '%s' should be displayed with permission '%s'", linkName, expectedPermission)
                    .isTrue();
        }
        if(expectedExpirationDays != null && !expectedExpirationDays.isEmpty()) {
            String expectedLocalExpirationDate = DateUtils.formatDate(expectedExpirationDays, DateUtils.DateFormatType.NUMERIC);
            assertThat(world.spacesMembersPage().isLinkDisplayedWithExpirationDate(linkName, expectedLocalExpirationDate))
                    .as("link '%s' should be displayed with expiration date '%s'", linkName, expectedLocalExpirationDate)
                    .isTrue();
        }
    }

    private boolean isSpaceDisplayed(String expectedName, String expectedSubtitle, String expectedStatus) {
        Log.log(Level.FINE, "Check if space " + expectedName + " is displayed with status: " + expectedStatus);
        for (WebElement spaceCard : world.spacesPage().getDisplayedSpaceCards()) {
            String cardName = world.spacesPage().getSpaceName(spaceCard);
            String cardSubtitle = world.spacesPage().getSpaceSubtitle(spaceCard);
            boolean cardDisabled = world.spacesPage().isDisabledSpace(spaceCard);
            if (!statusMatches(cardDisabled, expectedStatus)) {
                continue;
            }
            Log.log(Level.FINE, "Card: " + cardName + " - " + (cardSubtitle.isEmpty() ? "empty" : cardSubtitle));
            Log.log(Level.FINE, "Scenario: " + expectedName + " - " + (expectedSubtitle.isEmpty() ? "empty" : expectedSubtitle));
            if (expectedName.equals(cardName) && expectedSubtitle.equals(cardSubtitle)) {
                return true;
            }
        }
        return false;
    }

    private boolean statusMatches(boolean cardDisabled, String expectedStatus) {
        return switch (expectedStatus) {
            case "disabled" -> cardDisabled;
            case "enabled", "" -> !cardDisabled || expectedStatus.isEmpty();
            default -> throw new IllegalStateException("Unexpected value: " + expectedStatus);
        };
    }

    private void assertQuotaDisplayed(String expectedQuota, String expectedUnit) {
        String displayedQuota = world.spacesPage().getDisplayedQuotaValue();
        String displayedUnit = world.spacesPage().getDisplayedQuotaUnit();
        Log.log(Level.FINE, "Displayed quota: " + displayedQuota + " " + displayedUnit);
        assertThat(displayedQuota)
                .as("displayed quota should be '%s'", expectedQuota)
                .isEqualTo(expectedQuota);
        assertThat(displayedUnit)
                .as("displayed unit should be '%s'", expectedUnit)
                .isEqualTo(expectedUnit);
    }
}
