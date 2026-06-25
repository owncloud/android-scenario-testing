/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package e2e.support.video;

import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.WebDriverException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.logging.Level;

import e2e.support.log.Log;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;

public class ScreenRecorder {

    private static final int BIT_RATE = 2_000_000;
    private static final String VIDEO_SIZE = "360x640";
    private static final String VIDEO_FOLDER = "video";
    private static final String VIDEO_EXTENSION = ".mp4";
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd_HHmmss");

    private final AndroidDriver driver;

    private boolean recordingStarted = false;

    public ScreenRecorder(AndroidDriver driver) {
        this.driver = driver;
    }

    public void startRecording() {
        try {
            AndroidStartScreenRecordingOptions options = new AndroidStartScreenRecordingOptions();
            options.withBitRate(BIT_RATE);
            options.withVideoSize(VIDEO_SIZE);
            driver.startRecordingScreen(options);
            recordingStarted = true;
        } catch (Exception e) {
            recordingStarted = false;
            Log.log(Level.FINE, "Screen recording not initiated. Error: " + e.getMessage());
        }
    }

    public void stopRecording(String filename, String featureName, boolean failed) {
        if (!recordingStarted) {
            return;
        }
        try {
            String base64String = driver.stopRecordingScreen();
            byte[] data = Base64.decodeBase64(base64String);
            if (failed) {
                saveVideo(filename, featureName, data);
            }
        } catch (WebDriverException wde) {
            recordingStarted = false;
            Log.log(Level.FINE, "Error when stopping screen recording: " + wde.getMessage());
        } catch (Exception e) {
            recordingStarted = false;
            Log.log(Level.FINE, "Error saving video: " + e.getMessage());
        }
    }

    private void saveVideo(String filename, String featureName, byte[] data)
            throws IOException {
        createFeatureFolder(featureName);
        String timestamp = SDF.format(new Timestamp(System.currentTimeMillis()).getTime());
        String destinationPath = VIDEO_FOLDER + "/" + featureName + "/"
                + filename + "_" + timestamp + VIDEO_EXTENSION;
        Path path = Paths.get(destinationPath);
        Files.write(path, data);
    }

    private void createFeatureFolder(String featureName) {
        File folder = new File(VIDEO_FOLDER + "/" + featureName);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}
