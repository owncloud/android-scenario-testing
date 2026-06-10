/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.world;

import java.io.IOException;

import e2e.api.FilesAPI;
import e2e.api.GraphAPI;
import e2e.api.ShareAPI;
import e2e.api.TrashbinAPI;
import e2e.assertions.FileListAssertions;
import e2e.assertions.LinksAssertions;
import e2e.assertions.SharesAssertions;
import e2e.assertions.SpacesAssertions;
import e2e.assertions.UploadAssertions;
import e2e.pages.AndroidManager;
import e2e.pages.CameraPage;
import e2e.pages.ConflictPage;
import e2e.pages.DetailsPage;
import e2e.pages.DevicePage;
import e2e.pages.DocumentProviderPage;
import e2e.pages.FileListPage;
import e2e.pages.FolderPickerPage;
import e2e.pages.InputNamePage;
import e2e.pages.LoginPage;
import e2e.pages.PrivateSharePage;
import e2e.pages.PublicLinksPage;
import e2e.pages.RemoveDialogPage;
import e2e.pages.SearchShareePage;
import e2e.pages.SharePage;
import e2e.pages.ShortcutDialogPage;
import e2e.pages.SpaceMembersPage;
import e2e.pages.SpacesPage;
import e2e.pages.UploadsPage;
import e2e.preconditions.DevicePreconditions;
import e2e.preconditions.FileListPreconditions;
import e2e.preconditions.LinksPreconditions;
import e2e.preconditions.LoginPreconditions;
import e2e.preconditions.SharesPreconditions;
import e2e.preconditions.SpacesPreconditions;
import e2e.tasks.DocumentProviderTasks;
import e2e.tasks.FileListTasks;
import e2e.tasks.LinksTasks;
import e2e.tasks.SharesTasks;
import e2e.tasks.SpacesTasks;
import io.appium.java_client.android.AndroidDriver;

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

    private LinksTasks linksTasks;
    private SharesTasks sharesTasks;
    private FileListTasks fileListTasks;
    private SpacesTasks spacesTasks;
    private DocumentProviderTasks documentProviderTasks;

    private LinksAssertions linksAssertions;
    private SharesAssertions sharesAssertions;
    private FileListAssertions fileListAssertions;
    private SpacesAssertions spacesAssertions;
    private UploadAssertions uploadAssertions;

    private FileListPreconditions fileListPreconditions;
    private DevicePreconditions devicePreconditions;
    private LinksPreconditions linksPreconditions;
    private LoginPreconditions loginPreconditions;
    private SharesPreconditions sharesPreconditions;
    private SpacesPreconditions spacesPreconditions;

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

    public LinksTasks linkTasks() {
        if (linksTasks == null) {
            linksTasks = new LinksTasks(this);
        }
        return linksTasks;
    }

    public SharesTasks sharesTasks() {
        if (sharesTasks == null) {
            sharesTasks = new SharesTasks(this);
        }
        return sharesTasks;
    }

    public FileListTasks fileListTasks() {
        if (fileListTasks == null) {
            fileListTasks = new FileListTasks(this);
        }
        return fileListTasks;
    }

    public SpacesTasks spacesTasks() {
        if (spacesTasks == null) {
            spacesTasks = new SpacesTasks(this);
        }
        return spacesTasks;
    }

    public DocumentProviderTasks documentProviderTasks() {
        if (documentProviderTasks == null) {
            documentProviderTasks = new DocumentProviderTasks(this);
        }
        return documentProviderTasks;
    }

    public LinksAssertions linksAssertions() {
        if (linksAssertions == null) {
            linksAssertions = new LinksAssertions(this);
        }
        return linksAssertions;
    }

    public SharesAssertions sharesAssertions() {
        if (sharesAssertions == null) {
            sharesAssertions = new SharesAssertions(this);
        }
        return sharesAssertions;
    }

    public FileListAssertions fileListAssertions() {
        if (fileListAssertions == null) {
            fileListAssertions = new FileListAssertions(this);
        }
        return fileListAssertions;
    }

    public SpacesAssertions spacesAssertions() {
        if (spacesAssertions == null) {
            spacesAssertions = new SpacesAssertions(this);
        }
        return spacesAssertions;
    }

    public UploadAssertions uploadAssertions() {
        if (uploadAssertions == null) {
            uploadAssertions = new UploadAssertions(this);
        }
        return uploadAssertions;
    }

    public FileListPreconditions fileListPreconditions() {
        if (fileListPreconditions == null) {
            fileListPreconditions = new FileListPreconditions(this);
        }
        return fileListPreconditions;
    }

    public DevicePreconditions devicePreconditions() {
        if (devicePreconditions == null) {
            devicePreconditions = new DevicePreconditions(this);
        }
        return devicePreconditions;
    }

    public LinksPreconditions linksPreconditions() {
        if (linksPreconditions == null) {
            linksPreconditions = new LinksPreconditions(this);
        }
        return linksPreconditions;
    }

    public LoginPreconditions loginPreconditions() {
        if (loginPreconditions == null) {
            loginPreconditions = new LoginPreconditions(this);
        }
        return loginPreconditions;
    }

    public SharesPreconditions sharesPreconditions() {
        if (sharesPreconditions == null) {
            sharesPreconditions = new SharesPreconditions(this);
        }
        return sharesPreconditions;
    }

    public SpacesPreconditions spacesPreconditions() {
        if (spacesPreconditions == null) {
            spacesPreconditions = new SpacesPreconditions(this);
        }
        return spacesPreconditions;
    }
}
