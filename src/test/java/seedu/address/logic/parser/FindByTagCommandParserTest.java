package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindByTagCommand;
import seedu.address.model.person.NameContainsTagPredicate;
import seedu.address.model.tag.Tag;

/**
 * Contains integration tests for {@code FindByTagCommandParser}.
 */
public class FindByTagCommandParserTest {

    private final FindByTagCommandParser parser = new FindByTagCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindByTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidTagName_throwsParseException() {
        String invalidTag = "CS2103/T+CS2101";
        assert !Tag.isValidTagName(invalidTag);
        assertParseFailure(parser, invalidTag,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindByTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindByTagCommand() {
        Tag expectedTag = new Tag(VALID_TAG_FRIEND);
        FindByTagCommand expectedCommand = new FindByTagCommand(new NameContainsTagPredicate(expectedTag));

        assertParseSuccess(parser, VALID_TAG_FRIEND, expectedCommand);
        assertParseSuccess(parser, "    " + VALID_TAG_FRIEND, expectedCommand);
        assertParseSuccess(parser, VALID_TAG_FRIEND + "    \t", expectedCommand);
        assertParseSuccess(parser, " \t   " + VALID_TAG_FRIEND + "\t    \t ", expectedCommand);
    }
}
