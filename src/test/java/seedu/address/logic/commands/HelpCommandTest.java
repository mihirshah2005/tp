package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class HelpCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_help_success() {
        CommandResult result = new HelpCommand().execute(model);

        assertEquals(HelpCommand.SHOWING_HELP_MESSAGE, result.getFeedbackToUser(),
                "CLI should show the short confirmation message.");

        assertTrue(result.isShowHelp(), "Help window flag should be true.");

        assertFalse(result.isExit(), "Exit flag should be false.");

        assertEquals(model, expectedModel, "Model state should remain unchanged after help command.");
    }

}
