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

        assertTrue(result.getFeedbackToUser().startsWith("Here are some sample commands"),
                "Help message should start with the expected prefix.");

        assertTrue(result.isShowHelp(), "Help window flag should be true.");

        assertFalse(result.isExit(), "Exit flag should be false.");

        assertEquals(model, expectedModel, "Model state should remain unchanged after help command.");
    }

}
