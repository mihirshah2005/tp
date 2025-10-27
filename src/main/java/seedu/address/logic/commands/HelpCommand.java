package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window with summary.";

    /**
     * Executes the Help command and returns a {@code CommandResult} containing
     * a formatted list of available sample commands and their usage examples.
     */
    @Override
    public CommandResult execute(Model model) {
        assert model != null : "Model should not be null when executing HelpCommand";
        StringBuilder sb = new StringBuilder();

        sb.append("===== Command Summary =====\n")
                .append("addstu  - Add a new student\n")
                .append("addvol  - Add a new volunteer\n")
                .append("edit    - Edit an existing person\n")
                .append("find    - Find persons by name keyword(s)\n")
                .append("findtag - Find persons by tag\n")
                .append("pair    - Pair students and volunteers\n")
                .append("unpair  - Unpair existing pairs\n")
                .append("delete  - Delete a person by index\n")
                .append("list    - List all persons\n")
                .append("clear   - Clear all entries\n")
                .append("exit    - Exit the program\n")
                .append("help    - Viewing help\n\n")
                .append("For full command details and examples, visit:\n")
                .append("https://ay2526s1-cs2103t-f10-1.github.io/tp/UserGuide.html");

        String summaryText = sb.toString();

        CommandResult result = new CommandResult(SHOWING_HELP_MESSAGE, true, false);
        result.setHelpContent(summaryText);
        return result;
    }
}
