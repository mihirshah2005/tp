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
    public static final String MESSAGE_ALREADY_OPEN =
            "The help window is already open. If it cannot be found, "
                    + "check if it is minimised or at the edge of the screen.";

    private final String commandWord;
    public HelpCommand() {
        this.commandWord = null;
    }

    public HelpCommand(String commandWord) {
        this.commandWord = commandWord == null ? null : commandWord.trim().toLowerCase();
    }


    /**
     * Executes the Help command and returns a {@code CommandResult} containing
     * a formatted list of available sample commands and their usage examples.
     */
    @Override
    public CommandResult execute(Model model) {
        assert model != null : "Model should not be null when executing HelpCommand";

        if (commandWord != null && !commandWord.isEmpty()) {
            String fullHtml = getFullHelpHtml();
            String filtered = extractCommandSection(fullHtml, commandWord);

            if (filtered == null) {
                filtered = "<p>Unknown command: <b>" + commandWord + "</b></p>"
                        + "<p>Type <code>help</code> to see all available commands.</p>";
            }

            CommandResult result = new CommandResult("Opened help for command: " + commandWord, true, false);
            result.setHelpContent(filtered);
            return result;
        }

        CommandResult result = new CommandResult("Opened help window with command summary.", true, false);
        result.setHelpContent(getFullHelpHtml());
        return result;
    }
    private String getFullHelpHtml() {
        return """
        <h1>Command Summary</h1>
        <table>
          <tr><th>Action</th><th>Format, Example</th></tr>

          <tr><td>Add student</td>
              <td><code>addstu n/NAME [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…</code><br>
              e.g., <code>addstu n/James Ho p/22224444 e/jamesho@example.com a/123,
              Clementi Rd, 1234665 t/friend t/colleague</code></td></tr>

          <tr><td>Add volunteer</td>
              <td><code>addvol n/NAME [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…</code><br>
              e.g., <code>addvol n/Jane Roe p/93334444 e/jane@example.com a/45,
              River Valley Rd, 238000 t/mentor</code></td></tr>

          <tr><td>Clear</td>
              <td><code>clear</code></td></tr>

          <tr><td>Delete</td>
              <td><code>delete INDEX</code><br>
              e.g., <code>delete 3</code></td></tr>

          <tr><td>Edit</td>
              <td><code>edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…</code><br>
              e.g., <code>edit 2 n/James Lee e/jameslee@example.com</code></td></tr>

          <tr><td>Find</td>
              <td><code>find KEYWORD [MORE_KEYWORDS]</code><br>
              e.g., <code>find James Jake</code></td></tr>

          <tr><td>Find by tag</td>
              <td><code>findtag TAG [MORE_TAGS]</code><br>
              e.g., <code>findtag math science</code></td></tr>

          <tr><td>Pair</td>
              <td><code>pair INDEX 1ST_PARTNER_INDEX 2ND_PARTNER_INDEX ...</code><br>
              e.g., <code>pair 2 1 3</code></td></tr>

          <tr><td>Unpair</td>
              <td><code>unpair INDEX 1ST_PARTNER_INDEX 2ND_PARTNER_INDEX ...</code><br>
              e.g., <code>unpair 2 1 3</code></td></tr>

          <tr><td>List</td>
              <td><code>list</code></td></tr>

          <tr><td>Exit</td>
              <td><code>exit</code></td></tr>

          <tr><td>Help</td>
              <td><code>help</code></td></tr>
        </table>

        <p>For full command details and examples, visit:<br>
        <a href="https://ay2526s1-cs2103t-f10-1.github.io/tp/UserGuide.html" target="_blank">User Guide</a></p>""";
    }

    private String extractCommandSection(String html, String commandWord) {
        String lowerHtml = html.toLowerCase();
        int start = lowerHtml.indexOf("<td>" + commandWord);
        if (start == -1) {
            start = lowerHtml.indexOf("<code>" + commandWord);
            if (start == -1) {
                return null;
            }
        }
        int trStart = lowerHtml.lastIndexOf("<tr>", start);
        int trEnd = lowerHtml.indexOf("</tr>", start);
        if (trStart == -1 || trEnd == -1) {
            return null;
        }

        return "<h1>Help for '" + commandWord + "'</h1><table>"
                + html.substring(trStart, trEnd + 5)
                + "</table>"
                + "<p><a href='https://ay2526s1-cs2103t-f10-1.github.io/tp/UserGuide.html' "
                + "target='_blank'>View full guide</a></p>";
    }
}
