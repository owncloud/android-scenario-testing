/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package io.cucumber;

import android.AndroidManager;
import android.CameraPage;
import android.ConflictPage;
import android.DetailsPage;
import android.DevicePage;
import android.DocumentProviderPage;
import android.FileListPage;
import android.FolderPickerPage;
import android.InputNamePage;
import android.LoginPage;
import android.PrivateSharePage;
import android.PublicLinksPage;
import android.RemoveDialogPage;
import android.SearchShareePage;
import android.SharePage;
import android.ShortcutDialogPage;
import android.SpaceMembersPage;
import android.SpacesPage;
import android.UploadsPage;

import java.io.IOException;

import io.appium.java_client.android.AndroidDriver;
import utils.api.FilesAPI;
import utils.api.GraphAPI;
import utils.api.ShareAPI;
import utils.api.TrashbinAPI;

public class World {

    private final AndroidDriver driver;

    private LoginPage loginPage;
    private FileListPage fileListPage;
    private InputNamePage inputNamePage;
    private FolderPickerPage folderPickerPage;
    private RemoveDialogPage removeDialogPage;
    private DetailsPage detailsPage;
    private SharePage sharePage;
    private PublicLinksPage publicLinksPage;
    private SearchShareePage searchShareePage;
    private PrivateSharePage privateSharePage;
    private SpacesPage spacesPage;
    private DocumentProviderPage documentProviderPage;
    private UploadsPage uploadsPage;
    private CameraPage cameraPage;
    private ShortcutDialogPage shortcutDialogPage;
    private DevicePage devicePage;
    private ConflictPage conflictPage;
    private SpaceMembersPage spacesMembersPage;

    private ShareAPI shareAPI;
    private FilesAPI filesAPI;
    private GraphAPI graphAPI;
    private TrashbinAPI trashbinAPI;

    public World() {
        this.driver = AndroidManager.getDriver();
    }

    public LoginPage loginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage(driver);
        }
        return loginPage;
    }

    public FileListPage fileListPage() {
        if (fileListPage == null) {
            fileListPage = new FileListPage(driver);
        }
        return fileListPage;
    }

    public InputNamePage inputNamePage() {
        if (inputNamePage == null) {
            inputNamePage = new InputNamePage(driver);
        }
        return inputNamePage;
    }

    public FolderPickerPage folderPickerPage() {
        if (folderPickerPage == null) {
            folderPickerPage = new FolderPickerPage(driver);
        }
        return folderPickerPage;
    }

    public RemoveDialogPage removeDialogPage() {
        if (removeDialogPage == null) {
            removeDialogPage = new RemoveDialogPage(driver);
        }
        return removeDialogPage;
    }

    public DetailsPage detailsPage() {
        if (detailsPage == null) {
            detailsPage = new DetailsPage(driver);
        }
        return detailsPage;
    }

    public SharePage sharePage() {
        if (sharePage == null) {
            sharePage = new SharePage(driver);
        }
        return sharePage;
    }

    public PublicLinksPage publicLinksPage() {
        if (publicLinksPage == null) {
            publicLinksPage = new PublicLinksPage(driver);
        }
        return publicLinksPage;
    }

    public SearchShareePage searchShareePage() {
        if (searchShareePage == null) {
            searchShareePage = new SearchShareePage(driver);
        }
        return searchShareePage;
    }

    public PrivateSharePage privateSharePage() {
        if (privateSharePage == null) {
            privateSharePage = new PrivateSharePage(driver);
        }
        return privateSharePage;
    }

    public SpacesPage spacesPage() {
        if (spacesPage == null) {
            spacesPage = new SpacesPage(driver);
        }
        return spacesPage;
    }

    public DocumentProviderPage documentProviderPage() {
        if (documentProviderPage == null) {
            documentProviderPage = new DocumentProviderPage(driver);
        }
        return documentProviderPage;
    }

    public UploadsPage uploadsPage() {
        if (uploadsPage == null) {
            uploadsPage = new UploadsPage(driver);
        }
        return uploadsPage;
    }

    public CameraPage cameraPage() {
        if (cameraPage == null) {
            cameraPage = new CameraPage(driver);
        }
        return cameraPage;
    }

    public ShortcutDialogPage shortcutDialogPage() {
        if (shortcutDialogPage == null) {
            shortcutDialogPage = new ShortcutDialogPage(driver);
        }
        return shortcutDialogPage;
    }

    public DevicePage devicePage() {
        if (devicePage == null) {
            devicePage = new DevicePage(driver);
        }
        return devicePage;
    }

    public ConflictPage conflictPage() {
        if (conflictPage == null) {
            conflictPage = new ConflictPage(driver);
        }
        return conflictPage;
    }

    public SpaceMembersPage spacesMembersPage() {
        if (spacesMembersPage == null) {
            spacesMembersPage = new SpaceMembersPage(driver);
        }
        return spacesMembersPage;
    }

    public ShareAPI shareAPI() throws IOException {
        if (shareAPI == null) {
            shareAPI = new ShareAPI();
        }
        return shareAPI;
    }

    public FilesAPI filesAPI() throws IOException {
        if (filesAPI == null) {
            filesAPI = new FilesAPI();
        }
        return filesAPI;
    }

    public GraphAPI graphAPI() throws IOException {
        if (graphAPI == null) {
            graphAPI = new GraphAPI();
        }
        return graphAPI;
    }

    public TrashbinAPI trashbinAPI() throws IOException {
        if (trashbinAPI == null) {
            trashbinAPI = new TrashbinAPI();
        }
        return trashbinAPI;
    }
}
