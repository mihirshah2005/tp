package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for the Help window that displays the User Guide in a scrollable WebView.
 */
public class HelpWindow extends UiPart<Stage> {

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";
    private static final String USER_GUIDE_URL =
            "https://ay2526s1-cs2103t-f10-1.github.io/tp/UserGuide.html";

    @FXML
    private TextArea summaryArea;

    /**
     * Creates a new HelpWindow using the given stage.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        loadUserGuide();
    }

    /**
     * Creates a new HelpWindow with a new stage.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the Help window and centers it on the screen.
     */
    public void show() {
        Stage root = getRoot();
        if (root.isIconified()) {
            root.setIconified(false);
        }
        if (!root.isShowing()) {
            root.setResizable(true);
            root.show();
            root.centerOnScreen();
        }
        root.toFront();
        root.requestFocus();
    }

    public boolean isShowing() {
        return getRoot().isShowing();
    }

    public void hide() {
        getRoot().hide();
    }

    /**
     * Brings the main application window to the front and requests focus.
     * <p>
     * If the window is currently minimized (iconified), this method restores it
     * before bringing it to the front and setting the keyboard focus.
     */
    public void focus() {
        Stage root = getRoot();
        if (root.isIconified()) {
            root.setIconified(false);
        }
        root.toFront();
        root.requestFocus();
    }

    /**
     * Loads the online User Guide into the WebView.
     * If loading fails, shows a fallback message.
     */
    private void loadUserGuide() {
        try {
            summaryArea.setText("Opening User Guide...\n\n"
                    + "You can visit the following link manually:\n"
                    + USER_GUIDE_URL);
        } catch (Exception e) {
            logger.warning("Failed to load User Guide: " + e.getMessage());
            String fallbackText = "Unable to load User Guide.\n"
                    + "Please check your internet connection or visit manually:\n"
                    + USER_GUIDE_URL;
            summaryArea.setText(fallbackText);
        }
    }

    /**
     * Loads a formatted summary (used for `help` command output).
     */
    public void loadSummaryText(String summary) {
        if (summary == null || summary.isBlank()) {
            summaryArea.setText("No help content available.");
            return;
        }

        String plainText = summary
                .replaceAll("(?i)<br\\s*/?>", "\n")
                .replaceAll("(?i)</tr>", "\n")
                .replaceAll("(?i)</td>", "  ") // small spacing for columns
                .replaceAll("(?i)</p>", "\n\n")
                .replaceAll("(?i)<li>", " - ")
                .replaceAll("(?i)</li>", "\n")
                .replaceAll("&nbsp;", " ");

        plainText = plainText.replaceAll("<[^>]*>", "");

        plainText = plainText.replaceAll("(?i)View full guide", "").trim();

        plainText = plainText.replaceAll("(?i)(?<=')Add", "\nAdd");

        plainText = plainText.replaceAll("(?m)^[ \\t]*\\r?\\n", "\n").replaceAll("\n{3,}", "\n\n");

        plainText += "\n\nView full guide at:\n" + USER_GUIDE_URL;

        summaryArea.setText(plainText.trim());
    }
}
