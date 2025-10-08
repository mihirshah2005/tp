package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.PairCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new PairCommand object
 */
public class PairCommandParser implements Parser<PairCommand> {

    @Override
    public PairCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmed = args.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, PairCommand.MESSAGE_USAGE));
        }

        String[] tokens = trimmed.split("\\s+");
        if (tokens.length < 2) {
            // need at least one partner index after the main index
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, PairCommand.MESSAGE_USAGE));
        }

        Index mainIndex;
        try {
            mainIndex = ParserUtil.parseIndex(tokens[0]);
        } catch (IllegalValueException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, PairCommand.MESSAGE_USAGE), e);
        }

        List<Index> paired = new ArrayList<>();
        for (int i = 1; i < tokens.length; i++) {
            try {
                Index idx = ParserUtil.parseIndex(tokens[i]);
                // avoid pairing to self or duplicate entries
                if (!idx.equals(mainIndex) && paired.stream().noneMatch(idx::equals)) {
                    paired.add(idx);
                }
            } catch (IllegalValueException e) {
                throw new ParseException("Invalid index: " + tokens[i], e);
            }
        }

        if (paired.isEmpty()) {
            throw new ParseException("At least one index to pair is required.");
        }

        return new PairCommand(mainIndex, paired);
    }
}
