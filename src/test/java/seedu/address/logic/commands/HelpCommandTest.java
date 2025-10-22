package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class HelpCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_help_success() {
        CommandResult actualCommandResult = new HelpCommand().execute(model);
        assertTrue(actualCommandResult.getFeedbackToUser().startsWith("Here are some sample commands"));
        assertFalse(actualCommandResult.isShowHelp());
        assertFalse(actualCommandResult.isExit());
        assertCommandSuccess(new HelpCommand(), model, actualCommandResult, expectedModel);
    }
}
