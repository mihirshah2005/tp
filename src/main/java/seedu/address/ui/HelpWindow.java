package seedu.address.ui;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * A standalone window that displays the app's User Guide.
 */
public class HelpWindow extends UiPart<Stage> {

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    private static final String CLASSPATH_USERGUIDE = "/docs/UserGuide.md";
    private static final Path FS_USERGUIDE = Paths.get("docs", "UserGuide.md");

    @FXML
    private WebView webView;

    /**
     * Creates a {@code HelpWindow} bound to the given {@link Stage} and loads the User Guide content.
     *
     * @param root the JavaFX stage to use as the window root; must not be {@code null}.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        loadUserGuide();
    }

    /**
     * Creates a {@code HelpWindow} with a new {@link Stage} and loads the User Guide content.
     * Equivalent to {@code new HelpWindow(new Stage())}.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the help window and centers it on screen.
     * <p>If the window is already visible, this call is a no-op except for bringing
     * the window to the front due to JavaFX semantics.</p>
     */
    public void show() {
        logger.fine("Showing help window.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns whether the help window is currently visible.
     *
     * @return {@code true} if the underlying {@link Stage} is showing; {@code false} otherwise.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window without disposing it, allowing it to be shown again later via {@link #show()}.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Hides the help window without disposing it, allowing it to be shown again later via {@link #show()}.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    private void loadUserGuide() {
        try {
            String md = readUserGuideMarkdown();
            if (md == null) {
                webView.getEngine().loadContent("<h2>UserGuide not found</h2>");
                return;
            }
            Parser parser = Parser.builder().build();
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            Node document = parser.parse(md);
            String html = "<html><head><meta charset='UTF-8'></head><body style='font-family:sans-serif;padding:1em;'>"
                    + renderer.render(document) + "</body></html>";
            webView.getEngine().loadContent(html);
        } catch (Exception e) {
            webView.getEngine().loadContent("<h2>Failed to load UserGuide:</h2><pre>" + e.getMessage() + "</pre>");
        }
    }

    private String readUserGuideMarkdown() throws Exception {
        InputStream is = getClass().getResourceAsStream(CLASSPATH_USERGUIDE);
        if (is != null) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
        if (Files.exists(FS_USERGUIDE)) {
            return Files.readString(FS_USERGUIDE, StandardCharsets.UTF_8);
        }
        return null;
    }
}
