package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.FindByTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsTagPredicate;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new FindByTagCommand object.
 */
public class FindByTagCommandParser implements Parser<FindByTagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindByTagCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public FindByTagCommand parse(String userInput) throws ParseException {
        String tagName = userInput.trim();
        if (!Tag.isValidTagName(tagName)) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindByTagCommand.MESSAGE_USAGE));
        }
        return new FindByTagCommand(new NameContainsTagPredicate(new Tag(tagName)));
    }
}
