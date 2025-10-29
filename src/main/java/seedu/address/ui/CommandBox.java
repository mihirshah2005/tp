package seedu.address.ui;

import java.util.function.UnaryOperator;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private final CommandExecutor commandExecutor;

    @FXML
    private TextArea commandTextArea;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor}.
     */
    public CommandBox(CommandExecutor commandExecutor) {
        super(FXML);
        this.commandExecutor = commandExecutor;

        UnaryOperator<TextFormatter.Change> oneLineFilter = change -> {
            String t = change.getText();
            if (t != null && !t.isEmpty()) {
                // \R matches any linebreak sequence (\n, \r, \r\n, Unicode LS/PS)
                change.setText(t.replaceAll("\\R+", " "));
            }
            return change;
        };
        commandTextArea.setTextFormatter(new TextFormatter<>(oneLineFilter));

        // Clear error style when text changes
        commandTextArea.textProperty().addListener((u1, u2, u3) -> setStyleToDefault());

        // Submit on Enter
        commandTextArea.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ENTER && !e.isShiftDown()) {
                e.consume();
                handleCommandEntered();
            }
        });
        commandTextArea.setPrefRowCount(1);
        commandTextArea.setWrapText(true);
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandEntered() {
        String commandText = commandTextArea.getText();
        if (commandText.trim().isEmpty()) {
            return;
        }

        // If any cases with newlines are to escape the constructor then this gives the reason
        if (commandText.contains("\n") || commandText.contains("\r")) {
            setStyleToIndicateCommandFailure();
            commandTextArea.setPromptText("Only single-line commands are accepted");
            return;
        }

        try {
            commandExecutor.execute(commandText);
            commandTextArea.setText("");
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        }
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextArea.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextArea.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see seedu.address.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }

}
