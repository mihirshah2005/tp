package seedu.address.ui;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;

public class HelpWindow extends UiPart<Stage> {

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    private static final String CLASSPATH_USERGUIDE = "/docs/UserGuide.md";
    private static final Path FS_USERGUIDE = Paths.get("docs", "UserGuide.md");

    @FXML
    private WebView webView;

    public HelpWindow(Stage root) {
        super(FXML, root);
        loadUserGuide();
    }

    public HelpWindow() { this(new Stage()); }

    public void show() {
        logger.fine("Showing help window.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    public boolean isShowing() { return getRoot().isShowing(); }
    public void hide() { getRoot().hide(); }
    public void focus() { getRoot().requestFocus(); }

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
