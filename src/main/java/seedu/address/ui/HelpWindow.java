package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;
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
    private WebView webView;

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
        getRoot().setResizable(true);
        getRoot().show();
        getRoot().centerOnScreen();
    }

    public boolean isShowing() {
        return getRoot().isShowing();
    }

    public void hide() {
        getRoot().hide();
    }

    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Loads the online User Guide into the WebView.
     * If loading fails, shows a fallback message.
     */
    private void loadUserGuide() {
        try {
            webView.getEngine().load(USER_GUIDE_URL);
        } catch (Exception e) {
            logger.warning("Failed to load User Guide: " + e.getMessage());
            String fallbackHtml = "<html><body style='font-family:Segoe UI, sans-serif; padding:20px;'>"
                    + "<h2>Unable to load User Guide</h2>"
                    + "<p>Please check your internet connection or visit:<br>"
                    + "<a href='" + USER_GUIDE_URL + "'>" + USER_GUIDE_URL + "</a></p>"
                    + "</body></html>";
            webView.getEngine().loadContent(fallbackHtml);
        }
    }

    /**
     * Loads a formatted summary (used for `help` command output).
     */
    public void loadSummaryText(String summary) {
        if (summary == null || summary.isBlank()) {
            webView.getEngine().loadContent("<h2>No help content available.</h2>");
            return;
        }

        String css = """
        html, body {
            font-family: 'Segoe UI', Roboto, Arial, sans-serif;
            background-color: #2b2b2b;
            color: #e8e8e8;
            padding: 16px;
            margin: 0;
        }

        h1 {
            color: #ffffff;
            text-align: center;
            border-bottom: 1px solid #555;
            padding-bottom: 4px;
            margin: 0 0 6px 0;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 6px; /* minimal gap */
        }

        th, td {
            border: 1px solid #444;
            padding: 8px;
            text-align: left;
            vertical-align: top;
        }

        th {
            background-color: #3a3a3a;
            color: #fff;
        }

        tr:nth-child(even) {
            background-color: #333;
        }

        code {
            background-color: #1e1e1e;
            color: #e0e0e0;
            padding: 2px 4px;
            border-radius: 3px;
            font-family: "Consolas", monospace;
        }

        a {
            color: #4ea3ff;
            text-decoration: none;
        }

        a:hover {
            color: #82c4ff;
            text-decoration: underline;
        }

        p {
            margin-top: 8px;
            text-align: center;
        }""";

        String html = """
        <html>
          <head><style>%s</style></head>
          <body>%s</body>
        </html>
            """.formatted(css, summary);

        webView.getEngine().loadContent(html);

        // Make links open in browser
        webView.getEngine().locationProperty().addListener((obs, oldLoc, newLoc) -> {
            if (newLoc != null && !newLoc.isEmpty() && !newLoc.startsWith("about:")) {
                try {
                    java.awt.Desktop.getDesktop().browse(new java.net.URI(newLoc));
                    webView.getEngine().loadContent(html);
                } catch (Exception e) {
                    System.out.println("Failed to open link: " + e.getMessage());
                }
            }
        });
    }
}
