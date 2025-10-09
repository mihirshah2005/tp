package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.PairCommand;


public class PairCommandParserTest {

    private PairCommandParser parser = new PairCommandParser();

    @Test
    public void parse_validArgs_returnsPairCommand() {
        assertParseSuccess(parser, "1 2", new PairCommand(INDEX_FIRST_PERSON,
                Collections.singletonList(INDEX_SECOND_PERSON)));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, PairCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidNumberOfArgs_throwsParseException() {
        assertParseFailure(parser, "1", String.format(MESSAGE_INVALID_COMMAND_FORMAT, PairCommand.MESSAGE_USAGE));
    }
}
